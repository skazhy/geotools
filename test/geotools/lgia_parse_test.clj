(ns geotools.lgia-parse-test
  (:require [clojure.test :refer :all]
            [geotools.parse.lgia :refer :all]))

(def rows
  ["<Cell ss:StyleID=\"DatiTopo\"><Data ss:Type=\"String\">Durbes purvs </Data></Cell>"
   "<Cell ss:StyleID=\"Dati\"><Data ss:Type=\"String\">84028 </Data></Cell>"
   "<Cell ss:StyleID=\"Dati\"><Data ss:Type=\"String\">purvs </Data></Cell>"
   "<Cell ss:StyleID=\"Dati\"><Data ss:Type=\"String\">pastav </Data></Cell>"
   "<Cell ss:StyleID=\"Dati\"><Data ss:Type=\"String\"> </Data></Cell>"
   "<Cell ss:StyleID=\"DatiTopo\"><Data ss:Type=\"String\"> </Data></Cell>"
   "<Cell ss:StyleID=\"Dati\"><Data ss:Type=\"String\">Durbe, Durbes novads </Data></Cell>"
   "<Cell ss:StyleID=\"Dati\"><Data ss:Type=\"String\">56° 34' 32\" </Data></Cell>"
   "<Cell ss:StyleID=\"Dati\"><Data ss:Type=\"String\">21° 21' 41\" </Data></Cell>"])

(deftest lgia-block-parse-test
  (is (= (parse-lgia-block rows)
         {:name "Durbes purvs"
          :object-id "84028"
          :type "purvs"
          :state "pastav"
          :official-name nil
          :other-names nil
          :unit "Durbe, Durbes novads"
          :coordinates {:lon [21 21 41.0] :lat [56 34 32.0]}})))
