(ns geotools.parse.lgia
  (:require [clojure.string :as str]
            [geotools.parse.core :refer [parse-string]]))

;;; lgia.gov.lv (Latvian Geospatial Information Agency)
;;;; exports files to XLS files (that look XML-y),
;;; but all "formal" XLS parsers fail to digest them.

(def ^:private fields
  [:name :object-id :type :state :official-name :other-names :unit :lat :lon])

(defn parse-coordinates [lgia-map]
  (let [coordinates (parse-string (str (:lat lgia-map) " " (:lon lgia-map)))]
    (-> (assoc lgia-map :coordinates coordinates)
        (update :other-names #(when % (str/split % #"; ")))
        (dissoc :lat :lon))))

(defn parse-lgia-block [rows]
  ; dumb xml row splitting to extract the nested value
  (->> (map #(str/trim (nth (str/split % #">|<") 4)) rows)
       (map not-empty)
       (interleave fields)
       (apply hash-map)
       (parse-coordinates)))

(defn parse-lgia [path]
  (loop [full []
         current []
         rows (str/split (slurp path :encoding "windows-1257") #"\n")]
    (if-let [f (first rows)]
      (cond
        (.contains f "<Row") (recur full [] (rest rows))
        (.contains f "</Row") (recur (conj full current) [] (rest rows))
        (.contains f "<Cell ") (recur full (conj current f) (rest rows))
        :else (recur full current (rest rows)))
      (->> (filter #(= 9 (count %)) full)
           (rest)
           (map parse-lgia-block)))))
