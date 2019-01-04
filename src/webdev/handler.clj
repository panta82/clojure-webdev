(ns webdev.handler
  (:require
   [webdev.model :refer [create-item set-item-checked delete-item list-items]]
   [webdev.view :refer [items-page]]))

(defn handle-index-items [req]
  (let [db (:webdev/db req)
        items (list-items db)]
    {:status 200
     :body (items-page items)}))

(defn handle-create-item [req]
  (let [db (:webdev/db req)
        payload (:form-params req)
        created-id (create-item db (get payload "name") (get payload "description"))]
    {:status 302
     :headers {"Location" (str "/items")}}))