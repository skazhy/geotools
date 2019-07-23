(ns geotools.format
  (:require [clojure.string :as str]
            [geotools.core :refer [to-ddm to-decimal]]))

(defn format-geocheck
  "Formats given {:lat XXX :lon XXX} map for GeoCheck.org validation."
  [{:keys [lat lon]}]
  (let [[lat-deg lat-min] (to-ddm lat)
        [lon-deg lon-min] (to-ddm lon)]
    (str (if (pos? lat-deg) "N" "S")
         (format  "%02d " (Math/abs lat-deg))
         (format "%06.3f " lat-min)
         (if (pos? lat-deg) "E" "W")
         (format "%03d " (Math/abs lon-deg))
         (format "%06.3f" lon-min))))

(defn format-decimal [{:keys [lat lon]}]
  (str (to-decimal lat) "," (to-decimal lon)))

;;; Parsing

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
