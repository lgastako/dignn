(ns dignn.mnist_test
  (:require [clojure.core :as core])
  (:require [clojure.java.io :refer [input-stream]]
            [clojure.test :refer :all]
            [dignn.mnist :refer :all]
            [dignn.xlib :refer [slurpb]]
            [its.log :as log]
            [org.clojars.smee.binary.core :refer :all]))

(def label-fn-gz "resources/mnist/train-labels-idx1-ubyte.gz")
(def label-fn "resources/mnist/train-labels-idx1-ubyte")

(deftest test-read-label-src
  (doall
   (for [fn [label-fn
             label-fn-gz]]
     (let [results (read-label-src fn)]
       (is (= 2049 (:magic-number results)))
       (is (= 60000 (:num-items results)))
       (is (= 60000 (count (:labels results))))))))


;;(run-tests)


