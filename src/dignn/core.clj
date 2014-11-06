(ns dignn.core
  (:refer-clojure :exclude [find * - + / ==])
  (:require [clojure.core.matrix.operators :refer :all]
            [dignn.mnist-data :refer :all]
            [incanter.distributions :as dist]
            [its.log :as log]))

(log/set-level! :debug)

(def py-equiv-rand-dist (repeatedly #(dist/draw (dist/normal-distribution 0 1))))

(def sizes [784 100 10])

(def num-layers (count sizes))
(assert (= 3 num-layers))

(def biases
  (map (fn [size] (take size py-equiv-rand-dist))
       (rest sizes)))

(assert (= [100 10] (map count biases)))

(defn draw-py-dist []
  (dist/draw (dist/normal-distribution 0 1)))

(def weights
  (vec (for [[x y] (map vector (butlast sizes)
                           (rest sizes))]
         (vec (repeatedly y (fn [& _] (take x (repeatedly draw-py-dist))))))))

(assert (= (count weights)
           (count biases)))

(log/debug :mcw (map count weights))
(assert (= [100 10] (map count weights)))


(defn feed-forward
  "`layers` is a vector of layers of weights where the first layer are treated
    as the inputs and merged down through the rest of the layers to get the
    final output vector."
  [& layers]
  (log/debug :eval-nn {:layers layers})
  (assert (> (count layers) 0))
  (let [inputs (first layers)
        layers (rest layers)]
    (log/debug :eval-nn-let {:inputs inputs
                             :layers layers
                             :count-layers (count layers)})
    (if (= 0 (count layers))
      inputs
      (let [next-layer (* inputs (first layers))
            remaining (rest layers)]
        (log/debug :eval-nn {:next-layer next-layer
                             :remaining remaining})
        (apply feed-forward (cons next-layer remaining))))))
