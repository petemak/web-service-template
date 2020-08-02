(ns web-service.server-test
  (:use midje.sweet) ;; for against-background, before, after
  (:require [conf-er :as conf]
            [clj-http.client :as client]
            [web-service.server :as ws]))

;; ----------------------------------------------------
;; The web service
;;  1) can be started using (start-server)
;;  2) and stopped using (stop-server)
;;
;; This gives us the opportunity to use
;; the service in an integration test
;; 
;; We use the "against-background" macro from Midje to:
;;  1) fire up the service before tests
;;  2) shut it down after tghe tests
;; 
;; We can therefore do a real HTTP call
;; ----------------------------------------------------
(against-background [(before :contents (ws/start-server nil))
                     (after :contents (ws/stop-server))]

;; ----------------------------------------------------
;; The smallest checkable unit in Midje is the fact.
;; Fact is a gesture toward what we really want:
;; - to be able to make claims about our code that are
;;   as strong as mathematical facts.
;; ----------------------------------------------------
(fact "The version is served at the root "
      (let [port (conf/config :port)
            vers (conf/config :service-version)
            resp (client/get (str "http://localhost:" port "/api/ver"))]
        (nil? resp) => false
        (:status resp) => 200
        (:body resp) => "0.0.1")))




;; ----------------------------------------------------
;; To run all the tests, and check all the facts:
;; - lein midje
;;
;; To run individual namespaces:
;; - lein midje web-service.core-test
;;   
;; ----------------------------------------------------
