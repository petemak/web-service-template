(ns web-service.routes
  (:require [muuntaja.core :as m]
            [reitit.ring :as ring]
            [reitit.swagger :as swagger]
            [reitit.coercion.spec :as rspec]
            [web-service.middleware :as middleware]
            [web-service.handler :as handler]
            [reitit.swagger-ui :as swagger-ui]
            [reitit.ring.coercion :as coercion]
            [reitit.ring.middleware.exception :as exception]
            [reitit.ring.middleware.muuntaja :as muuntaja])  )



;; --------------------------------------------------
;; Wrap identifier
;; --------------------------------------------------
(defn wrap
  [handler id]
  (fn [request]
    (update (handler request) :wrap (fnil conj '()) id)))

;; --------------------------------------------------
;; Routes
;;
;; Creates a handler for the /swagger.json route
;;
;; --------------------------------------------------
(def routes
  [
   ["/swagger.json" {:get {:no-doc true
                           :info {:title "Sample API with Reitit, Swagger etc."}
                           :handler (swagger/create-swagger-handler)}}]
   
   ["/api" {:swagger {:tags ["Client APIs"]}
            :middleware [[wrap :api]]}
    
    ["/ping"  {:get {:summary "Ping service for availability."
                     :handler handler/ok-handler}}]
    
    ["/ver"   {:get {:summary "Return API version number"
                     :handler handler/version}}]
    ["/admin" {:swagger {:tags ["Admin APIs"]}
               :middleware [[wrap :admin]]}
     ["/user/:user-id" {:get {:summary "Get user information"
                              :handler handler/user-handler
                              :parameters {:path {:user-id string?}}}
                        :post {:summary "Update user"
                               :parameters {:path {:user-id string?}
                                            :body {:user-id string?
                                                   :first-name string?
                                                   :last-name string?}}
                               :responses {200  {:schema {:user-id string?
                                                          :first-name string?
                                                          :last-name string?}}}
                               :handler handler/user-handler}}]]]])

;; --------------------------------------------------
;; Router
;;
;; - Injects muuntaja middleware into the router
;;   for fortmatting and coercion
;; 
;; - Defines and attaches coercion to the router:
;; Coercion is a process of transforming parameters
;; (and responses) from one format into another.
;;                                        
;; NOTE: exception middleware is set after response
;;       and before request formatting to catch
;;       errors in the input format!!!
;; --------------------------------------------------
(defn router
  "Create router and pass on DB to router data"
  [db]
  (ring/router routes
               {:data {:db db
                       :coercion rspec/coercion
                       :muuntaja m/instance
                       :middleware [swagger/swagger-feature
                                    muuntaja/format-negotiate-middleware
                                    muuntaja/format-response-middleware
                                    exception/exception-middleware
                                    muuntaja/format-request-middleware
                                    coercion/coerce-request-middleware
                                    coercion/coerce-response-middleware
                                    middleware/db-middleware]}}))



;; --------------------------------------------------
;; Returns handler function
;; Swagger UI is served as default handler,
;; the second argument to the reitit.ring/ring-handler
;; function.
;; --------------------------------------------------
(defn create-handler
  "Create applicaion main handler and
   with specified db for storage"
  [db]
  (ring/ring-handler (router db)
                     (swagger-ui/create-swagger-ui-handler {:path "/"})))
