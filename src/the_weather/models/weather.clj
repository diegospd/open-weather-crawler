(ns the-weather.models.weather
  (:require [schema.core :as s])
  (:import (org.joda.time DateTime)))

(s/defschema Wind
  {:speed s/Num
   :deg   s/Num
   :gust  s/Num})

(s/defschema Values
  {:temperature-units s/Keyword
   :temp              s/Num
   :feels_like        s/Num
   :temp_min          s/Num
   :temp_max          s/Num
   :pressure          s/Num
   :humidity          s/Num
   :wind              Wind
   :clouds            s/Num
   :visibility        s/Num
   })

(s/defschema Weather
  {:main        s/Str
   :description s/Str
   :weather     Values
   :as-of       DateTime})
