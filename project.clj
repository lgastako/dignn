(defproject dignn "0.1.0-SNAPSHOT"
  :description "A neural network for recognizing digits."
  :url "http://github.com/lgastako/dignn"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :main dignn.core
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [its-log "0.2.2"]
                 [org.clojars.smee/binary "0.3.0"]
                 [net.mikera/core.matrix "0.31.1"]
                 [incanter "1.5.5"]])
