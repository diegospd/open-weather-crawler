(ns the-weather.logic.weather
  (:require [schema.core :as s]))

(s/defn current-weather-uri :- s/Str
        [api-key   :- s/Str
         latitude  :- s/Num
         longitude :- s/Num]
        (str "https://api.openweathermap.org/data/2.5/weather"
             "?lat=" latitude
             "&lon=" longitude
             "&appid=" api-key))
