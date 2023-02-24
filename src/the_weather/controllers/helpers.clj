(ns the-weather.controllers.helpers
  (:require [clojure.core.async :as async])
  (:import (clojure.lang ExceptionInfo)))

(defn tap!
  ([msg]
   (println msg))

  ([msg x]
   (println msg)
   (print "    ")
   (println x)
   x))

(defn in-parallel!
  [io-fn! xs n-threads]
  (let [out (async/chan)]
    (async/pipeline-blocking
      n-threads
      out
      (map io-fn!)
      (async/to-chan! xs))
    (async/reduce conj! (transient []) out)))

(defn run-in-parallel-&-wait!
  [io-fn! xs n-threads]
  (-> io-fn!
      (in-parallel! xs n-threads)
      async/<!!
      persistent!))

(defn timing! [f]
  (let [start  (System/nanoTime)
        result (f)
        end    (System/nanoTime)]
    {:seconds (/ (- end start) 1e9)
     :results result }))

(defn reporting-errors! [f x errors-atom]
  (let [append! (fn [x] (partial cons x))]
    (try (f x)
         (catch ExceptionInfo e
           (swap! errors-atom
                  (append! {:value     x
                            :exception (select-keys (ex-data e)
                                                    [:status :body])}))))))
