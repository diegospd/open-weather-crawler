(ns the-weather.adapters.travel
  (:require [the-weather.models.travel :as models.travel]
            [the-weather.models.airport :as models.airport]
            [schema.core :as s]
            [clojure.string :as str]))

(defn- dissoc-nils
  [h]
  (into {} (filter (fn [[k v]] (and v k)) h)))

(defn- map-keys [f m]
  (->> (map (fn [[k v]] [(f k) v]) m)
       (into {})))

(s/defn ^:private parse-numeric
  [ks m]
  (if (nil? ks)
    m
    (let [update    #(update m % read-string)
          [k & rest] ks]
      (recur rest (update k)))))

(s/def ^:private numeric-keys :- [s/Keyword]
  (->> models.travel/travel-skeleton
       (filter (fn [[_key val]]
                 (= s/Num val)))
       (map first)))

(s/defn ^:private format-maps :- [{s/Keyword s/Any}]
  [maps :- [{s/Str s/Str}]]
  (let [key->keyword (fn [m] (map-keys (comp keyword #(str/replace % #"_" "-")) m))]
    (->> maps
         (map key->keyword)
         (map (partial parse-numeric numeric-keys)))))

(s/defn ^:private tabular->maps :- [{s/Str s/Str}]
  [tabular :- [s/Str]]
  (let [header (first tabular)]
    (->> (map zipmap (repeat header) (rest tabular))
         (mapv dissoc-nils))))

(s/defn tabular->travels :- [models.travel/Travel]
  [tabular :- [s/Str]]
  (-> tabular
      tabular->maps
      format-maps))

(s/defn travel->airports :- [models.airport/Airport]
  [{:keys [origin-longitude
           origin-latitude
           destination-longitude
           destination-latitude
           destination-name
           origin-name
           origin-iata-code
           destination-iata-code]} :- models.travel/Travel]
  [{:iata-code origin-iata-code
    :name      origin-name
    :longitude origin-longitude
    :latitude  origin-latitude}
   {:iata-code destination-iata-code
    :name      destination-name
    :longitude destination-longitude
    :latitude  destination-latitude}])

(s/defn travel-and-weather :- models.travel/TravelAndWeather
  [weather-map :- models.airport/AirportCode->Weather
   travel      :- models.travel/Travel]
  (merge travel
         {:destination-weather (-> travel :destination-iata-code weather-map)
          :origin-weather      (-> travel :origin-iata-code weather-map)}))

(s/defn travels-and-weather :- [models.travel/TravelAndWeather]
  [weather-map :- models.airport/AirportCode->Weather
   travels     :- [models.travel/Travel]]
  (map (partial travel-and-weather weather-map) travels))

