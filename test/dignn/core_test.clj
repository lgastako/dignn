(ns dignn.core-test
  (:refer-clojure :exclude [find]) ; suppress the shadowing warning
  (:require [clojure.core :as core])
  (:require [clojure.test :refer :all]
            [dignn.core :refer :all]))

;; Note: one consequence of implementing sum and product this way is that they behave like
;; apply in that if the last item is a list it's expanded.

;; (deftest test-sum
;;   (is (= 0 (sum [0 1]))))

;; (deftest test-product
;;   (is (= 0 (product 0))))


(deftest test-nand-perceptron []
  (is (= (execute-perceptron nand {:a 0 :b 0})
         1))
  (is (= (execute-perceptron nand {:a 1 :b 0})
         1))
  (is (= (execute-perceptron nand {:a 0 :b 1})
         1))
  (is (= (execute-perceptron nand {:a 1 :b 1})
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


  ;; (is (= (execute (atom adder-network) {:x1 0 :x2 0})
  ;;        {:sum 0 :carry 0}))
  ;; (is (= (execute adder-network {:x1 1 :x2 0})
  ;;        {:sum 1 :carry 0}))
  ;; (is (= (execute adder-network {:x1 0 :x2 1})
  ;;        {:sum 1 :carry 0}))
  ;; (is (= (execute adder-network {:x1 1 :x2 1})
  ;;        {:sum 0 :carry 1}))


(run-tests)

