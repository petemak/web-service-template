(ns web-service.db
  (:require [datomic.api :as d]))


;;-----------------------------------------------
;; Task schema: describes atribiutes that make
;; up a task.
;;-----------------------------------------------
(def task-schema [{:db/ident :task/name
                   :db/valueType :db.type/string
                   :db/cardinality :db.cardinality/one
                   :db/doc "The name of the task"}

                  {:db/ident :task/tid
                   :db/valueType :db.type/string
                   :db/cardinality :db.cardinality/one
                   :db/doc "Unique task identifier"}
                  
                  {:db/ident :task/description
                   :db/valueType :db.type/string
                   :db/cardinality :db.cardinality/one
                   :db/doc "The task description"}

                  {:db/ident :task/creation-date
                   :db/valueType :db.type/instant
                   :db/cardinality :db.cardinality/one
                   :db/doc "The date on which the task was created"}
                  
                  {:db/ident :task/status
                   :db/valueType :db.type/string
                   :db/cardinality :db.cardinality/one
                   :db/doc "The status of a task"}])


;;-----------------------------------------------
;; Datalog query for all tasks
;;-----------------------------------------------
(def all-tasks-querry
  '[:find ?e ?tid ?name ?descr ?crd
    :where [?e :task/tid ?tid]
           [?e :task/name ?name]
           [?e :task/description ?descr]
           [?e :task/creation-date ?crd]])

;;-----------------------------------------------
;; Utililty functions
;;-----------------------------------------------
(defn unique-id!
  "Generate a unique identifier"
  []
  (.toString (java.util.UUID/randomUUID)))

;; ----------------------------------------------
;; create-databas: Creates database specified by uri.
;;                 Returns true if the database was
;;                 created, false if exists.
;;
;; connect: Connects to the specified database,
;;          returing a Connection. 
(defn connect
  "Connect to the data base URI
   and returns the connection"
  [uri]
  (if (d/create-database uri)
    (d/connect uri)))

;; ----------------------------------------------
;; Transact: Submits a transaction to the database
;; for writing.
;;
;; ----------------------------------------------
(defn init-db
  "Connect to the database specified by the uri
   and transacts the schema"
  [uri]
  (let [conn (connect uri)]
    {:conn conn
     :db (d/transact conn task-schema)}))


;; ----------------------------------------------
;; This method should be called as
;; part of clean shutdown of a JVM process.
;;
;; ----------------------------------------------
(defn shutdown
  "Shut down database resources"
  []
  (d/shutdown false))


;; ----------------------------------------------
;; Save a task. 
;; - name and description must be provided
;; - id and creation date will be generated
;; ----------------------------------------------
(defn save-task
  "Save task with specified name n, description d,
   and s status"
  [{:keys [conn name description]}]
  (let [data [{:task/tid (unique-id!)
               :task/name name
               :task/description description
               :task/creation-date (java.util.Date.)
               :task/status "open"}]] 
    (:tx-data (d/transact conn data))))


;; ----------------------------------------------
;; Rerieve all tasks from the databse
;; ----------------------------------------------
(defn load-tasks
  "Use the connection ro retrieve all tasks from
  te database"
  [{:keys [conn]}]
  (d/q all-tasks-querry (d/db conn)))
