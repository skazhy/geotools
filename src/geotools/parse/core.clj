(ns geotools.parse.core
  (:require [clojure.string :as str]))

(def ^:private multipliers {"N" 1 "S" -1 "E" 1 "W" -1})

(defn- parse-coordinate [multiplier [d m? s?]]
  (cond
    s? [(* multiplier (Integer. d)) (Integer. m?) (Double. s?)]
    m? [(* multiplier (Integer. d)) (Double. m?)]
    :else (* multiplier (Double. d))))

(defn parse-string
  "Attempts to parse given lat/lon coordinate string. Returns a lat lon
   map with the same cordinate format that was provided in input on success."
  [raw-str]
  (let [multipliers (->> (re-matches #".*([NS]).*([EW]).*" raw-str) rest
                         (map multipliers))
        split-str (-> (str/replace raw-str #"[A-Z]" "")
                      (str/trim)
                      (str/split #"[\s\"\'°′″,]+"))]
    (->> (split-at (/ (count split-str) 2) split-str)
         (map parse-coordinate (or (seq multipliers) [1 1]))
         (interleave [:lat :lon])
         (apply hash-map))))
