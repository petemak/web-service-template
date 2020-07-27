(ns web-service.routes-test
  (:use [midje.sweet])
  (:require [conf-er :as conf]
            [clj-http.client :as client]
            [web-service.routes :as routes]))


(fact "All roots  (ping, api..)  must return status 200 OK"
      (let [png-req {:request-method :get
                      :uri "/api/ping"}
            ver-req {:request-method :get
                     :uri "/api/ver"}
            usr-req {:request-method :get
                     :uri "/api/admin/user/user-101"}]
        (:status (routes/app png-req)) => 200
        (:status (routes/app ver-req)) => 200
        (:status (routes/app usr-req)) => 200))



(fact "version route must return version as specified"
      (let [api-ver (conf/config :service-version)
            ver-req {:request-method :get
                     :uri "/api/ver"}]
        (:wrap (routes/app ver-req)) => '(:api)
        (:body (routes/app ver-req)) => api-ver))



(fact "User route must return a user map
       containing id, first and last "
      (let [usr-req {:request-method :get
                     :uri "/api/admin/user/user-101"}
            usr-pos {:request-method :post
                     :body-params {:user-id "user-101" :first-name "Bugs" :last-name "Bunny"}
                     :uri "/api/admin/user/user-101"}]
        (:wrap (routes/app usr-req)) => '(:api :admin)
        (:wrap (routes/app usr-pos)) => '(:api :admin)
        (some? (:body (routes/app usr-req))) => true
        (some? (:id (:body (routes/app usr-req)))) => true
        (some? (:first-name (:body (routes/app usr-req)))) => true
        (some? (:last-name (:body (routes/app usr-req)))) => true))
      
