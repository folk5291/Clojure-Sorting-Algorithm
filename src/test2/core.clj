(ns test2.core
  (:gen-class))

(require '[clojure.java.io :as io])
(require ['clojure.string :as 'str])
(import '(java.util.concurrent Executors))

(defn -main
  [& args]
  '(test2.core))

(defn data [file]
  (with-open [rdr (io/reader file)]
    (doseq [line (line-seq rdr)]
      (slurp file))))

;;Partitioning sequence used from https://gist.github.com/ChrisWphoto/c6f86f34cd7ade5055aec743ec990765
(defn splitInTwo [numSeq]
  (partition (/ (count numSeq) 2) numSeq))

(defn splitInFour [numSeq]
  (partition (/ (count numSeq) 4) numSeq))

(defn split-8 [numSeq]
  (partition (/ (count numSeq) 8) numSeq))

(defn split-16 [numSeq]
  (partition (/ (count numSeq) 16) numSeq))

(defn split-32 [numSeq]
  (partition (/ (count numSeq) 32) numSeq))


(defn Sorter [list]
  (lazy-seq
    (loop [[first & second] list]            
      (if-let [[pivot & pos] (seq first)]    
        (let [smaller? #(< % pivot)]       
          (recur (list*                    
                   (filter smaller? pos)     
                   pivot                    
                   (remove smaller? pos)     
                   second)))                 
        (when-let [[x & second] second]      
          (cons x (Sorter second)))))))

(defn sort-list [pos]
 (Sorter (list pos)))

(print "1 Thread: ")
(time (Sorter (data "C:/Users/jf063535/Desktop/test2/resources/numbers.txt")))

(print "2 Threads: ")
(time (pmap Sorter (splitInTwo (data "C:/Users/jf063535/Desktop/test2/resources/numbers.txt"))))

(print "4 Threads: ")
(time (pmap Sorter (splitInFour (data "C:/Users/jf063535/Desktop/test2/resources/numbers.txt"))))

(print "8 Threads: ")
(time (pmap Sorter (split-8 (data "C:/Users/jf063535/Desktop/test2/resources/numbers.txt"))))

(print "16 Threads: ")
(time (pmap Sorter (split-16 (data "C:/Users/jf063535/Desktop/test2/resources/numbers.txt"))))

(print "32 Threads: ")
(time (pmap Sorter (split-32 (data "C:/Users/jf063535/Desktop/test2/resources/numbers.txt"))))
