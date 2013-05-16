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

(facts "forwards-and-backwards"
       (defn   valid-request [uri]        (forwards-and-backwards {:request-method :get,   :uri uri}))
       (defn invalid-request [method uri] (forwards-and-backwards {:request-method method, :uri uri}))

       (fact "/forwards returns forward text"
             (valid-request "/forwards") => (contains {:body "Hello world!"}))
       (fact "/backwards returns backwards text"
             (valid-request "/backwards") => (contains {:body "!dlrow olleH"}))
       (facts "any other URL causes a 404 Not Found response"
              (invalid-request :get "/missing")   => (contains {:status 404})
              (invalid-request :post "/forwards") => (contains {:status 404})
              )
       )

(facts "left-and-right"
       (defn   valid-request [uri]        (left-and-right {:request-method :get,   :uri uri}))
       (defn invalid-request [method uri] (left-and-right {:request-method method, :uri uri}))

       (fact "/left returns left"
             (valid-request "/left") => (contains {:body "Left!"}))
       (fact "/right returns right"
             (valid-request "/right") => (contains {:body "Right!"}))
       (facts "any other URI returns nil, which would cause compojure to try another route"
              (invalid-request :get "/missing") => nil
              (invalid-request :post "/left")   => nil
              )
       )

(facts "app"
       (defn   valid-request [uri]        (app {:request-method :get,   :uri uri}))
       (defn invalid-request [method uri] (app {:request-method method, :uri uri}))

       (fact "/forwards returns forward text"
             (valid-request "/forwards") => (contains {:body "Hello world!"}))
       (fact "/backwards returns backwards text"
             (valid-request "/backwards") => (contains {:body "!dlrow olleH"}))
       (fact "/left returns left"
             (valid-request "/left") => (contains {:body "Left!"}))
       (fact "/right returns right"
             (valid-request "/right") => (contains {:body "Right!"}))
       )
