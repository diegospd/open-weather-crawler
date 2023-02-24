(ns the-weather.controllers.dataset
  (:require [schema.core :as s]
            [the-weather.models.travel :as models.travel]
            [the-weather.logic.airport :as logic.airport]
            [the-weather.controllers.weather :as controllers.weather]
            [the-weather.diplomat.filesystem :as diplomat.filesystem]
            [the-weather.adapters.travel :as adapters.travel]))

(def ^:private dataset-path "resources/challenge_dataset.csv")

(s/defn check-weather-at-airports! :- [models.travel/TravelAndWeather]
  [max-calls-by-minute :- s/Int
   n-threads           :- s/Int
   api-key             :- s/Str]
  (let [travels           (diplomat.filesystem/travels-dataset! dataset-path)
        airports-weathers (controllers.weather/check-at-airports! max-calls-by-minute n-threads api-key travels)]
    (-> airports-weathers
        logic.airport/airport-code->weather
        (adapters.travel/travels-and-weather travels))))
