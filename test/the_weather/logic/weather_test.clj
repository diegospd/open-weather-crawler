(ns the-weather.logic.weather-test
  (:require [clojure.test :refer :all]
            [the-weather.logic.weather :as logic.weather]
            [schema.test :as s]))

(s/deftest current-weather-uri-test
           (testing "api urls to open weather are well formed"
             (is (= "https://api.openweathermap.org/data/2.5/weather?lat=1.2&lon=-1.2&appid=KEY"
                    (logic.weather/current-weather-uri "KEY" 1.2 -1.2)))))
