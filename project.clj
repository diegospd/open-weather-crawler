(defproject the-weather "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :aliases {"clj-kondo-deps" ["with-profile" "+test" "clj-kondo" "--copy-configs" "--dependencies" "--parallel" "--lint" "$classpath"]
            "clj-kondo-lint" ["do" ["clj-kondo-deps"] ["with-profile" "+test" "clj-kondo"]]}
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url  "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [org.clojure/data.csv "1.0.1"]
                 [clj-http "3.12.3"]
                 [cheshire "5.11.0"]
                 [org.clojure/core.async "1.5.648"]
                 [clj-time "0.15.2"]
                 [prismatic/schema "1.4.1"]]
  :main ^:skip-aot the-weather.core
  :target-path "target/%s"
  :plugins [[com.github.clj-kondo/lein-clj-kondo "0.2.1"]]
  :profiles {:uberjar {:aot      :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}})
