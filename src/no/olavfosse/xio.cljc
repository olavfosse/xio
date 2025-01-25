(ns no.olavfosse.xio
  "The cross-host IO library"
  #?(:clj (:import java.nio.file.Paths
                   java.nio.file.Files)))

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

(defn bslurp [path]
  (->
   #?(:clj (-> (^[String String/1] Paths/get path (into-array String []))
               Files/readAllBytes)
      :lpy (with [f (open path "rb")]
                 (.read f)))
   ;; On the JVM this reuses the underlying array
   vec))

