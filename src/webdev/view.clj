(ns webdev.view
  (:require
   [hiccup.page :refer [html5]]
   [hiccup.core :refer [html h]]))

(defn items-page [items]
  (html5
   {:lang :en}
   [:head
    [:title "Clojure!"]
    [:meta
     {:name :viewport
      :content "width=device-width, initial-scale=1.0"}]
    [:link
     {:href "/bootstrap/css/bootstrap.min.css"
      :rel "stylesheet"}]]
   [:body
    [:div.container
     [:h1 "My items"]
     [:div.row]
     (if (seq items)
       [:table.table.table-stripped
        [:thead
         [:tr
          [:th "Name"]
          [:th "Description"]]]
        [:tbody
         (for [i items]
           [:tr
            [:td (h (:name i))]
            [:td (h (:description i))]])]])]]
   [:script
    {:src "/jquery/jquery-3.3.1.min.js"}]
   [:script
    {:src "/bootstrap/js/bootstrap.min.js"}]))