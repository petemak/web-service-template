(ns workflow
  (:require [integrant.repl :as ig-repl]
            [web-service.system :as system]))

;; --------------------------------------
;; Defined define a zero-argument function
;; that returns the system configuration.
;; --------------------------------------
(ig-repl/set-prep! (fn [] system/system-config))

;; --------------------------------------
;; Starting the system
;; 
;; --------------------------------------
(defn start
  "Start systen.
   Also: ig-repl/go"
  []
  (ig-repl/prep)
  (ig-repl/init))


;; --------------------------------------
;; Stop the system
;; 
;; --------------------------------------
(defn stop
  "Shut down the system"
  []
  (ig-repl/halt))



;; --------------------------------------
;; Reload the system
;; 
;; --------------------------------------
(defn restart
  "Reload our source files and restart the system"
  []
  (ig-repl/reset))



(comment
  (start)
  (stop)
  (restart))
