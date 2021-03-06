(ns dignn.xlib)


(defn ifn [pred? f]
  "Return a function that applies f to its input if f matches pred? and otherwise returns x."
  (fn [x]
    (if (pred? x)
      (f x)
      x)))

;; From user3742065 here:
;; http://stackoverflow.com/questions/23018870/how-to-read-a-whole-binary-file-nippy-into-byte-array-in-clojure

(defn slurpb [is]
  "Convert an input stream is to byte array"
  (with-open [baos (java.io.ByteArrayOutputStream.)]
    (let [ba (byte-array 2000)]
      (loop [n (.read is ba 0 2000)]
        (when (> n 0)
          (.write baos ba 0 n)
          (recur (.read is ba 0 2000))))
      (.toByteArray baos))))
