(defproject mob-site "Rel-1.0"
  :description "Clojure implementation of our mobile generation project"
  :url "http://public-actiion.org"
  :license {:name "GNU General Public License, version 2"
            :url "http://www.gnu.org/licenses/gpl-2.0.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
    [commons-io/commons-io "2.4"]
  ]
  :plugins [[codox "0.6.4"]]
  :codox {:src-dir-uri "http://github.com/lorinpa/mob-site-clojure/blob/dev-1.0"
     :src-linenum-anchor-prefix "L" 
  }
  :main mob-site.core
  )
