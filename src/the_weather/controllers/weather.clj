(ns the-weather.controllers.weather
  (:require [the-weather.models.travel :as models.travel]
            [schema.core :as s]
            [the-weather.controllers.control :as controllers.control :refer [tap!]]
            [the-weather.models.airport :as models.airport]
            [the-weather.diplomat.http-out :as http-out]
            [the-weather.logic.travel :as logic.travel]))

(s/defn ^:private parallel-check!
  [airports    :- [models.travel/Travel]
   errors-atom
   n-threads   :- s/Int
   api-key     :- s/Str]
  (let [airport->weather+errors! #(controllers.control/reporting-errors! (partial http-out/airport->weather! api-key)
                                                                         %
                                                                         errors-atom)]
    (controllers.control/run-in-parallel-&-wait! airport->weather+errors! airports n-threads)))

(defn- maybe-wait-for-next-batch!
  [elapsed-seconds]
  (let [one-minute 62]
    (when (> one-minute elapsed-seconds)
      (Thread/sleep (* 1000 (tap! "seconds to sleep before next batch:"
                                  (- one-minute elapsed-seconds)))))))

(s/defn ^:private check-by-batch! :- [models.airport/AirportAndWeather]
  ([errors-atom max-calls-by-minute n-threads api-key airports]
   (check-by-batch! [] errors-atom max-calls-by-minute n-threads api-key airports))
  ([accumulated-results :- [models.airport/AirportAndWeather]
    errors-atom
    max-calls-by-minute :- s/Int
    n-threads           :- s/Int
    api-key             :- s/Str
    airports            :- [models.airport/Airport]]
   (if (empty? airports)
     accumulated-results
     (let [[batch remaining-airports] (split-at max-calls-by-minute airports)
           {:keys [seconds results]}  (controllers.control/timing!
                                        #(parallel-check! batch errors-atom n-threads api-key))]
       (when (not-empty remaining-airports)
         (maybe-wait-for-next-batch! seconds))
       (recur (concat accumulated-results results)
              errors-atom
              max-calls-by-minute
              n-threads
              api-key
              remaining-airports)))))

(s/defn ^:private parallel-check-by-batches-with-retry! :- [models.airport/AirportAndWeather]
  ([max-calls-by-minute n-threads api-key airports]
   (parallel-check-by-batches-with-retry! [] (atom []) max-calls-by-minute n-threads api-key airports))
  ([accumulated-results :- [models.airport/AirportAndWeather]
    errors-atom
    max-calls-by-minute :- s/Int
    n-threads           :- s/Int
    api-key             :- s/Str
    airports            :- [models.airport/Airport]]
   (let [results        (check-by-batch! errors-atom max-calls-by-minute n-threads api-key airports)
         total-results  (concat accumulated-results results)
         errors         @errors-atom
         retry-airports (->> errors (map :value) set vec)]
     (if (empty? retry-airports)
       total-results
       (do (tap! (str (count errors) " queries failed; sleeping 2 minutes before retrying..\n   Error codes:")
                 (set (map (fn [{:keys [exception]}] (:body exception))
                           errors)))
           (Thread/sleep (* 1000 60 2))
           (recur accumulated-results (atom []) max-calls-by-minute n-threads api-key retry-airports))))))

(s/defn check-at-airports! :- [models.airport/AirportAndWeather]
  [max-calls-by-minute :- s/Int
   n-threads           :- s/Int
   api-key             :- s/Str
   travels             :- [models.travel/Travel]]
  (tap! (str "Found " (count travels) " travel tickets"))
  (let [airports (logic.travel/airports travels)]
    (tap! (str "Found " (count airports) " different airports"))
    (parallel-check-by-batches-with-retry! max-calls-by-minute n-threads api-key airports)))
