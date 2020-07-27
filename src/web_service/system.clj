(ns web-service.system
  (:require [integrant.core :as ig]
            [web-service.routes :as routes]
            [ring.adapter.jetty :refer [run-jetty]]))

;;---------------------------------------------------------
;;Initialising components
;;---------------------------------------------------------
(def system-config
  {:web-service/server  {:handler (ig/ref :web-service/handler) :port 3449}
   :web-service/handler {:db      (ig/ref :web-service/db)}
   :web-service/db      {:db-uri  "datomic:mem//test"}})

(defmethod ig/init-key :web-service/server [s {:keys [handler port]}]
  (println "initialising Jetty server" s "on port ["  port "] with handler" handler)
  (run-jetty handler {:port port :join? false}))


(defmethod ig/init-key :web-service/handler [h {:keys [db]}]
  (println "initialising handler [" h "] with DB" db )
  (routes/create-handler db))


(defmethod ig/init-key :web-service/db [d {:keys [db-uri]}]
  (println "Initialising DB [" d "] with URI" db-uri)
  {:db-url db-uri})

;;---------------------------------------------------------
;; Halting components
;;---------------------------------------------------------
(defmethod ig/halt-key! :web-service/server [_ server]
  (println "Halting:" server)
  (.stop server))


(defmethod ig/halt-key! :web-service/handler [_ h]
  (println "Halting:" h))


(defmethod ig/halt-key! :web-service/db [_ d]
  (println "Halting:" d))



(defn -main
  []
  (ig/init system-config))

(comment

  (def system (ig/init system-config))
  (ig/halt! system)
  
  )
