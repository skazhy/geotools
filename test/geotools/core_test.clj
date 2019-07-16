(ns geotools.core-test
  (:require [clojure.test :refer :all]
            [geotools.core :refer :all]))

(defn- =ish [this that]
  (if (vector? this)
    (every? true? (map =ish this that))
    (< (Math/abs (- this that)) 0.00001)))

(deftest to-dms-test
  (is (=ish  (to-dms [56 56 52.7352]) [56 56 52.7352]))
  (is (=ish (to-dms [56 56.87892]) [56 56 52.7352]))
  (is (=ish (to-dms 56.947982) [56 56 52.7352]))

  (is (=ish (to-dms [-33 59 16.0044]) [-33 59 16.0044]))
  (is (=ish (to-dms [-33 59.26674]) [-33 59 16.0044]))
  (is (=ish (to-dms -33.987779) [-33 59 16.0044])))

(deftest to-ddm-test
  (is (=ish (to-ddm [56 56 52.7352]) [56 56.87892]))
  (is (=ish (to-ddm [56 56.87892])  [56 56.87892]))
  (is (=ish (to-ddm 56.947982) [56 56.87892]))

  (is (=ish (to-ddm [-33 59 16.0044]) [-33 59.26674]))
  (is (=ish (to-ddm [-33 59.26674]) [-33 59.26674]))
  (is (=ish (to-ddm -33.987779) [-33 59.26674])))

(deftest to-decimal-test
  (is (=ish (to-decimal [56 56 52.7352]) 56.947982))
  (is (=ish (to-decimal [56 56.87892])  56.947982))
  (is (=ish (to-decimal 56.947982) 56.947982))

  (is (=ish (to-decimal [-33 59 16.0044]) -33.987779))
  (is (=ish (to-decimal [-33 59.26674]) -33.987779))
  (is (=ish (to-decimal -33.987779) -33.987779)))

(deftest distance-test
  ; Distance between two airfields, just under 60 km.
  (is (=ish (distance {:lat 57.099466 :lon  24.264608}
                      {:lat 56.671059 :lon 23.690155})
            59050.6689)))
