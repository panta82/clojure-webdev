(ns webdev.core
  (:require [ring.adapter.jetty :as jetty]
            [ring.middleware.reload :refer [wrap-reload]]
            [compojure.core :refer :all]
            [compojure.route :as route]))

(defn greet [req]
  {:status 200
   :body "Hello World"
   :headers {}})

(defn goodbye [req]
  {:status 200
   :body "Goodbye!"})

(defroutes app
  (GET "/" [] greet)
  (GET "/goodbye" [] goodbye)
  (route/not-found "Not found"))

(defn args-to-options [args]
  {:port (get args 0 3000)})

(defn start-server [app options]
  (jetty/run-jetty app {:port (Integer. (:port options))}))

(defn -main [& args]
  (start-server app (args-to-options args)))

(defn -dev-main [& args]
  (start-server (wrap-reload #'app) (args-to-options args)))
