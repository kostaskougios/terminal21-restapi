# Running applications on the server

To create an app that runs on the server, implement the `ServerSideApp` trait and then pass your implementation to the `start()` method of the server:

```scala
class MyServerApp extends ServerSideApp:
  override def name = "My Server App"

  override def description = "Some app that I want to be available when I start the server"

  override def createSession(serverSideSessions: ServerSideSessions, dependencies: Dependencies): Unit =
    serverSideSessions
      .withNewSession("my-server-app-session", name)
      .connect: session =>
        given ConnectedSession = session
  ... your app code ...
```

See for example the [default terminal21 apps](../terminal21-server-app/src/main/scala/org/terminal21/serverapp/bundled).

Now make sure your app is included in the server's classpath and then pass it as an argument to `start()`, i.e. with this `scala-cli` script:

```scala
//> ...
//> using dep MY_APP_DEP

import org.terminal21.server.Terminal21Server

Terminal21Server.start(apps = Seq(new MyServerApp))
```

Now start the server and the app should be available in the app list of terminal21.
