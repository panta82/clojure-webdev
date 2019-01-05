(ns webdev.core
  (:require [webdev.model :as model]
            [webdev.handler :as handler])
  (:require [ring.adapter.jetty :as jetty]
            [ring.handler.dump :refer [handle-dump]]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.resource :refer [wrap-resource]]
            [ring.middleware.file-info :refer [wrap-file-info]]
            [compojure.core :refer [defroutes GET POST DELETE ANY]]
            [compojure.route :as route]))

(def db "jdbc:postgresql://localhost/webdev?password=qweasd")

(defn greet [req]
  {:status 200
   :body "Hello World"
   :headers {}})

(defn goodbye [req]
  {:status 200
   :body "Goodbye!"})

(defn about [req]
  {:status 200
   :body "Why am I adding this? Not exactly super interesting"})

(defn request [req] handle-dump)

(defn greeting [req]
  {:status 200
   :body (str "Yo! " (:name (:route-params req)))})

(defn calc [req]
  (let [params (:route-params req)
        a (Integer. (:a params))
        op (:op params)
        b (Integer. (:b params))
        res (case op
              "+" (+ a b)
              "-" (- a b)
              "*" (* a b)
              ":" (/ a b)
              nil)]
    (if (nil? res)
      {:status 400 :body (str "Invalid operation " op)}
      {:status 200 :body (str a " " op " " b " = " res)})))

(defroutes routes
  (ANY "/request" [] request)

  (GET "/" [] greet)
  (GET "/goodbye" [] goodbye)
  (GET "/about" [] about)
  (GET "/yo/:name" [] greeting)
  (GET "/calc/:a/:op/:b" [] calc)

  (GET "/items" [] handler/handle-index-items)
  (POST "/items" [] handler/handle-create-item)
  (DELETE "/items/:item-id" [] handler/handle-delete-item)

  (route/not-found "Not found"))

(defn wrap-server-name [handler]
  (fn [req]
    (let [res (handler req)
          headers (get res :headers {})
          updated-headers (assoc headers "Server" "Panta")]
      (assoc res :headers updated-headers))))

(defn wrap-db [handler]
  (fn [req]
    (handler (assoc req :webdev/db db))))

(def sim-methods
  {"PUT" :put
   "DELETE" :delete})

(defn wrap-simulated-methods [handler]
  (fn [req]
    (if-let
     [method
      (and
       (= :post (:request-method req))
       (sim-methods (get-in :req [:params "_method"])))]
      (handler (assoc req :request-method method))
      (handler req))))

(def app
  (wrap-simulated-methods
   (wrap-server-name
    (wrap-file-info
     (wrap-resource
      (wrap-db
       (wrap-params
        routes))
      "static")))))

(defn args-to-options [args]
  {:port (nth args 0 3000)})

(defn start-server [app options]
  (model/create-tables db)
  (jetty/run-jetty app {:port (Integer. (:port options))}))

(defn -main [& args]
  (do [(println "Running in PRODUCTION mode")
       (start-server app (args-to-options args))]))