(ns dignn.costs)

(defn quadratic
  "Quadratic cost function (or MSE)."
  [w b]
  (let [n (comment "number of training instances")]
    (*
     (/ 1
        (* 2 n))
     (comment "sum "))))
