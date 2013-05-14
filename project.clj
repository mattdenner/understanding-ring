(defproject understanding-ring "0.1.0-SNAPSHOT"
  :description "Understanding Clojure ring"
  :url "http://github.com/mattdenner/understanding-ring"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies     [[org.clojure/clojure     "1.5.1"]
                     [ring/ring-core          "1.1.8"]
                     [ring/ring-jetty-adapter "1.1.8"]]
  :plugins          [[lein-ring               "0.8.5" :exclusions [org.clojure/clojure]]]
  :dev-dependencies [[ring/ring-devel         "1.1.8"]]
  :profiles {:dev {:dependencies [[midje "1.5.0"]]}}
  :ring {:handler understanding-ring.core/handler})
