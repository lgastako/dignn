(ns dignn.core-test
  (:refer-clojure :exclude [find]) ; suppress the shadowing warning
  (:require [clojure.core :as core])
  (:require [clojure.test :refer :all]
            [dignn.core :refer :all]
            [its.log :as log]))

;; Note: one consequence of implementing sum and product this way is that they behave like
;; apply in that if the last item is a list it's expanded.

(deftest test-pow
  (is (= 4.0 (pow 2 2)))
  (is (= 8.0 (pow 2 3)))
  (is (= 9.0 (pow 3 2)))
  (is (= 27.0 (pow 3 3)))
  (is (= 5993375.773053164 (pow 9.6 6.9))))

(deftest test-exp
  (is (= 148.4131591025766 (exp 5))))

(deftest test-nand-perceptron []
  (is (= (evaluate nand [0 0])
         1))
  (is (= (evaluate nand [1 0])
         1))
  (is (= (evaluate nand [0 1])
         1))
  (is (= (evaluate nand [1 1])
         0)))

(deftest test-find-of-missing-values [])
  (let [network (make-network adder-config)]
    (is (= (find network [:input :missing]) nil))
    (is (= (find network [:node :missing]) nil))
    (is (= (find network [:output :missing]) nil)))

(deftest test-find-of-cached-values []
  (let [network (make-network adder-config)]
    (create! network [:input :mock-input] :mock-input-val)
    (create! network [:node :mock-node] :mock-node-val)
    (create! network [:output :mock-output] :mock-output-val)
    (is (= (find network [:input :mock-input]) :mock-input-val))
    (is (= (find network [:node :mock-node]) :mock-node-val))
    (is (= (find network [:output :mock-output]) :mock-output-val))))

(deftest test-something []
  (is (= (execute (atom adder-network) [0 0])
         {:sum 0 :carry 0}))
  (is (= (execute adder-network [1 0])
         {:sum 1 :carry 0}))
  (is (= (execute adder-network [0 1])
         {:sum 1 :carry 0}))
  (is (= (execute adder-network [1 1])
         {:sum 0 :carry 1})))


(run-tests)
