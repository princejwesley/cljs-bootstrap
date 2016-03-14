#!/bin/bash

rm -rf out/
lein run -m clojure.main script/build.clj

sed '1d' main.js > clojurescript.js 
echo "module.exports = { compiler: cljs_bootstrap.core, goog: goog, cljs: cljs }" >> clojurescript.js
