(ns mob-site.sitemap
(:use mob-site.parseFeed)
(:use mob-site.nodeFormat)
)


(defn convert-month 
  "Converts a three letter month representation to a
   two digit month representation."
  [month]
     (cond 
       (= "Jan" month) "01"
       (= "Feb" month) "02"
       (= "Mar" month) "03" 
       (= "Apr" month) "04" 
       (= "May" month) "05"  
       (= "Jun" month) "06"
       (= "Jul" month) "07"
       (= "Aug" month) "08"
       (= "Sep" month) "09"
       (= "Oct" month) "10"
       (= "Nov" month) "11"
       (= "Dec" month) "12"
       :default "ooops"
    ) 
  )


  (defn format-date 
    "Converts a long date format to a short yyyy-mm-dd."
    [date] 
    (def elems  (clojure.string/split date  #"\s"))
      (format "%s-%s-%s" (elems 3)  (convert-month(elems 2)) (elems 1) )
  )

(defn relative-link 
 "Creates a full path and file name. 
  Strips out the original path (from the RSS XML doc) and
  replaces with our new mobile site path.
 " 
 [base-addr dir link] 
 (let [	 
          link-elems  (clojure.string/split link  #"/")
          link-elems-len (- (count link-elems) 1)
          link-result (format "%s/%s/%s.html" base-addr dir (link-elems link-elems-len))
      ]
      link-result
  )
)

(defn format-item
  "Creates and formats an XML document fragement.
   Document fragement represents an individual 
   artilcle/web page/document.
   "
  [base-addr dir pubDate link]
  
  (format 
    "<url>
     <loc>%s</loc>
     <lastmod>%s</lastmod>
     </url>" (relative-link base-addr dir link) (format-date pubDate)
    )

)


(defn build-body 
  "Build a collection of XML document fragments
   which serve as the body. 
  "
  [base-dir dir nodes] 
  
  (let [buf (ref[])] 
    (doseq [f nodes]
        (add-element buf
            (format-item base-dir dir  (:pubDate f) (:link f))
        )
    )
    (vector-to-string buf)
   )
)



(defn build-sitemap
  "Method combines all of our fragments together and returns
   a full site map xml document.
   Adds a document header and the xml parent set (urlset).
  "
  [base-addr dir nodes]

  
  (let [ 
        node-list nodes
        last-mod-date (format-date (:pubDate (first nodes))) 
         
        header 
        (format
          "<?xml version='1.0' encoding='UTF-8'?>
          <urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\"
           xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"
           xsi:schemaLocation=\"http://www.sitemaps.org/schemas/sitemap/0.9
           http://www.sitemaps.org/schemas/sitemap/0.9/sitemap.xsd\">
           <url>
           <loc>%s</loc>
           <changefreq>daily</changefreq>
           <priority>1.0</priority>
           <lastmod>%s</lastmod>
           </url>" base-addr last-mod-date
          )
         body
          (build-body base-addr dir node-list)
        ] 
        (str header body "</urlset>" )
   ) ; end of let
 )
