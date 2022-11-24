(ns the-weather.logic.travel
  (:require [the-weather.models.travel :as models.travel]
            [the-weather.models.airport :as models.airport]
            [schema.core :as s]
            [clojure.set :as set]
            [the-weather.adapters.travel :as adapters.travel]))

(s/defn airports :- #{models.airport/Airport}
  [travels :- [models.travel/Travel]]
  (->> travels
       (map adapters.travel/travel->airports)
       doall
       (reduce (fn [x y] (set/union (set x) y)) #{})))
