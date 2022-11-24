(ns the-weather.core
  (:require [schema.core :as s]
            [the-weather.controllers.dataset :as controllers.dataset])
  (:gen-class))

(def open-weather-api-key
  "PASTE YOUR KEY HERE")

(s/defn -main
  [& args]
  (let [max-api-calls-per-minute     60
        amount-of-parallel-api-calls 6]
    (println (controllers.dataset/check-weather-at-airports! max-api-calls-per-minute
                                                             amount-of-parallel-api-calls
                                                             open-weather-api-key))))
