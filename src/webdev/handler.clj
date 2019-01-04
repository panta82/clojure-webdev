(ns webdev.handler
  (:require
   [webdev.model :refer [create-item set-item-checked delete-item list-items]]))

(defn handle-index-items [req]
  (let [db (:webdev/db req)
        items (list-items db)]
    {:status 200
     :body (str
            "<html><body>"
            (mapv :name items)
            "<form method=\"POST\" action=\"/items\">
            <input type=text name=name placeholder=name />
            <input type=text name=description placeholder=description />
            <input type=submit />
            </form>"
            "</body></html")}))

(defn handle-create-item [req]
  (let [db (:webdev/db req)
        payload (:form-params req)
        created-id (create-item db (get payload "name") (get payload "description"))]
    {:status 302
     :headers {"Location" (str "/items")}}))