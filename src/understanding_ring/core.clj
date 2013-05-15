(ns understanding-ring.core
  (:use ring.adapter.jetty)
  (:use ring.util.response))

; Handlers are functions that take requests (a map) and generate responses (another map).
(defn handler [request]
  (-> (response "Hello world!")
      (content-type "text/plain")
      (charset "utf-8")))

; Middlewares are functions that take the handler to wrap in their behaviour, and return a 
; function (a wrapped handler) doing exactly that.
(defn reversing-middleware [handler]
  (fn [request]
    (let [{body :body, :as full} (handler request)]
      (assoc full :body (apply str (reverse body))))))

; You can write middleware that does whatever you want, like deciding which handler to call
; based on the incoming request.
(defn directed-middleware [predicate positive negative]
  (fn [request] ((if (predicate request) positive negative) request)))

; You application then becomes a stack of middleware wrapping around handlers.
(def app
  (directed-middleware
    (fn [request] (= "/forward" (request :uri)))
    handler
    (reversing-middleware handler)))
