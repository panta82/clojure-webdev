(ns webdev.dev
  (:require
   [ring.middleware.reload :refer [wrap-reload]]
   [webdev.core :refer [start-server app args-to-options]]))

(defn -main [& args]
  (do [(println "Running in DEV mode")
       (start-server (wrap-reload #'app) (args-to-options args))]))
