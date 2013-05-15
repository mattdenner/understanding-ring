(ns understanding-ring.core-test
  (:use midje.sweet)
  (:use understanding-ring.core))

(facts "handler"
       (fact "returns a simple body"
             (handler {}) => (contains {:body "Hello world!"}))
       (fact "returns an HTTP 200 OK response"
             (handler {}) => (contains {:status 200}))
       (fact "returns a text/plain content in UTF-8"
             (:headers (handler {})) => (contains {"Content-Type" #"^text/plain;"})
             (:headers (handler {})) => (contains {"Content-Type" #"charset=utf-8"}))
       )

(facts "reversing-middleware"
       (def wrapped (reversing-middleware (fn [r] {:body "abcde"})))
       (fact "returns a function that reverses the body of the handler"
             (wrapped {}) => (contains {:body "edcba"}))
       )

(facts "directed-middleware"
       (def wrapped
         (directed-middleware
           (fn [request] (= (request :url) "left"))
           (fn [request] "the left")
           (fn [request] "the right")))

       (fact "returns the appropriate result based on the predicate"
             (wrapped {:url "left"})  => "the left"
             (wrapped {:url "right"}) => "the right")
       )
