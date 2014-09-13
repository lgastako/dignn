(ns dignn.core
  (:require [clojure.test :refer [is]]))

(declare find-or-create!)

;; a perceptron
(def nand
  {:inputs {:a -2
            :b -2}
   :bias 3})

;; a network configuration
(def adder-config
  {:inputs [:x1 :x2]
   :nodes {:node1 {:perceptron nand
                   :inputs {:a {:input :x1}
                            :b {:node :node2}}}
           :node2 {:perceptron nand
                   :inputs {:a {:input :x1}
                            :b {:input :x2}}}
           :node3 {:perceptron nand
                   :inputs {:a {:node :node1}
                            :b {:node :node4}}}
           :node4 {:perceptron nand
                   :inputs {:a {:node :node2}
                            :b {:input :x2}}}
           :node5 {:perceptron nand
                   :inputs {:a {:node :node2}
                            :b {:node :node2}}}}
   :outputs {:sum {:node :node3}
             :carry {:node :node5}}})

(defn make-network [config]
  (atom {:config config}))

(defn output-names [network]
  (keys (get-in @network [:config :ouputs])))

(defn find [network [target-type target]]
  (get-in @network [:values target-type target]))

(defn create! [network [target-type target] value]
  (swap! network assoc-in [:values target-type target] value))

(def product (partial apply *))

(defn execute-perceptron [perceptron inputs]
  (let [w 1.0
        x (product (vals inputs))
        b (:bias perceptron)]
    (if (+ (* w x) b)
      0
      1)))

(defn calc-output! [network target inputs]
  (let [src (get-in @network [:outputs (second target)])]
    (find-or-create! network src inputs)))

(defn perceptron-of [network node]
  (get-in @network [:nodes node :perceptron]))

(defn calc-node! [network target inputs]
  (or (find network target)
      (execute-perceptron (perceptron-of network (second target)))))

(defn calc-input! [network target inputs] (get inputs (second target)))

(defn calc! [network target inputs]
  (let [target-type (first target)
        f (target-type {:output calc-output!
                        :node calc-node!
                        :input calc-input!})]
    (f network target inputs)))

(defn find-or-create! [network target inputs]
  "First attempts to find a cached value of the target in the network.  If the
   value is not already cached then calculates it and adds it to the cache."
  (or (find network target)
      (let [value (calc! network target inputs)]
        (create! network target value)
        (find network target))))

(defn execute [network inputs]
  "Evaluate the network on the given inputs.  Operates by find-or-create!'ing
   all of the outputs and recursively fulfilling their inputs."
  (-> network
      :outputs
      keys
      (->> (map #(-> [:output %]))
           (map #(find-or-create! network inputs %))
           (into {}))))

(def adder-network (make-network adder-config))

(defn demo []
  (is (= (execute adder-network {:x1 0 :x2 0})
         {:sum 0 :carry 0}))
  (is (= (execute adder-network {:x1 1 :x2 0})
         {:sum 1 :carry 0}))
  (is (= (execute adder-network {:x1 0 :x2 1})
         {:sum 1 :carry 0}))
  (is (= (execute adder-network {:x1 1 :x2 1})
         {:sum 0 :carry 1})))

(demo)
