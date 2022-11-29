(defproject program ""

  :dependencies [[org.clojure/clojure "1.10.3"]
                 [org.clojure/core.async "1.3.618"]
                 [org.clojure/spec.alpha "0.2.187"]
                 [org.clojure/data.csv "0.1.4"]
                 [org.clojure/java.jdbc "0.7.9"]
                 [org.postgresql/postgresql "42.2.6"]
                 [clj-time/clj-time "0.15.0"]
                 [mvxcvi/whidbey "2.2.1"]
                 [nrepl/nrepl "0.8.3"]
                 [cider/cider-nrepl "0.25.5"]]

  :source-paths ["src"]
  :target-path "out"
  :main Mr-Pink.main
  :profiles {:uberjar {:aot :all}}
  :repl-options {:init-ns Mr-Pink.main}
  :java-opts ["-Dclojure.compiler.direct-linking=true"]
  :uberjar-name "Mr-Pink.jar")