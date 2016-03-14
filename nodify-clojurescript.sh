#!/bin/bash

rm -rf out/
lein run -m clojure.main script/build.clj

sed '1d' main.js > clojurescript.js 
echo "exports.core = cljs_bootstrap.core" >> clojurescript.js
