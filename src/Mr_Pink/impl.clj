(ns Mr-Pink.impl
  (:require [clojure.repl :refer :all]
            [clojure.reflect :refer :all]
            [clojure.pprint :as pp]
            [clojure.java.io :as io]
            [clojure.string :as cstr]
            [clojure.java.javadoc]
            [clojure.zip :as zip]
            [clojure.xml :as xml]
            [puget.printer :as pug]
            ;
            ))


(defn read-nth-line
  "Read line-number from the given text file. The first line has the number 1."
  [filename line-number]
  (with-open [rdr (clojure.java.io/reader filename)]
    (nth (line-seq rdr) (dec line-number))))

(defn count-lines
  [filename]
  (with-open [rdr (clojure.java.io/reader filename)]
    (count (line-seq rdr))))

(defn nl
  "append newline char to str"
  [s]
  (str s "\n"))

(defn write-lines
  "Write lines vector to file"
  [filename lines-vec]
  (with-open [w (clojure.java.io/writer filename :append true)]
    (doseq [line lines-vec]
      (.write w (nl line)))))

(defn delete-files
  [& filenames]
  (doseq [filename filenames]
    (.delete (java.io.File. filename))))

(defn mk-dirs
  "Make directories in the path"
  [path]
  (.mkdirs (java.io.File. path)))

(defn create-file
  [filename]
  (.createNewFile (java.io.File. filename)))

(defn read-binary-file
  "returns bute-array , accepts :offset :limit"
  [filename & {:keys [offset limit]
               :or   {offset 0}}]
  (let [f   (io/file filename)
        lim (or limit (- (.length f) offset))
        buf (byte-array lim)]
    ; (prn lim offset)
    (with-open [in (io/input-stream  f)]
      (.skip in offset)
      (.read in buf 0 lim)
      buf
      ;
      )))

(defn bytes->int [bytes]
  "Converts a byte array into an integer."
  (->>
   bytes
   (map (partial format "%02x"))
   (apply (partial str "0x"))
   read-string))

(defn read-int-from-file
  "returns the number represented by bytes in range [offset , + offset  limit]"
  [filename offset limit]
  (->   (read-binary-file  filename :offset offset :limit limit)
        bytes->int))

(comment
  ;
  )


(comment

  ; (Examples/hello)
  {:hello "world"}

  (System/getProperty "java.vm.version")
  (System/getProperty "java.version")
  (System/getProperty "java.specification.version")
  (clojure-version)


  ;
  )


(defn try-parse-int
  "returns number or nil"
  [number-string]
  (try (Integer/parseInt number-string)
       (catch Exception e nil)))

(defn try-parse-float
  "returns number or nil"
  [number-string]
  (try (Float/parseFloat number-string)
       (catch Exception e number-string)))

(defn replace-double-quotes
  [s & {:keys [ch]
        :or   {ch "'"}}]
  (clojure.string/replace s #"\"" ch))

(defn split-tab
  "Splits the string by tab char"
  [s]
  (cstr/split s #"\t"))

(defn filter-by-key
  [coll key]
  (filter #(-> % :key #{key}) coll))

(defn drop-nth
  "Remove nth element from coll"
  [n coll]
  (keep-indexed #(if (not= %1 n) %2) coll))

(defn nth-seq
  "Returns the nth element in a seq"
  [coll index]
  (first (drop index coll)))

(comment


  (try-parse-int "3")

  ;
  )

(defn prn-members
  "Prints unique members of an instance using clojure.reflect"
  [inst]
  (->>
   (reflect inst)
   (:members)
   (sort-by :name)
   (map #(:name %))
   (set)
   (into [])
   (sort)
   pp/pprint
  ;  (pp/print-table )
  ;  (pp/print-table [:name :flags :parameter-types])
   ))


(defn javadoc-print-url
  "Opens a browser window displaying the javadoc for the argument.
  Tries *local-javadocs* first, then *remote-javadocs*."
  {:added "1.2"}
  [class-or-object]
  (let [^Class c (if (instance? Class class-or-object)
                   class-or-object
                   (class class-or-object))]
    (if-let [url (#'clojure.java.javadoc/javadoc-url (.getName c))]
    ;   (browse-url url)
      url
      (println "Could not find Javadoc for" c))))

(comment

  (source javadoc)
  (source clojure.java.javadoc/javadoc-url)


  (apropos "javadoc-url")

  (javadoc-print-url Runtime)
  (javadoc-print-url String)

;;;
  )


(defn partition-into-vecs
  "Returns vec of vecs "
  [part-size v]
  (->>
   (partition part-size v)
   (mapv vec)))


(defn cprn
  "color-prints a value "
  ([x]
   (pug/cprint x nil))
  ([x opts]
   (pug/cprint x opts)))

(defn sytem-prn
  "print using System.out.println"
  [msg]
  (.println (System/out) msg))

(comment

  (cprn [1 2 3 4 5])

  (sytem-prn "3")
  ;;;
  )

(defn rand-int-in-range
  "returns random int in range a b"
  [a b]
  (int (- b (* (rand) (- b a)))))

(defn nth-seqio
  "Returns the nth element in a seq"
  [coll index]
  (first (drop index coll)))

;;convenience function, first seen at nakkaya.com later in clj.zip src
(defn zip-str [s]
  (zip/xml-zip
   (xml/parse (java.io.ByteArrayInputStream. (.getBytes s)))))