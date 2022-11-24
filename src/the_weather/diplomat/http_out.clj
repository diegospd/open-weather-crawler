(ns the-weather.diplomat.http-out
  (:require [schema.core :as s]
            [cheshire.core :as json]
            [clj-http.client :as http]
            [clj-time.core :as time]
            [the-weather.adapters.weather :as adapters.weather]
            [the-weather.models.weather :as models.weather]
            [the-weather.logic.weather :as logic.weather]
            [the-weather.models.airport :as models.airport]))

(s/defn current-weather! :- models.weather/Weather
  [api-key   :- s/Str
   latitude  :- s/Num
   longitude :- s/Num]
  (-> api-key
      (logic.weather/current-weather-uri latitude longitude)
      (http/get)
      :body
      (json/decode true)
      (adapters.weather/wire->weather (time/now))))

(s/defn airport->weather! :- models.airport/AirportAndWeather
  [api-key                      :- s/Str
   {:keys [latitude longitude]
    :as   airport}              :- models.airport/Airport]
  {:airport airport
   :weather (current-weather! api-key latitude longitude)})
