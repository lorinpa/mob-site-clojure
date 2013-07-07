(ns mob-site.core-test
  (:require [clojure.test :refer :all]
            [mob-site.core :refer :all]
          [mob-site.sitemap :refer :all ]
          [mob-site.commandLine :refer :all]
            ))




(deftest date-format 
  (testing "format-date"
    (is (=(mob-site.sitemap/format-date "Tue, 04 Jun 2013 22:13:24 +0000") "2013-06-04" ))
  )
)

(deftest trailing-file-separator
  (testing "trailing-file-separator" 
      (is (= (mob-site.commandLine/trim-trailing-separator "me/") "me"))
  )
)

(deftest trailing-file-separator-2
  (testing "trailing-file-separator-2" 
      (is (= (mob-site.commandLine/trim-trailing-separator "me") "me"))
  )
)

(deftest test-create-full-path 
  (testing "create-full-path"
    (is (= (mob-site.commandLine/create-full-path "mob" "test.txt") "mob/test.txt" ))  
  )
)


(deftest valid-num-args
  (testing "valid command line routine"
      (let 
          [ args (seq ["-in" "one" "-out" "two"  ]) valid (mob-site.commandLine/num-args-valid? args 4) ] 
          (is (= valid true))
      )
  )
)

(deftest valid-line 
  (testing "command line routine"
      (let 
          [ args (seq ["-in" "one" "-out" "two"  ]) valid (mob-site.commandLine/validCommandLine? args 4 ) ] 
          (is (= valid true))
      )
  )
)
