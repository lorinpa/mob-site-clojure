(ns mob-site.fileUtils)

( import '(org.apache.commons.io FileUtils)
'(java.io File)
)


(defn createDir 
  "Creates directory if does not
   already exit. A simple utility which
   wraps the java.io.File mkdir function.
  "
  [subDir] 
  ( let [ file (File. subDir)]
      (if (false? (.exists file))
          (.mkdir file)
      )
  )
)

 
(defn isDir 
  "Determines whether a directory exists or not.
   A simple utility which wraps the java.io.File
   isDirectory function.
  "
  [dirLoc]
  (let[ dir (File. dirLoc) ] 
      (.isDirectory dir)
  )
 )
 
 (defn copySet 
   "Copies a source directory (and all of the child contents) to
    the destination directory. Uses the Apache Commons IO library
    to perform the file and directory copies.
   "
   [srcParentDir destDir ]
  (let 	[ 	srcDir (File. srcParentDir) files (.list srcDir)]
      (doseq [f files]
          (let [
                  destPath (str destDir "/" f)
                  srcPath (str srcParentDir "/" f )
                  elem (File. srcPath) 
                  
                  aDir (.isDirectory elem)
          ]
          (cond 
              (= aDir  true) (FileUtils/copyDirectory (File. srcPath)  (File. destPath ))
              (= aDir false) (FileUtils/copyFile  (File. srcPath) (File. destPath))
          )
              
          ) ; end inner let
      ) ; end do seq
  ) ; end outer let 
 ) ; end function

