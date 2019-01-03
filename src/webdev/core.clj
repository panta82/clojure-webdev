(ns webdev.core
  (:require [ring.adapter.jetty :as jetty]
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
  {:port (nth args 0 3000)})

(defn start-server [app options]
  (jetty/run-jetty app {:port (Integer. (:port options))}))

(defn -main [& args]
  (do [(println "Running in PRODUCTION mode")
       (start-server app (args-to-options args))]))