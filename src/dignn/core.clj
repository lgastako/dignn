(ns dignn.core
  (:refer-clojure :exclude [find * - + / ==])
  (:require [clojure.core.matrix.operators :refer :all]
            [clojure.math.numeric-tower :as math]
            [dignn.mnist-data :refer :all]
            [incanter.distributions :as dist]
            [its.log :as log]))

(log/set-level! :debug)

(defn draw-py-dist [] (dist/draw (dist/normal-distribution 0 1)))

(def py-stream (repeatedly draw-py-dist))

(defn feed-forward
  "`layers` is a vector of layers of weights where the first layer are treated
    as the inputs and merged down through the rest of the layers to get the
    final output vector."
  [neuron & layers]
  (log/debug :eval-nn {:neuron neuron :layers layers})
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
        (apply feed-forward neuron (cons next-layer remaining))))))

(defn perceptron [threshold weights inputs]
  (if (< (* weights inputs) threshold)
    0
    1))

(defn sigmoid [bias weights inputs]
  (/ 1 (+ 1 (math/expt (* -1 (- (* weights inputs) bias))))))

(defn run-network [network-eval-f neuron weights biases inputs]
  (let [layers (vec (cons inputs (* weights biases)))]
    (network-eval-f neuron layers)))

;; We omit the first layer which are inputs and thus have no biases (or biases
;; of 1, however you prefer to think of it).
(defn run-dignn [inputs]
  (let [sizes [784 100 10]
        num-layers (count sizes)
        biases (map (fn [size] (take size py-stream))
                    (rest sizes))
        weights (vec (for [[x y] (map vector
                                      (butlast sizes)
                                      (rest sizes))]
                       (vec (repeatedly y (fn [& _] (take x (repeatedly draw-py-dist)))))))]
    (assert (= 3 num-layers))
    (assert (= (count weights)
               (count biases)))
    (assert (= [100 10] (map count biases)))
    (assert (= [100 10] (map count weights)))
    (log/debug :mcw (map count weights))
    (let [layers (vec (cons inputs (* weights biases)))]
      (run-network feed-forward
                   sigmoid))))

;; (defn run-adder [inputs]
;;   (let [weights [-2 -2]
;;         biases 3]  ;; does this get broadcast properly?
;;     (run-network feed-forward sigmoid weights biases inputs)))
