(ns geotools.format-test
  (:require [clojure.test :refer :all]
            [geotools.format :refer :all]))

(deftest geocheck-formatting
  (is (= (format-geocheck {:lat 56.916805 :lon 24.094929})
         "N56 55.008 E024 05.696")
      "Longitude is 0-padded, N and E are correctly formatted")

  (is (= (format-geocheck {:lat 54.075833 :lon 29.301605})
         "N54 04.550 E029 18.096")
      "Latitude is 0-padded")

  (is (= (format-geocheck {:lat -12.511979 :lon -53.807635})
         "S12 30.719 W053 48.458")
      "Negative degrees are formatted as expected"))
