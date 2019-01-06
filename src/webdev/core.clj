(ns webdev.core
  (:require [webdev.model :as model]
            [webdev.handler :as handler])
  (:require [ring.adapter.jetty :as jetty]
            [ring.handler.dump :refer [handle-dump]]
            [ring.middleware.reload :as ring-reload]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.resource :refer [wrap-resource]]
            [ring.middleware.file-info :refer [wrap-file-info]]
            [compojure.core :refer [routes GET POST PUT DELETE ANY]]
            [compojure.route :as route]))

; Defeat private defn
(def reloader #'ring-reload/reloader)

(defn wrap-server-name [handler]
  (fn [req]
    (let [res (handler req)
          headers (get res :headers {})
          updated-headers (assoc headers "Server" "Panta")]
      (assoc res :headers updated-headers))))

(defn wrap-db [handler db]
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

(defn make-routes [dev]
  (routes
   (ANY "/request" [] handle-dump)

   (GET "/items" [] handler/handle-index-items)
   (POST "/items" [] handler/handle-create-item)
   (DELETE "/items/:item-id" [] handler/handle-delete-item)
   (PUT "/items/:item-id/checked" [] handler/handle-set-item-checked)

   (route/not-found "Not found")))

(defn make-app [settings]
  (let
   [routes (make-routes (:dev settings))]
    (wrap-simulated-methods
     (wrap-server-name
      (wrap-file-info
       (wrap-resource
        (wrap-db
         (wrap-params
          routes) (:db settings))
        "static"))))))

(defn load-settings [args dev]
  {:port (Integer. (nth args 0 3000))
   :db (nth args 1 "jdbc:postgresql://localhost/webdev?password=qweasd")
   :dev dev})

(defn make-reloading-app [settings]
  (let [reload! (reloader ["src"] true)]
    (fn [request]
      (reload!)
      ((make-app settings) request))))

(defn start [settings]
  (let
   [app
    (if (:dev settings)
      (make-reloading-app settings)
      (make-app settings))]
    (println "Running in " (if (:dev settings) "DEVELOPMENT" "PRODUCTION") " mode")
    (model/create-tables (:db settings))
    (jetty/run-jetty app {:port (:port settings)})))

(defn -main [& args]
  (start (load-settings args false)))