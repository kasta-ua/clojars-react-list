(set-env!
  :resource-paths #{"resources"}
  :dependencies '[[adzerk/bootlaces   "0.1.10" :scope "test"]
                  [cljsjs/boot-cljsjs "0.5.0"  :scope "test"]
                  [cljsjs/react       "0.14.3-0"
                   cljsjs/react-dom   "0.14.3-1"]])

(require
  '[adzerk.bootlaces :refer :all]
  '[cljsjs.boot-cljsjs.packaging :refer :all])

(def +version+ "0.7.8-0")
(bootlaces! +version+)

(task-options!
  pom {:project     'ua.modnakasta/react-list
       :version     +version+
       :description "A versatile infinite scroll React component."
       :url         "https://github.com/orgsync/react-list"
       :scm         {:url "https://github.com/modnakasta/clojars-react-list"}
       :license     {"MIT" "http://opensource.org/licenses/MIT"}})

(deftask *download []
  (download :url      "https://github.com/orgsync/react-list/archive/0.7.8.zip"
            :checksum "23ce47f6785a466d97c1a96994f3a2c2"
            :unzip    true))

(deftask package []
  (comp
    (*download)
    (sift :move {#"^react-list-.*/react-list.js"
                 "mk/react-list/development/react-list.inc.js"})
    (minify :in  "mk/react-list/development/react-list.inc.js"
            :out "mk/react-list/production/react-list.min.inc.js")
    (sift :include #{#"^mk"})
    (deps-cljs :name "mk.react-list"
               :requires ["cljsjs.react"])))

(deftask clojars []
  (comp
    (package)
    (build-jar)
    (push-release)))
