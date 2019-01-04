(ns webdev.model
  (:require [clojure.java.jdbc :as jdbc]))

(defn create-tables [db]
  (jdbc/execute! db ["CREATE EXTENSION IF NOT EXISTS \"uuid-ossp\""])
  (jdbc/execute! db ["CREATE TABLE IF NOT EXISTS items (
      id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
      name TEXT NOT NULL,
      description TEXT NOT NULL,
      checked BOOLEAN NOT NULL DEFAULT FALSE,
      date_created TIMESTAMPTZ NOT NULL DEFAULT now()
    )"]))

(defn create-item [db name description]
  (let [res (jdbc/query db ["INSERT INTO items(name, description)
      VALUES (?, ?)
      RETURNING id" name description])]
    (:id (first res))))

(defn set-item-checked [db id checked]
  (let [res (jdbc/execute! db ["UPDATE items SET checked = ? WHERE id = ?" checked id])]
    (= [1] res)))

(defn delete-item [db id]
  (let [res (jdbc/execute! db ["DELETE FROM items WHERE id = ?" id])]
    (= [1] res)))

(defn list-items [db]
  (jdbc/query db ["SELECT * FROM items ORDER BY date_created ASC"]))