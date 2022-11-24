(ns the-weather.adapters.weather
  (:require [the-weather.models.weather :as models.weather]
            [schema.core :as s])
  (:import (org.joda.time DateTime)))

(s/defn wire->weather :- models.weather/Weather
  [{:keys [wind weather clouds main visibility]}
   as-of :- DateTime]
  {:as-of       as-of
   :main        (-> weather first :main)
   :description (-> weather first :description)
   :weather     (merge main
                       {:temperature-units :kelvin
                        :wind              wind
                        :clouds            (:all clouds)
                        :visibility        visibility})})
