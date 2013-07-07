(ns mob-site.nodeFormat)



  ;for our clojure implementation we want to edit the body before 
  ;the node is added to our collection.. A lot of work to mutate in clojure.
  ;so let's avoid the over-head 
;
  (defn edit-body
    "This method is simple text replace dedicated to articles
     on my site. That means function included hard-coded values.
     Example of text replacement. 
     Spefically, I am changing the cpntact mechanism.. I am changing links to index.html
     to the mobile site index pages (including a seperate index for both JavaScript 
     category articles and a an index for PolyGlot artilces).
    "
    [str-val ]

    (let [  mutable-str (ref str-val )
            token 
            (format
              " - I'm an experienced developer. My main interest is in new technology. Please use our contact box <a href=\"http://public-action.org/contact\" target=\"_blank\">here</a> if you are interested in hiring me. Please no recruiters :%s"
              (quote ")") 
            )
            replacement-str
            (format
             " is an experienced developer focused on  new technology. Open for work (contract or hire), <a id=\"contact\" href=\"http://public-action.org/mob/contact\" target=\"_blank\">Drop Lorin  a note. </a> <span class=\"muted\">  Please no recruiters :%s</span>"
              (quote ")")
            )
        ] 
    
        (dosync
          (ref-set mutable-str 
            (clojure.string/replace 
                (deref mutable-str)
                token
                replacement-str
            )
          )
        )


      (dosync
        (ref-set mutable-str 
          (clojure.string/replace 
              (deref mutable-str)
              #"http://public-action.org/category/technology/javascript"
              "js-index.html"
          )
        )
      )

      (dosync
          (ref-set mutable-str 
            (clojure.string/replace 
                (deref mutable-str)
                #"http://public-action.org/category/technology/polyglot"
                "polyglot-index.html"
            )
          )
      )

    ); end of let

  )



