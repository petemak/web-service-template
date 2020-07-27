# Web service template
A sample project demonstrating creation of API services using and thanks to the following libraries
- #Metosin's reitit ring# for routing: https://github.com/metosin/reitit
- #Metosin's reitit muuntaja middleware# for content-negotiation, request and response formatting.
- #Metosin's reitit Swager UI middleware#
- #Brian Merick's Midje# for testing https://github.com/marick/Midje
- #Weavejester's integrant# for modeling components,their lifecycle methods and dependencies: https://github.com/weavejester/integrant
- #Weavejester's integrant REPL# for reloaded workflow: https://github.com/weavejester/integrant-repl

Thanks to Daw-Ran Liou. His Clojure/Walk tutorials are an excellent resource for for a comprehensive and practical view of how to use of these libraries together to create REST APIs.
https://www.youtube.com/channel/UCj1qlRI5WHAASFw6BeIw_ew

## Usage
Clone and use dev/workflow.clj to start, stop, restart server. Components are defined in system.clj

## License

Copyright © 2020 FIXME

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.
