(ns geotools.format
  (:require [geotools.core :refer [to-ddm]]))

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
