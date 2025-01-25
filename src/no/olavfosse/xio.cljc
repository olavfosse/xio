(ns no.olavfosse.xio
  "The cross-host IO library"
  #?(:clj (:import java.nio.file.Paths
                   java.nio.file.Files
                   java.io.ByteArrayOutputStream
                   java.io.InputStream
                   java.io.ByteArrayInputStream))
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
(defn slurp
  "Opens a reader on f and reads all its contents, returning a string.
  See clojure.java.io/reader for a complete list of supported arguments."
  {:added "1.0"
   :tag String}
  ([f & opts]
     (let [opts (normalize-slurp-opts opts)
           sw (java.io.StringWriter.)]
       (with-open [^java.io.Reader r (apply jio/reader f opts)]
         (jio/copy r sw)
         (.toString sw)))))
(defn bslurp
  "Exactly the same as clojure.core/slurp except that it returns the raw
  binary data as a vector of bytes"
  #?(:clj [f & opts]
     (let [baos (ByteArrayOutputStream.)]
       (with-open [^InputStream r (apply jio/input-stream f opts)]
         (jio/copy r sw)
         (ByteArrayInputStream sw))))
    ;; TODO: should support same types as slurp
  (->
   #?(:clj ()
      :lpy (with [f (open path "rb")]
                 (.read f)))
   ;; On the JVM this reuses the underlying array
   vec))


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
