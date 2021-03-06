(ns intro.lines
 (:require [quil.core :as q]
           [quil.middleware :as m]
           [firstclassshapes.core :refer :all]))



(defn setup []
  (q/frame-rate 45)
  (q/color-mode :rgb)

  (let [line-length 50
        box-length 50]


  (def diag-line (create-line (dec line-length) (dec line-length) 3 255 100))

  (def inviz-block (create-rect box-length box-length)))

  (def block-line (above diag-line
                         inviz-block))

  (def total-width (:tw (first block-line)))

  (def row (reduce beside (repeat (/ (+ (q/width) 200) total-width) block-line)))

  (def total-height (:th (first row)))

  (def lines (reduce above (repeat (/ (+ (q/height) 200) total-height) row)))

  {:angle 0
   :angle2 0
   :dir "horiz"})

(defn mouse-clicked [state event]
  (if (= (:button event) :left)
    (assoc state :dir "horiz")
    (assoc state :dir "vert")))

;-

(defn update-state [state]
  (assoc state
    :angle (+ (:angle state) 0.02)
    :angle2 (- (:angle2 state) 0.02)))


(defn draw-horiz [state]
  (let [half-w (/ (q/width) 2)
        half-h (/ (q/height) 2)
        move-dist (:tw (first block-line))
        quarter-row (/ (:th (first row)) 4)]

    (ds lines (+ half-w (* (q/sin (:angle state)) move-dist)) (- half-h quarter-row))
    (ds lines (+ half-w (* (q/sin (:angle2 state)) move-dist)) (+ half-h quarter-row))))


(defn draw-vert [state]
  (let [half-w (/ (q/width) 2)
        half-h (/ (q/height) 2)
        move-dist (:tw (first block-line))
        quarter-row (/ (:th (first row)) 4)]

    (ds lines (- half-w quarter-row) (+ half-h (* (q/sin (:angle state)) move-dist)))
    (ds lines  (+ half-w quarter-row) (+ half-h (* (q/sin (:angle2 state)) move-dist)))))

;-
(defn draw-state [state]
  (f-background 25 25 25 25)
  (f-stroke 255 80)
  (if (= (:dir state) "horiz")
    (draw-horiz state)
    (draw-vert state)))

;-
(q/defsketch start
  :title "Moving Lines"
  :size [(- (min (q/screen-width) (q/screen-height)) 30) (- (min (q/screen-width) (q/screen-height)) 30)]
  :setup setup
  :update update-state
  :draw draw-state
  :mouse-clicked mouse-clicked
  :middleware [m/fun-mode])


