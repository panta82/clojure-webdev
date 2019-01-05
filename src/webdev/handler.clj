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

(defn handle-delete-item [req]
  (let [db (:webdev/db req)
        item-id (java.util.UUID/fromString (:item-id (:route-params req)))
        deleted (delete-item db item-id)]
    (if deleted
      {:status 302
       :headers {"Location" "/items"}}
      {:status 404
       :body "List item not found"})))

(defn handle-set-item-checked [req]
  (let [db (:webdev/db req)
        item-id (java.util.UUID/fromString (get-in req [:route-params :item-id]))
        checked (= "checked" (get-in req [:form-params "checked"]))
        updated (set-item-checked db item-id checked)]
    (if updated
      {:status 302
       :headers {"Location" "/items"}}
      {:status 404
       :body "List item not found"})))