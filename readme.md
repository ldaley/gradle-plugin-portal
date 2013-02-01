This is a Ratpack app, which is a thin veneer over Vert.x that gives some convenience for doing quick apps.

Vert.x doesn't have a static file server, or session management, or a few other things that make prototyping easy.
It doesn't really change the nature of the app much at all.

## How to use it

Either `./gradlew run` or `./gradlew idea`, then use the pre0configured run configuration.

Go to `http://localhost:5050`. You can sign in, use a traditional HTTP POST, or use the “Ajax Sign In” button to make
 the auth request via Ajax (there to test that session cookies set via Ajax functional as expected; they do).

The app is using 4 different data sources to authn users:

1. A programatically defined in memory store
2. An LDAP server (running embedded, but queried over the network)
3. A JDBC server (embedded H2)
4. Shiro's flat file.

Look at `src/ratpack/ratpack.groovy` for the details. While these data sources are defined programatically, Shiro
has a mechanism for loading this kind of config from a file which maps pretty directly to the code. It's just easier
to experiment with them programatically defined.

## Users

All users have password “password”.

The in memory store has: `user1`, `user2`
The ldap store has: `ldap1`, `ldap2`
The jdbc store has: `db1`, `db2`
The ini file store has: `ini1`, `ini2`

## Some other stuff

For the hell of it, we are also using the Vert.x message bus including receiving messages in the browser. The valid code
for this is in Init.java, event.html and event.js. One the login page, you can hit the “Open Event Log” button or
go to /events in another tab.

`LoginPage` is a bit of a beast. It could definitely be refactored to be cleaner.

## What's next

Some playing with perms and roles.
