(ns webdev.core
  (:require [ring.adapter.jetty :as jetty]
            [ring.handler.dump :refer [handle-dump]]
            [compojure.core :refer [defroutes]]
            [compojure.route :as route]))

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

(defroutes app
  (GET "/" [] greet)
  (GET "/goodbye" [] goodbye)
  (GET "/about" [] about)
  (GET "/request" [] request)
  (GET "/yo/:name" [] greeting)
  (GET "/calc/:a/:op/:b" [] calc)
  (route/not-found "Not found"))

(defn args-to-options [args]
  {:port (nth args 0 3000)})

(defn start-server [app options]
  (jetty/run-jetty app {:port (Integer. (:port options))}))

(defn -main [& args]
  (do [(println "Running in PRODUCTION mode")
       (start-server app (args-to-options args))]))