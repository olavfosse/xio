(ns no.olavfosse.xio
  "The cross-host IO library"
  #?(:clj (:import java.nio.file.Paths
                   java.nio.file.Files
                   java.io.ByteArrayOutputStream
                   java.io.InputStream
                   java.io.ByteArrayInputStream
                   java.io.OutputStream
                   java.io.OutputStreamWriter))
  #?(:clj (:require [clojure.java.io :as jio])))

;; The idea is to build on the host's IO APIs. Support the 80/20 of IO
;; - the lib could be called paretIO. io.cljc is also very
;; clear. paretIO it is, because it flows. Also abbreviates nicely to
;; pio, e.g pio/bslurp. Not terrible.
;;
;; Ooo, xio would be a really nice name. xio.cljc
;;
;; - [x] bslurp
;; - [ ] http
;;
;; :clj
;; - Build on nio
;;
;; Do some work in other repos like:
;; - https://github.com/weavejester/medley/blob/master/src/medley/core.cljc
;; - https://github.com/cgrand/xforms
;; I don't want to duplicate the functionality of these repos



;; IDEA: return a https://github.com/leonoel/clope

#?(:cljs nil
   :default (defn bslurp-raw
              "Like `bslurp`, but returns a native type."
              [f & opts]
              #?(:clj 
                 (let [baos (ByteArrayOutputStream.)]
                   (with-open [^InputStream r (apply jio/input-stream f opts)]
                     (jio/copy r baos)
                     ;; fwiw Clojure may reuse the underlying array
                     ;; if we pass it directly to vec.
                     (mapv (partial + 128) (ByteArrayOutputStream/.toByteArray baos))))
                 :lpy
                 (with [f (open path "rb")]
                       (.read f)))))

#?(:cljs nil
   :default (defn bslurp
              "Like clojure.core/slurp except that it returns the raw binary data as
  a vector of bytes, instead of as a string.

Uses 0-255 range for bytes, which is the same as python, but different from jvm where it is -128-127"
              [f & opts]
              (vec (apply bslurp-raw f opts))))

#?(:clj (defn bspit
          "Like clojure.core/bspit except that it takes raw binary data as a seq
  of bytes and writes to the specified file."
          [f content & options]
          (with-open [^OutputStream os (apply jio/output-stream f options)]
            (OutputStream/.write os (byte-array (mapv (partial - 128) content)) 0 (count content)))))


;; Resources:
;; - Core java for the impatient
;;   - Since this lib is all abt interop might be worth
;;     reading more in this book. i'll read chapter 9 again.
;;     I kinda would like to avoid clojure.java.io and make it
;;     more modern and focussed around the newer APIs. I think
;;     generally the interfaces are the same tho.
;;
;; If I'm really gonna do this properly I'd need to dig into more io
;; APIs. I think it'd be quite educational to actually study and
;; compare several ecosystem's IO APIs and find their overlap and
;; where they differ. Could be some good for an article.
;;
;; Also I need to figure out how the api should look. Will definitely
;; be transducer-first type shi.
;;
;; I really need to find the actual overlap of a bunch of IO systems.
;;
;; I think making this useful is easy, but getting to 1.0 is a big job.
