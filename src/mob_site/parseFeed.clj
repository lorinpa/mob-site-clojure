(ns mob-site.parseFeed
 (:use mob-site.nodeFormat)
)

(import '(javax.xml.parsers SAXParserFactory)
'(org.xml.sax ContentHandler)
'(org.xml.sax.ext DefaultHandler2)
'(java.io File)
'(java.lang StringBuilder)
)

;my custom mutable  functions
(defn vector-clear 
  "Utility which clears a vector of clojure references.
   Part of my small set of mutable functions.
  "
  [v]
    (dosync (ref-set v (vector nil)))
)

(defn vector-to-string 
  "Utility which converts a vector of clojure references 
   to a string.
   Part of my small set of mutable functions.
  "
  [v]
  (clojure.string/join "" (deref v))
)

(defn add-element 
  "Utility which appends a mutable collection.
   Part of my small set of mutable functions.
  "
  [collection elem]
  (dosync 
    (alter collection conj elem)
  )
)

(defn mutable-hash-put 
  "Utility which appends a key/value set to
  a mutable hash-set.
  Part of my small set of mutable functions.
  "
  [mp key val] 
  (dosync 
    (ref-set mp (assoc (deref mp) key val))
  )
)

(defn mutable-put-list-elem 
  "Utility which appends a collection as a key/value set 
  in a parent hash set. Used for multiple categories related to
  a single article (item node in the RSS XML doc).
  Part of my small set of mutable functions.
  "
  [collection key val] 
  (dosync 
    (ref-set collection (conj (deref collection) key val))
  )
)

(defn clear-hash 
  "Utitilty which resets/empties a mutable hash set/
  Part of my small set of mutable functions.
  "
  [hs]
  (dosync 	
    (ref-set hs {})
  )
)

(defn map-ref-get [mp key]
  ;; map being a map-ref
  ;; key E.G. :categories	
  (key (deref mp))
)

(defn add-to-mapped-sequence
  "Utility which appends a mutable sequence.
   The sequence is then mapped to a parent hash-set
   as defined by the parameter (key).
  Part of my small set of mutable functions.
  "
  [mp key value]
  (let [tseq (map-ref-get mp key)]
    (mutable-hash-put
      mp key (conj tseq value)
    )
  )
)

(defn add-keyed-sequence 
  "Adds a sequence to a parent mutable hash set.
  Part of my small set of mutable functions.
  "
  [mp key sq]
  ;; mp is a map-ref
  ;; key is E.G. :categories
  ;; sq is a sequence-ref	
  (mutable-hash-put
    mp key (deref sq)
  )
)


;;
; sax handler
;;
(defn proxy-handler 
  "SAX event listener. This method wraps the java sax DefaultHandler2.
  This is my clojure implementation of the SAX event handler. 
  
  Note! This is the first listing I wrote in clojure and thus the most likey 
  candidate for  refactoring. Used clojure referneces to build the document as 
  SAX events are fired. The other functions in this listing, break down the 
  tasks in to  manageable pieces.
  "
  []

  ; acts as a string buffer
  (def sb (ref[]))
  ; our node 
  (def node (ref {:title "" :pubDate ""}))
    ; our node collection
  (def nodeList (ref[]))
   (def start (ref false ))

   (defn getNodeList [] (deref nodeList))


  (proxy [DefaultHandler2]

      [] ;; constructor  no args

      (characters [ch start length]
          (add-element sb (String. ch start length))
      )

      (startElement [uri localName qName  atts ]
          (vector-clear sb)
          (if (= "language" qName)
              (dosync (ref-set start true))
          )
      )

      (endElement [uri localName qName  ]
          (if (= (deref start) true)
              (cond
                  (= "title" qName) 
                      (mutable-hash-put node :title (str (vector-to-string sb)))
                  (= "link" qName) 
                      (mutable-hash-put node :link (str (vector-to-string sb)))
                  (= "pubDate" qName) 
                      (mutable-hash-put node :pubDate (vector-to-string sb))
                  (= "description" qName) 
                      (mutable-hash-put node :body (edit-body (vector-to-string sb)))
                  (= "category" qName) 
                      ;(mutable-hash-put node :category  (vector-to-string sb))
                      (add-to-mapped-sequence node :categories (vector-to-string sb))
                  (= "item" qName)
                      (do
                          (add-element nodeList (deref node))
                          (clear-hash node)
                          (add-keyed-sequence node :categories (ref()))
                      )
                  )
              )
      )
  )
)


(defn extract-text [filename]
  (let [parser (.newSAXParser (SAXParserFactory/newInstance))]
    (.parse parser (File. filename) (proxy-handler)))

   (getNodeList)
)
