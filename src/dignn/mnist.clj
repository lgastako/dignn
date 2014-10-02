(ns dignn.mnist
  (:require [clojure.java.io :refer [input-stream]]
            [dignn.xlib :refer [ifn]]
            [org.clojars.smee.binary.core :refer [decode ordered-map repeated]]))

;; Functions for reading mnist file formats

(def gunzip #(-> % java.util.zip.GZIPInputStream.))
(def gzipped? #(-> (.endsWith (str %) ".gz")))

(defn parse-src [parser src]
  (let [parse #(decode parser %)]
    (-> src
        input-stream
        (ifn gzipped? gunzip)
        parse)))

;; [offset] [type]          [value]          [description]
;; 0000     32 bit integer  0x00000801(2049) magic number (MSB first)
;; 0004     32 bit integer  60000            number of items
;; 0008     unsigned byte   ??               label
;; 0009     unsigned byte   ??               label
;; ........
;; xxxx     unsigned byte   ??               label
;; The labels values are 0 to 9.

(defn read-label-src [src]
  (->> src (parse-src (ordered-map
                       :magic-number :int-be
                       :num-items :int-be
                       :labels (repeated :ubyte)))))

;; [offset] [type]          [value]          [description]
;; 0000     32 bit integer  0x00000803(2051) magic number
;; 0004     32 bit integer  60000            number of images
;; 0008     32 bit integer  28               number of rows
;; 0012     32 bit integer  28               number of columns
;; 0016     unsigned byte   ??               pixel
;; 0017     unsigned byte   ??               pixel
;; ........
;; xxxx     unsigned byte   ??               pixel

(defn read-image-src [src]
  (->> src (parse-src (ordered-map
                       :magic-number :int-be
                       :num-images :int-be
                       :num-rows :int-be
                       :num-cols :int-be
                       :pixels (repeated :ubyte)))))
