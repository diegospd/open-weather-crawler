(ns the-weather.logic.travel-test
  (:require [clojure.test :refer :all]
            [schema.test :as s]
            [the-weather.mock.travel-data :as mock.travels]
            [the-weather.logic.travel :as logic.travel]))

(s/deftest airports-test
           (testing "output structure"
             (is (= [{:origin-longitude      -99.566
                      :origin-latitude       19.3371
                      :airline               "4O"
                      :destination-longitude -100.107
                      :destination-iata-code "MTY"
                      :flight-num            "104"
                      :origin                "TLC"
                      :destination-latitude  25.7785
                      :destination-name      "General Mariano Escobedo International Airport"
                      :destination           "MTY"
                      :origin-iata-code      "TLC"
                      :origin-name           "Licenciado Adolfo Lopez Mateos International Airport"}]
                    (take 1 mock.travels/travels))))

           (testing "returns the expected amount of airports"
             (are [travels airports-count]
               (is (= airports-count (count (logic.travel/airports travels))))
               ;; travels           airports-count
               [] 0
               mock.travels/travels 52)))


