(ns dignn.core
  (:refer-clojure :exclude [find]) ; suppress the shadowing warning
  (:require [clojure.test :refer [deftest is run-tests]]
            [its.log :as log]))

(log/set-level! :off)

(declare find-or-create!)

(defn pow [base exponent]
  (Math/pow base exponent))

(defn exp [x]
  (Math/exp x))

;; Types of neurons

(defn gencalc [neuron inputs]
  (log/debug :perceptron {:neuron neuron :inputs inputs})
  (let [inks (keys (:inputs neuron))
        weights (:inputs neuron)
        exvec (apply juxt inks)
        w (exvec weights)
        x (exvec inputs)
        wx (map * w x)
        b (:bias neuron)]
    {:w w
     :x x
     :wx wx
     :b b}))

(defn perceptron [neuron inputs]
  (log/debug :perceptron {:neuron neuron :inputs inputs})
  (let [{wx :wx b :b} (gencalc neuron inputs)
        val (apply + b wx)]
    (if (<= val 0)
      0
      1)))

(defn sigmoid [neuron inputs]
  (log/debug :sigmoid {:neuron neuron :inputs inputs})
  (let [{wx :wx b :b} (gencalc neuron inputs)
        sumwx (apply + wx)]
    (/ 1
       (+ 1 (exp (* -1 (- sumwx b)))))))

;; Example neurons

;; a perceptron
(def nand
  {:calc perceptron
   :inputs {:a -2
            :b -2}
   :bias 3})


;; a network configuration
(def adder-config
  {:inputs [:x1 :x2]
   :nodes {:node1 {:neuron nand
                   :inputs {:a {:input :x1}
                            :b {:node :node2}}}
           :node2 {:neuron nand
                   :inputs {:a {:input :x1}
                            :b {:input :x2}}}
           :node3 {:neuron nand
                   :inputs {:a {:node :node1}
                            :b {:node :node4}}}
           :node4 {:neuron nand
                   :inputs {:a {:node :node2}
                            :b {:input :x2}}}
           :node5 {:neuron nand
                   :inputs {:a {:node :node2}
                            :b {:node :node2}}}}
   :outputs {:sum {:node :node3}
             :carry {:node :node5}}})

(defn make-network [config]                              (atom {:config config}))
(defn output-names [network]                             (keys (get-in @network [:config :ouputs])))
(defn find         [network [target-type target]]        (get-in @network [:values target-type target]))
(defn create!      [network [target-type target] value]  (swap! network assoc-in [:values target-type target] value))

(defn execute-neuron [neuron inputs]
  (log/debug :execute-neuron {:neuron neuron :inputs inputs})
  (let [calc (:calc neuron)]
    (calc neuron inputs)))

(defn calc-output! [network target inputs]
  (let [src (get-in @network [:outputs (second target)])]
    (find-or-create! network src inputs)))

(defn perceptron-of [network node]
  (get-in @network [:nodes node :perceptron]))

(defn calc-node! [network target inputs]
  (or (find network target)
      (execute-neuron (perceptron-of network (second target)))))

(defn calc-input! [network target inputs]
  (get inputs (second target)))

(defn calc! [network target inputs]
  (let [target-type (first target)
        f (target-type {:output calc-output!
                        :node calc-node!
                        :input calc-input!})]
    (f network target inputs)))

(defn find-or-create! [network inputs target]
  "First attempts to find a cached value of the target in the network.  If the
   value is not already cached then calculates it and adds it to the cache."
  (or (find network target)
      (let [value (calc! network target inputs)]
        (create! network target value)
        (find network target))))

(defn execute [network inputs]
  "Evaluate the network on the given inputs.  Operates by find-or-create!'ing
   all of the outputs and recursively fulfilling their inputs."
  (->> (output-names network)
       (map #(-> [:output %]))
       (map #(find-or-create! network inputs %))
       (into {})))

(def adder-network (make-network adder-config))

(defn system []
  {:inputs [{:a 0 :b 0}
            {:a 1 :b 0}
            {:a 0 :b 1}
            {:a 1 :b 1}]
   :network adder-config})

(def start identity)
(def stop (constantly nil))

(defn execute [inputs network]
  (doall (for [input inputs]
           (let [network (make-network network)]
             (execute network input)
             (log/debug :input input :network-after @network)))))

(defn -main []
  ;; TODO: docopt
  (let [{inputs :inputs network :network} (system)]
    (execute inputs network)))
