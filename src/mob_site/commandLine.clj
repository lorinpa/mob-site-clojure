(ns mob-site.commandLine)


(defn num-args-valid? 
  "Determines whether the correct number of command line arguments were passed in."
  [arguments num-expected]
  (let [
          args (vec arguments)
          valid (= (count args) num-expected)
      ]
      valid
  )
)


(defn validCommandLine? 
  "Determines whether a valid command line of
   -in INFILE -out OUTPUTDIR was passed in."
  [arguments num-valid-args ]

  ;; test whether we have the valid number of arguments or else we will throw
  ;; an out of bounds exception

  (if (num-args-valid? arguments num-valid-args)  
     
     ;; main is passed [& args] -- that is a ArraySeq , we have to convert to a vector so 
     ;; that we can pull out each element and test
     
    (let [ args (vec arguments)  

            ;; the every? requires a comparison predicate value and a sequence
            ;; thus the predicate is true? we want all the tests to return true
            ;; we have to convert a series of booleans in to a sequence
            ;; (= args 0) "in") represents a test which evaluates to either true or false
            ;; so we have 2 booleans converted to a sequence
            ;; finally we assign the result of (is every test true) to "valid" and return "valid" 
            ;; as the functions return value
            
            valid	(every? true? ( seq[  (= ( args 0) "-in")  (=   ( args 2) "-out")  ]) )
         ]
        valid
    )
  )
)

(defn trim-trailing-separator 
  "Trims a trailing file separator (if it exists).
   We get the file separator as defined by the host operating
   system."
  [file-spec]
  ;; compare the int values of system separotor and the last character 
  ;; of our file spec/
  ;; If they match then remove the last character
  ;; If not match return the original string
  ;; Note! My first version compared strings versus ints. Failed. Int compared works.
  (let [v (vec file-spec)
        file-sep-int (int ((vec (System/getProperty "file.separator")) 0))
        vlen (count v)
        last-char (v (- vlen 1))
        last-char-int (int last-char)
        result 
        (if 
          (= last-char-int file-sep-int)
          (clojure.string/join ""  (pop v))
          file-spec
        )
      ]
    result
  )
)

(defn create-full-path 
  "Create a full path for a file.
   We use the file separator supplied by the host
   operating system.
  "
  [target-dir file-name]
  (str target-dir (System/getProperty "file.separator") file-name)  
)
