(ns the-weather.models.airport
  (:require [schema.core :as s]
            [the-weather.models.weather :as models.weather]))


(s/defschema Airport
  {:iata-code s/Str
   :name      s/Str
   :longitude s/Num
   :latitude  s/Num})

(s/defschema AirportAndWeather
  {:airport Airport
   :weather models.weather/Weather})

(s/defschema AirportCode->Weather
  {s/Int models.weather/Weather})