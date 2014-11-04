(ns dignn.costs)

;; where E = sigma sum

;; C(w, b) = 1/2n * (E(x) || y(x) - a ||^2
;; w = weights
;; b = biases
;; n = number of training samples
;; x = training sample input
;; y(x) = desired output vector when input is x
;; a is the actual output vector when input is x
;; ||v|| = length of vector v


(defn quadratic
  "Quadratic cost function (or MSE).
   w = weights
   b = biases
   xs = training inputs
   ys = training outputs"
  [w b xs ys]
  (let [n (count xs)]
    (assert (= (count xs)
               (count ys)))
    (* (/ 1 (* 2 n))
       ())))



