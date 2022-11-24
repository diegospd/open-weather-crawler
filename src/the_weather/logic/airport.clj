(ns the-weather.logic.airport
  (:require [schema.core :as s]
           [the-weather.models.airport :as models.airport]))


(s/defn ^:private airport-&-weather->map :- models.airport/AirportCode->Weather
  [{:keys [airport weather]} :- models.airport/AirportAndWeather]
  {(:iata-code airport) weather})

(s/defn airport-code->weather :- models.airport/AirportCode->Weather
  [airports-and-weathers :- [models.airport/AirportAndWeather]]
  (reduce merge {} (map airport-&-weather->map airports-and-weathers)))
