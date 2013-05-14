(ns understanding-ring.core
  (:use ring.adapter.jetty)
  (:use ring.util.response))

(defn handler [request]
  (-> (response "Hello world!")
      (content-type "text/plain")
      (charset "utf-8")))
