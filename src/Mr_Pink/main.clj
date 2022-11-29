(ns Mr-Pink.main
  (:gen-class)
  (:require [Mr-Pink.nrepl]
            [Mr-Pink.impl]
            [Mr-Pink.etl]
            [Mr-Pink.query]
            [Mr-Pink.psql]
   ;
            )
  ;
  )


(defn -dev  [& args]
  (Mr-Pink.nrepl/-main)
  #_(tool.pedestal.server/run-dev))

(defn -main  [& args]
  (Mr-Pink.nrepl/-main)
  )

