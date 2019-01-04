(defproject webdev "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [ring "1.7.1"]
                 [compojure "1.6.1"]
                 [org.clojure/data.json "0.2.6"]
                 [org.clojure/java.jdbc "0.7.8"]
                 [org.postgresql/postgresql "42.2.5"]
                 [hiccup "1.0.5"]]
  :main webdev.core
  :min-lein-version "2.0.0"
  :uberjar-name "webdev.jar"
  :profiles {:dev
             {:source-paths ["src" "dev"]
              :main webdev.dev}})
