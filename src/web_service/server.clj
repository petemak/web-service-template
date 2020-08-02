(ns web-service.server
  (:require [conf-er :as conf]
            [web-service.routes :as routes]
            [ring.adapter.jetty :refer [run-jetty]]))

;; --------------------------------------------------
;;  Atom (shared/synchronous/uncoordianted
;;          reference type to hold server instance
;; --------------------------------------------------
(def server (atom nil))


;; --------------------------------------------------
;;  Starting the server and storing reference
;; --------------------------------------------------
(defn start-server
  "Side effects! starts the server and stores
  reference in the server atom"
  [db]
  (let [port (conf/config :port)] 
    (swap! server (fn [_]
                    (run-jetty (routes/create-handler db)
                               {:port port
                                :join? false}))) 
    (println "::-> Jetty started and running on port: " port)))



;; --------------------------------------------------
;;  Stop the server stored in the reference
;; --------------------------------------------------
(defn stop-server
  "Side effects! sops the server using the
  reference in the server atom"
  []
  (if (some? @server)
    (.stop @server)
    (println "Server stopped!")))

