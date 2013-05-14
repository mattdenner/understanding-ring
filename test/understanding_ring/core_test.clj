(ns understanding-ring.core-test
  (:use midje.sweet)
  (:use understanding-ring.core))

(facts "handler"
       (fact "returns a simple body"
             (handler {}) => (contains {:body "Hello world!"}))
       (fact "returns an HTTP 200 OK response"
             (handler {}) => (contains {:status 200}))
       (fact "returns a text/plain content"
             (:headers (handler {})) => (contains {"Content-Type" #"^text/plain;"}))
       (fact "returns the content in UTF-8"
             (:headers (handler {})) => (contains {"Content-Type" #"charset=utf-8"}))
       
       )
