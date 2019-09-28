(ns immutant-websockets.server.core
  (:require
    [immutant.web             :as web]
    [immutant.web.async       :as async]
    [immutant.web.middleware  :as web-middleware]
    [compojure.route          :as route]
    [environ.core             :refer (env)]
    [compojure.core           :refer (ANY GET defroutes)]
    [ring.util.response       :refer (response redirect content-type)])
  (:gen-class))

(def channel-store (atom []))

(defn send-message-to-all [m]
  "Sends a message to all connected ws connections"
    (doseq [ch @channel-store]
      (async/send! ch m)))

; (:id (ws/session h) get user ID of message
; need to be able to pool these and send it out to all with ID
(def websocket-callbacks
  "WebSocket callback functions"
  {:on-open   (fn [channel]
    (swap-vals! channel-store conj channel)) ; store channels for later
  :on-close   (fn [channel {:keys [code reason]}]
    (swap! channel-store (partial filter (fn [chan]
      (if (= chan channel)
        false
        true)))))
  :on-message (fn [ch m]
    (send-message-to-all m))})


(defroutes routes
  (GET "/" {c :context} (redirect (str c "/index.html")))
  (route/resources "/"))

(defn -main [& {:as args}]
  (web/run
    (-> routes
      ; (web-middleware/wrap-session {:timeout 20})
      ;; wrap the handler with websocket support
      ;; websocket requests will go to the callbacks, ring requests to the handler
      (web-middleware/wrap-websocket websocket-callbacks))
      (merge {"host" (env :demo-web-host), "port" 8080}
      args)))