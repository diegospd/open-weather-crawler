(ns the-weather.diplomat.filesystem
  (:require [schema.core :as s]
            [clojure.data.csv :as csv]
            [the-weather.adapters.travel :as adapters.travel]
            [clojure.java.io :as io]
            [the-weather.models.travel :as models.travel]))


(defn- empty-string-to-nil
  [s]
  (if (and (string? s) (empty? s))
    nil
    s))

(s/defn ^:private load-csv! :- [s/Str]
  [filepath :- s/Str]
  (with-open [reader (io/reader filepath)]
    (->> (csv/read-csv reader)
         (map (fn [row] (map empty-string-to-nil row)))
         (doall))))

(s/defn travels-dataset! :- [models.travel/Travel]
  [csv-filepath :- s/Str]
  (-> csv-filepath
      load-csv!
      adapters.travel/tabular->travels))

