(ns webdev.view
  (:require
   [hiccup.page :refer [html5]]
   [hiccup.core :refer [html h]]))

(defn form-group-horizontal [label id input]
  (html
   [:div.form-group.row
    [:label.col-form-label.col-sm-2
     {:for :id} label]
    [:div.col-sm-6
     input]]))

(defn form-text-input [label name placeholder]
  (html
   (form-group-horizontal
    label name
    [:input.form-control
     {:id name
      :name name
      :placeholder placeholder}])))

(defn new-item-form []
  (html
   [:form
    {:method "post"
     :action "/items"}
    (form-text-input "Name" :name "Name")
    (form-text-input "Description" :description "Description")
    [:div.row
     [:div.col-sm-10.offset-sm-2
      [:input.btn.btn-primary
       {:type "submit"
        :value "Create"}]]]]))

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
          [:th "Description"]
          [:th ""]]]
        [:tbody
         (for [i items]
           [:tr
            [:td (h (:name i))]
            [:td (h (:description i))]
            [:td
             [:form
              {:action (str "/items/" (:id i))
               :method "POST"}
              [:input {:type "hidden" :name "_method" :value "DELETE"}]
              [:button.btn.btn-danger.btn-sm {:type "submit" :title "Delete"} "✖"]]]])]]
       [:h5 "There are no items"])
     [:div
      [:h4 "New item"]
      (new-item-form)]]]
   [:script
    {:src "/jquery/jquery-3.3.1.min.js"}]
   [:script
    {:src "/bootstrap/js/bootstrap.min.js"}]))