(ns geotools.parse-test
  (:require [clojure.test :refer :all]
            [geotools.parse.core :refer :all]))

(deftest string-parsing
  (testing "Google Maps formats"
    (is (= (parse-string "56°55'25.5\"N 24°05'52.9\"E")
           {:lat [56 55 25.5] :lon [24 5 52.9]}))

    (is (= (parse-string "56.921758, 24.147614")
           {:lat 56.921758 :lon 24.147614}))

    (is (= (parse-string "40°49'50.9\"N 73°47'48.0\"W")
           {:lat [40 49 50.9] :lon [-73 47 48.0]}))

    (is (= (parse-string "-27.116790, -109.369032")
           {:lat -27.116790 :lon -109.369032})))

  (testing "Wikipedia formats"
    (is (= (parse-string "54.652°N 24.934°E") {:lat 54.652 :lon 24.934}))

    (is (= (parse-string "37°14′0″N 115°48′30″W")
           {:lat [37 14 0.0] :lon [-115 48 30.0]}))

    (is (= (parse-string "40°38′23″N 073°46′44″W")
           {:lat [40 38 23.0] :lon [-73 46 44.0]})))

  (testing "Reverse GeoCheck parsing"
    (is (= (parse-string "N56 55.008 E024 05.696")
           {:lat [56 55.008] :lon [24 5.696]}))

    (is (= (parse-string "N54 04.550 E029 18.096")
           {:lat [54 4.550] :lon [29 18.096]}))

    (is (= (parse-string "S12 30.719 W053 48.458")
           {:lat [-12 30.719] :lon [-53 48.458]})))

  (testing "Geocaching format"
    (is (= (parse-string "N 56° 56.488 E 024° 06.500")
           {:lat [56 56.488] :lon [24 6.5]}))))
