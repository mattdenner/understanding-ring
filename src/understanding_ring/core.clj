(ns understanding-ring.core
  (:use ring.adapter.jetty)
  (:use ring.util.response)
  (:use compojure.core)
  (:require [compojure.handler :as compojure-handler]
            [compojure.route   :as compojure-route])
  )

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

; Here we use Compojure to route requests to an appropriate handler.
;
; Note that because we want to reuse the handler defined above we have to use 'make-route'
; rather than the 'GET' macro, because we need access to the request.  But it's still pretty
; clear.
(defroutes forwards-and-backwards
  (make-route :get "/forwards" handler)
  (make-route :get "/backwards" (reversing-middleware handler))
  (compojure-route/not-found "Sorry, not found!"))

; Because routes are named we can define multiple sets and then combine them in our application
; so that it picks the appropriate.  The rule is, though, that the routes must not handle the
; request (which includes the "not-found" style of error) for the next group to be called.
(defroutes left-and-right
  (GET "/left" [] (response "Left!"))
  (GET "/right" [] (response "Right!")))

(def app
  (-> (routes left-and-right forwards-and-backwards)
      compojure-handler/site
      ))
