(ns web-service.system
  (:require [conf-er :as conf]
            [integrant.core :as ig]
            [web-service.routes :as routes]
            [web-service.db :as wdb]
            [ring.adapter.jetty :refer [run-jetty]]))

;;---------------------------------------------------------
;;Define components
;; Server
;; Handler
;; DB
;; Configuration
;;---------------------------------------------------------
(def system-config
  {:web-service/server  {:handler (ig/ref :web-service/handler)
                         :cfg     (ig/ref :web-service/config)}
   :web-service/handler {:db      (ig/ref :web-service/db)}
   :web-service/db      {:cfg     (ig/ref :web-service/config)}
   :web-service/config  nil})

;;---------------------------------------------------------
;;Initialising components
;;---------------------------------------------------------
(defmethod ig/init-key :web-service/server [s {:keys [handler]
                                            {:keys [port]} :cfg}]
  (println "initialising Jetty server" s "on port ["  port "] with handler" handler)
  (run-jetty handler {:port port :join? false}))


(defmethod ig/init-key :web-service/handler [h {:keys [db]}]
  (println "initialising handler [" h "] with DB" db )
  (routes/create-handler db))


(defmethod ig/init-key :web-service/db [d {{:keys [db-uri]} :cfg}]
  (println "Initialising DB [" d "] with URI" db-uri)
  (wdb/init-db db-uri))

(defmethod ig/init-key :web-service/config [c _]
  {:port (conf/config :port)
   :db-uri (conf/config :db-uri)})



;;---------------------------------------------------------
;; Halting components
;;---------------------------------------------------------
(defmethod ig/halt-key! :web-service/server [_ server]
  (println "Halting:" server)
  (.stop server))


(defmethod ig/halt-key! :web-service/handler [_ h]
  (println "Halting:" h))


(defmethod ig/halt-key! :web-service/db [_ d]
  (println "Halting:" d)
  (wdb/shutdown))


(defmethod ig/halt-key! :web-service/config [_ c]
  (println "Halting:" c))



(defn -main
  "Entry point"
  []
  (ig/init system-config))

;;
;; For REPL 
(comment
  (def system (ig/init system-config))
  (ig/halt! system)  
  )
