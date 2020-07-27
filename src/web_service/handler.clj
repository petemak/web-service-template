(ns web-service.handler
  (:require [conf-er :as conf]
            [clj-time.local :as ct]
            [clj-time.format :as ft]))

;; --------------------------------------------------
;;  Version handler
;; --------------------------------------------------
(defn version
  "Returns sevice version"
  [req]
  {:status 200
   :db (:db req)
   ;; :headers {"Content-Type" "text/html"}
   :body (conf/config :service-version)})


;; --------------------------------------------------
;;  Version handler
;; --------------------------------------------------
(defn ok-handler
  "Returns "
  [req]
  {:status 200
   :headers {"Content-Type" "text/plain"}
   :body (str {:uri (:uri req)
               :db (:db req)
               :status "OK"
               :remote-addr (:remote-addr req)
               :timestamp (ft/unparse (ft/formatter :date-hour-minute-second)
                                      (ct/local-now))})})



;; --------------------------------------------------
;;  Version handler
;; --------------------------------------------------
(defn user-handler
  [req]
  {:status 200
   :headers {"Content-Type" "text/plain"}
   :body {:id (:user-id (:path-params req))
          :first-name "Tom"
          :last-name "Jasper"}})

