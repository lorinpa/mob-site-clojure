(ns mob-site.core
(:use mob-site.parseFeed)
(:use mob-site.nodeFormat)
(:use mob-site.sitemap)
(:use mob-site.docFormatter )
(:use mob-site.indexFile )
(:use mob-site.fileUtils)
(:use mob-site.commandLine)
)


(defn main-process 
  "Method directs our overall process.
   Verifies command line.
   Starts the SAX parser
   Creates the mobile site documents and file.
  "
  [input-file output-dir]

  (println (format "processing input file: %s , output directory: %s" input-file output-dir))
 
  ( let
    [
      ;; parse the RSS xml document and retrieve our document fragment collection (nodes)
      nodes (extract-text input-file)
      ;; defuine our target directory, trim the trailing file separator if necessary
      targetDir (str (System/getProperty "user.dir") "/" (trim-trailing-separator output-dir ))
    ]
    (println (format "Parsed %d articles from %s" (count nodes) input-file))
    ;; create our target directory
    (createDir targetDir)

    ;; create the mobile site map
    (let [ sm	(build-sitemap "http://public-action.org" output-dir nodes) ] 
      (spit (create-full-path targetDir "sitemap.xml") sm)
      (println (	format "created  filename: %s" (create-full-path targetDir "sitemap.xml") ))
    )

    ;; format nodes as web pages, write each web page to disk
    (doseq [node nodes]
        (let [ 
            filename  (create-full-path targetDir (format-link (:link node)) )
        ]
         (spit filename (webDocFormat  node  "AA-side"  "a-side.html"  true))
        )
    )

    ;; create the main index/table of contents page
    (let
         [  filename  (create-full-path targetDir "index.html" )  ; creates the file name and assign it to a local symbol	
            idxDoc (createIndexDoc "Public Action Articles" "index.html" nodes) ; format data (list or articles) as a web page assign result to local symbol
        ]
         
        (spit filename (webDocFormat  idxDoc  "AA-side"  "a-side.html"  false) ) ; write web page to disk -- assign the web page nav bar values
    )

    ;; create the poly article index page
    (let [ poly-articles
        (filter
            ( fn[n] 
                    (some #(= "Polyglot" %) (:categories n ) )
            )
            nodes
        )
            filename  (create-full-path targetDir "polyglot-index.html" ) 
            idxDoc (createIndexDoc "Public Action PolyGlot Articles" "index.html" poly-articles)

        ]
        (println (format "there are %d polyglot articles " (count poly-articles)))
        (spit filename (webDocFormat  idxDoc  "AA-side"  "a-side.html"  false) )
    )

    ;; create the javascript article index page
    (let [ js-articles
        (filter
            ( fn[n] 
                    (some #(= "JavaScript" %) (:categories n ) )
            )
            nodes
        )
            filename  (create-full-path targetDir "js-index.html" ) 
            idxDoc (createIndexDoc "Public Action JavaScript Articles" "index.html" js-articles)
        ]
        (println (format "there are %d js articles " (count js-articles)))
        (spit filename (webDocFormat  idxDoc  "AA-side"  "a-side.html"  false) )
    )

    ;; copy out static contents
    (if (isDir targetDir)
      (copySet (str (System/getProperty "user.dir") "/static-content") targetDir  )
    )
  ) ;; end of let
) ;; end of function 

(defn -main 
  "Entry point to the application."
  [& args]
  (if (validCommandLine? args 4) 
      (do
          (let [
                argv (vec args)
                input-file (argv 1)
                output-dir (argv 3)
          ]
          (main-process input-file output-dir)
         ); end of let
      )
      (println "Usage: -in INPUTFILE -out OUTPUTDIR")
  )
)
