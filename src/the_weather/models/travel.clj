(ns the-weather.models.travel
  (:require [schema.core :as s]
            [the-weather.models.weather :as models.weather]))

(def travel-skeleton
  {:destination-iata-code s/Str
   :origin-iata-code      s/Str
   :airline               s/Str
   :flight-num            s/Str
   :origin-latitude       s/Num
   :destination-latitude  s/Num
   :origin                s/Str
   :origin-name           s/Str
   :destination-name      s/Str
   :destination-longitude s/Num
   :origin-longitude      s/Num
   :destination           s/Str})

(s/defschema Travel travel-skeleton)

(s/defschema TravelAndWeather
  (merge travel-skeleton
         {:destination-weather models.weather/Weather
          :origin-weather      models.weather/Weather}))
