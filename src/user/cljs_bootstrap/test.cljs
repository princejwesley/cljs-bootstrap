(ns cljs-bootstrap.test
  (:require [cljs.js :as cljs]))

(enable-console-print!)

(set! *target* "nodejs")
(def st (cljs/empty-state))

(comment
  (require-macros '[cljs.env.macros :as env])
  (require '[cljs.pprint :as pp]
           '[cljs.env :as env]
           '[cljs.analyzer :as ana]
           '[cljs.compiler :as comp])

  ;; works
  (cljs/eval st '(defn foo [a b] (+ a b))
    {:eval-fn cljs/js-eval}
    (fn [res]
      (println res)))

  ;; works
  (cljs/compile st "(defprotocol IFoo (foo [this]))"
    (fn [js-source]
      (println "Source:")
      (println js-source)))

  ;; works
  (cljs/eval-str st
    "(defn foo [a b] (+ a b))
     (defn bar [c d] (+ c d))"
    {:eval-fn cljs/js-eval}
    (fn [res]
      (println res)))

  ;; works
  (cljs/eval st '(ns foo.bar)
    {:eval-fn cljs/js-eval}
    (fn [res]
      (println res)))

  (binding [cljs/*load-fn*
            (fn [{:keys [name]} cb]
              (println name)
              (cb {:lang :js
                   :source "function hello() { console.log(\"Hello!\"); };"}))]
    (cljs/compile st "(ns foo.bar (:require [hello-world.core]))"
      {:verbose true
       :source-map true}
      (fn [js-source]
        (println "Source:")
        (println js-source))))

  ;; works!
  (cljs/compile st "(defn foo\n[a b]\n(+ a b))" nil
    {:verbose true :source-map true}
    (fn [js-source]
      (println "Source:")
      (println js-source)))

  (def vm (js/require "vm"))

  (cljs/eval-str st "(ns foo.bar (:require [hello-world.core]))"
    {:verbose true
     :eval-fn (fn [{:keys [name source]}]
                (.runInThisContext vm source name))
     :load-fn (fn [{:keys [name]} cb]
                (println name)
                (cb {:lang :js
                     :source "function hello() { console.log(\"Hello!\"); };"}))}
    (fn [js-source]
      (println "Source:")
      (println js-source)))
  )