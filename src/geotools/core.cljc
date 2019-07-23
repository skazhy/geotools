(ns geotools.core)

(defn- square [x] (* x x))
(def ^:private EARTH-RADIUS 6371000)  ; in meters

;;; Unit conversion

;; negative lat - South
;; negative lon - West

;; Decimal: 56.951790, 24.134197 (sent in as-is)
;; DMS: 56°57'06.4"N 24°08'03.1"E (sent in as [[56 57 6.4] [24 8 3.1]])
;; DDM: 56°57.1074, 24°8.05182 (sent in as [[56 57.1074] [24 8.05182]])

(defn to-decimal
  "Transforms GPS coordinate to the decimal format."
  [in]
  (if (vector? in)
    (let [[d m s?] in]
      (if s?
        (* (if (pos? d) 1 -1) (+ (Math/abs d) (/ m 60) (/ s? 3600)))
        (* (if (pos? d) 1 -1) (+ (Math/abs d) (/ m 60)))))
    in))

(defn to-dms
  "Transforms GPS coordinate to DMS (degree, minute, second) format."
  [in]
  (if (vector? in)
    (let [[d m s?] in]
      (if s?
        in
        [d (int (Math/floor m)) (-> (* 60 m) (mod 60))]))
    [(-> (Math/abs in) Math/floor (* (Math/signum in)) int)
     (-> (Math/abs in) (* 60) Math/floor (mod 60) int)
     (-> (Math/abs in) (* 3600) (mod 60))]))

(defn to-ddm
  "Transforms GPS coordinate to DDM (degree, decimal minute) format."
  [in]
  (if (vector? in)
    (let [[d m s?] in]
      (if s?
        [d (+ m (/ s? 60))]
        in))
    (let [abs-deg (-> (Math/abs in) Math/floor)]
      [(int (* (Math/signum in) abs-deg)) (* (- (Math/abs in) abs-deg) 60)])))

(defn to-radians
  "Transforms GPS coordinate to radians."
  [in]
  (Math/toRadians (to-decimal in)))

;;; Distance calculations

; https://www.movable-type.co.uk/scripts/latlong.html
; https://en.wikipedia.org/wiki/Haversine_formula

(defn distance
  "input: {:lat 56.9575580 :lon 24.1574050} pair, output in meters."
  [from to]
  (let [a (+ (-> (Math/toRadians (- (:lat to) (:lat from))) (/ 2)
                 (Math/sin)
                 (square))
             (* (->  (Math/toRadians (- (:lon to) (:lon from))) (/ 2)
                    (Math/sin)
                    (square))
                (Math/cos (Math/toRadians (:lat from)))
                (Math/cos (Math/toRadians (:lat to)))))]
    (* EARTH-RADIUS 2 (Math/atan2 (Math/sqrt a) (Math/sqrt (- 1 a))))))

(defn bearing
  "input: {:lat 56.9575580 :lon 24.1574050} pair, output in degrees."
  [from to]
  (let [to-lat (to-radians (:lat to))
        from-lat (to-radians (:lat from))
        lon-delta (Math/toRadians (- (:lon to) (:lon from)))
        x  (* (Math/sin lon-delta)
              (Math/cos to-lat))
        y (- (* (Math/cos from-lat)
                (Math/sin to-lat))
             (* (Math/sin from-lat)
                (Math/cos to-lat)
                (Math/cos (Math/toRadians lon-delta))))]
    (Math/toDegrees (Math/atan2 x y))))

(defn destination-point
  "input {:lat 56.9575580 :lon 24.1574050}
   bearing in degrees, distance in meters."
  [from bearing distance]
  (let [from-lat (to-decimal (:lat from))
        from-lon (to-decimal (:lon from))
        angular-dist (/ distance EARTH-RADIUS)
        lat (Math/asin (+ (* (Math/sin (Math/toRadians from-lat))
                             (Math/cos angular-dist))
                          (* (Math/cos (Math/toRadians from-lat))
                             (Math/sin angular-dist)
                             (Math/cos (Math/toRadians bearing)))))]
    {:lat (Math/toDegrees lat)
     :lon (Math/toDegrees
            (+ (Math/toRadians from-lon)
               (Math/atan2 (* (Math/sin (Math/toRadians bearing))
                              (Math/sin angular-dist)
                              (Math/cos (Math/toRadians from-lat)))
                           (- (Math/cos angular-dist)
                              (* (Math/sin (Math/toRadians from-lat))
                                 (Math/sin lat))))))}))
