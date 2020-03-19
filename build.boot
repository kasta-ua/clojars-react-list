(set-env!
  :resource-paths #{"resources"}
  :dependencies '[[adzerk/bootlaces   "0.1.13" :scope "test"]
                  [cljsjs/boot-cljsjs "0.5.2"  :scope "test"]
                  [cljsjs/react       "15.3.0-0"]
                  [cljsjs/react-dom   "15.3.0-0"]])


(require
  '[adzerk.bootlaces :refer :all]
  '[cljsjs.boot-cljsjs.packaging :refer :all])


(def +lib-version+ "0.8.11")
(def +version+ (str +lib-version+ "-1"))


(bootlaces! +version+)


(task-options!
  pom {:project     'ua.kasta/react-list
       :version     +version+
       :description "A versatile infinite scroll React component."
       :url         "https://github.com/orgsync/react-list"
       :scm         {:url "https://github.com/kasta-ua/clojars-react-list"}
       :license     {"MIT" "http://opensource.org/licenses/MIT"}})


(deftask package []
  (comp
    (download :url      (format "https://github.com/orgsync/react-list/archive/%s.zip" +lib-version+)
              :checksum "2520790B88291B1884F88A77EB71A1D9"
              :unzip    true)

    (sift :move {#"^react-list-.*/react-list.js"
                 "mk/react-list/development/react-list.inc.js"})

    (minify :in   "mk/react-list/development/react-list.inc.js"
            :out  "mk/react-list/production/react-list.min.inc.js"
            :lang :ecmascript5)

    (sift :include #{#"^mk"})

    (deps-cljs :name "mk.react-list"
               :requires ["cljsjs.react"])

    (pom)

    (jar)))


(deftask clojars []
  (comp
    (package)
    (push-release)))
