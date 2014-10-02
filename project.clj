(defproject dignn "0.1.0-SNAPSHOT"
  :description "A neural network for recognizing digits."
  :url "http://github.com/lgastako/dignn"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :main dignn.core
  :profiles {:dev {:source-paths ["dev"]
                   :dependencies [[org.clojure/tools.namespace "0.2.3"]
                                  [org.clojure/java.classpath "0.2.0"]]}}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [its-log "0.2.2"]
                 [org.clojars.smee/binary "0.3.0"]])
