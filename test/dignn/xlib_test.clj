(ns dignn.xlib_test
  (:require [clojure.core :as core])
  (:require [clojure.test :refer :all]
            [dignn.xlib :refer :all]
            [its.log :as log]))

(deftest test-ifn
  (is (= 1 ((ifn even? inc) 1)))
  (is (= 3 ((ifn even? inc) 2))))

(deftest test slurpb
  (is (= "hello" (String. (slurpb (java.io.ByteArrayInputStream. (.getBytes "hello")))))))

(run-tests)


