(ns webdev.dev
  (:require
   [webdev.core :refer [start load-settings]]))

(defn -main [& args]
  (start (load-settings args true)))
