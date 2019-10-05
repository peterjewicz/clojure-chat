# Clojure Chat

A simple chat application with Clojure using websockets


## Setup

To run UI

    lein figwheel

and open your browser at [localhost:3449](http://localhost:3449/).
This will auto compile and send all changes to the browser without the
need to reload. After the compilation process is complete, you will
get a Browser Connected REPL.

To start the server run a repl from server/core and run

    (-main)

This should start up a websocket server on the provided port

The server can be run from any REPL afterwards run ````(-main)```` which will start it up.


## License

Copyright © 2014 FIXME

Distributed under the Eclipse Public License either version 1.0 or (at your option) any later version.
