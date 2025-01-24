(ns no.olavfosse.io
  "The cross-host IO library")

;; The idea is to build on the host's IO interfaces. Support the 80/20
;; of IO - the lib could be called paretIO. io.cljc is also very
;; clear. paretIO it is, because it flows. Also abbreviates nicely to
;; pio, e.g pio/bslurp. Not terrible.
;;
;; Ooo, xio would be a really nice name. xio.cljc
;;
;; - [ ] bslurp
;; - [ ] http
;;
;; :clj
;; - Build on java.nio

(defn bslurp [path]
  #?(:clj (with-open [f (io/reader path)] f)
     :lpy (with [f (open path "rb")]
                (.read f))))
