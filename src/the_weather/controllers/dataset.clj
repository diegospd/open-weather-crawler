(ns the-weather.controllers.dataset
  (:require [clojure.data.csv :as csv]
            [the-weather.models.travel :as models.travel]
            [the-weather.logic.airport :as logic.airport]
            [the-weather.controllers.weather :as controllers.weather]
            [schema.core :as s]
            [clojure.java.io :as io]
            [the-weather.adapters.travel :as adapters.travel]))

(def ^:private dataset-path "resources/challenge_dataset.csv")

(defn- empty-string-to-nil
  [s]
  (if (and (string? s) (empty? s))
    nil
    s))

(s/defn ^:private load-csv! :- [s/Str]
  [filepath :- s/Str]
  (with-open [reader (io/reader filepath)]
    (->> (csv/read-csv reader)
         (map (fn [row] (map empty-string-to-nil row)))
         (doall))))

(s/defn load-&-parse! :- [models.travel/Travel]
  []
  (-> dataset-path
      load-csv!
      adapters.travel/tabular->travels))


(s/defn check-weather-at-airports! :- [models.travel/TravelAndWeather]
  [max-calls-by-minute :- s/Int
   n-threads           :- s/Int
   api-key             :- s/Str]
  (let [travels           (load-&-parse!)
        airports-weathers (controllers.weather/check-at-airports! max-calls-by-minute n-threads api-key travels)]
    (-> airports-weathers
        logic.airport/airport-code->weather
        (adapters.travel/travels-and-weather travels))))
