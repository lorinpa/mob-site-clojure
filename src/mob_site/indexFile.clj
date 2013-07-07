(ns mob-site.indexFile
  (:use mob-site.nodeFormat)
  (:use mob-site.parseFeed)
)

(defn format-link 
  "Converts the original RSS XML document item link 
   to a relative link. Removes the original directory and path,
   Adds .html to suffix our link/path.
  "
  [ link]
  ( let
      [
        link-elems  (clojure.string/split link  #"/")
        link-elems-len (- (count link-elems) 1)
        formatted-link (str (link-elems link-elems-len) ".html" )
      ]
      formatted-link
  ) ; end of let
)

(defn create-title-list
  "Creates a fragment of our index web page. 
   Creates a list of links. The links represent each child page.
   We are created a table of contents index/document.
  "
  [nodes]
  (let [ buf 	(ref[])	]
        (doseq [node nodes]
            (add-element buf
                (format
                    "<li><a href=\"%s\">%s</a></li>\n"
                    (format-link (:link node) )
                    (:title node)
                )
            )
        )
        (vector-to-string buf)
  ) ; end of let
)


(defn createIndexDoc 
  "Method puts together all the fragement and 
   returns a full html document. The html document is a 
   table of contents/index web pagee.
  "
  [docTitle docLink nodeList]
  (let
      [
        start_order_list 
        (format
            "<ul class=\"nav nav-list\">\n
                <li class=\"nav-header\">%s</li>\n"
            docTitle
        )	
         indexDoc {:title docTitle :link docLink :body (str start_order_list  (create-title-list nodeList)   "</ul>" ) :categories () }
      ]
      indexDoc
  ) ;; end of let
)




