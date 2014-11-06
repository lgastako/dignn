(ns dignn.mnist-data
  (:require [clojure.java.io :as io]
            [dignn.mnist :refer :all]))


(def t10k-images-idx3-ubyte
  (read-image-src (io/input-stream (io/resource "mnist/t10k-images-idx3-ubyte"))))

(def t10k-labels-idx1-ubyte
  (read-label-src (io/input-stream (io/resource "mnist/t10k-labels-idx1-ubyte"))))

(def train-images-idx3-ubyte
  (read-image-src (io/input-stream (io/resource "mnist/t10k-images-idx3-ubyte"))))

(def train-labels-idx1-ubyte
  (read-label-src (io/input-stream (io/resource "mnist/t10k-labels-idx1-ubyte"))))
