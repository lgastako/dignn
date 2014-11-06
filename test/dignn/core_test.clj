(ns dignn.core-test
  (:refer-clojure :exclude [find]) ; suppress the shadowing warning
  (:require [clojure.core :as core])
  (:require [clojure.test :refer :all]
            [dignn.core :refer :all]
            [its.log :as log]))

;; (deftest test-something []
;;   (is (= (run-adder [0 0])
;;          [0 0]))
;;   ;; (is (= (run-adder [1 0])
;;   ;;        [1 0]))
;;   ;; (is (= (run-adder [0 1])
;;   ;;        [1 0]))
;;   ;; (is (= (run-adder [1 1])
;;   ;;        [0 1]))
;;   )


(deftest test-run-dignn
  (log/debug :whee)
  (run-dignn (take 784 py-stream)))

(run-tests)
