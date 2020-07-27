(ns web-service.middleware)

;; Reitit middleware
;;
;; :name    Name of the middleware as a qualified keyword
;; :spec    clojure.spec definition for the route data
;; :wrap    The actual middleware function of:
;;          handler & args => request => response
;; :compile Middleware compilation function
;;          expects a function of:
;;          route-data router-opts => ?IntoMiddleware

(def db-middleware
  {:name ::db
   :compile (fn [{:keys [db]} router-opts]
              (fn [handler]
                (fn [request]
                  (handler (assoc request :db db)))))})
