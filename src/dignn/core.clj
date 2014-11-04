(ns dignn.core
  (:refer-clojure :exclude [find * - + / ==])
  (:require [clojure.core.matrix.operators :refer :all]
            [clojure.test :refer [deftest is run-tests]]
            [its.log :as log]))

(log/set-level! :debug)

(declare find-or-create!)

(defn pow [base exponent]
  (Math/pow base exponent))

(defn exp [x]
  (Math/exp x))

;; Types of neurons

(defn wx [w b]
  (reduce + (* w b)))

(defn sum [xs] (apply +  xs))

(defprotocol INeuron "Neurons"
  (evaluate [this inputs] "Evaluate the neuron on the given inputs."))

(defrecord PerceptronNeuron [w b]
  INeuron
  (evaluate [_ inputs]
    (log/debug :perceptron {:inputs inputs :w w :b b})
    ;; Perceptrons that have a single bias but I think the way core.matrix's
    ;; broadcasting works that should just work anyway.
    (let [val (+ (sum (* w inputs)) b)]
      (if (<= val 0)
        0
        1))))

(defrecord SigmoidNeuron [w b]
  INeuron
  (evaluate [_ inputs]
    (log/debug :sigmoid {:inputs inputs :w w :b b})
    (let [val (wx w b)]
      (/ 1 (+ 1 (exp (* -1 (- val b))))))))

;; ;; Example neurons

(def nand (->PerceptronNeuron [-2 -2] 3))

;; an example manual network configuration
(def adder-config
  {:inputs 2
   :nodes [{:neuron nand
            :inputs [{:input 0}
                     {:node 1}]}
           {:neuron nand
            :inputs [{:input 0}
                     {:input 1}]}
           {:neuron nand
            :inputs [{:node 0}
                     {:node 3}]}
           {:neuron nand
            :inputs [{:node 1}
                     {:input 1}]}
           {:neuron nand
            :inputs [{:node 1}
                     {:node 1}]}]
   :outputs {:sum {:node 2}
             :carry {:node 4}}})

(defn make-network [config]
  (atom {:config config}))

(defn output-names [network]
  (keys (get-in @network [:config :ouputs])))

(defn find [network [target-type target]]
  (get-in @network [:values target-type target]))

(defn create! [network [target-type target] value]
  (swap! network assoc-in [:values target-type target] value))

;; (defn calc-output! [network target inputs]
;;   (let [src (get-in @network [:outputs (second target)])]
;;     (find-or-create! network src inputs)))

;; (defn perceptron-of [network node]
;;   (get-in @network [:nodes node :perceptron]))

;; (defn calc-node! [network target inputs]
;;   (or (find network target)
;;       (execute-neuron (perceptron-of network (second target)))))

;; (defn calc-input! [network target inputs]
;;   (get inputs (second target)))

;; (defn calc! [network target inputs]
;;   (let [target-type (first target)
;;         f (target-type {:output calc-output!
;;                         :node calc-node!
;;                         :input calc-input!})]
;;     (f network target inputs)))

;; (defn find-or-create! [network inputs target]
;;   "First attempts to find a cached value of the target in the network.  If the
;;    value is not already cached then calculates it and adds it to the cache."
;;   (or (find network target)
;;       (let [value (calc! network target inputs)]
;;         (create! network target value)
;;         (find network target))))

(defn execute [network inputs]
  "Evaluate the network on the given inputs.  Operates by find-or-create!'ing
   all of the outputs and recursively fulfilling their inputs."
  (->> (output-names network)
       (map #(-> [:output %]))
       (map #(find-or-create! network inputs %))
       (into {})))

(def adder-network (make-network adder-config))

(defn system []
  {:inputs [[0 0]
            [1 0]
            [0 1]
            [1 1]]
   :network adder-config})

(def start identity)
(def stop (constantly nil))

;; (defn execute [inputs network]
;;   (doall (for [input inputs]
;;            (let [network (make-network network)]
;;              (execute network input)
;;              (log/debug :input input :network-after @network)))))

;; (defn -main []
;;   ;; TODO: docopt
;;   (let [{inputs :inputs network :network} (system)]
;;     (execute inputs network)))
