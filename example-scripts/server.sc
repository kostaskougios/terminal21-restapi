#!/usr/bin/env -S scala-cli

//> using jvm "21"
//> using scala 3
//> using javaOpt -Xmx128m
//> using dep io.github.kostaskougios::terminal21-server-app:0.30

import org.terminal21.server.Terminal21Server

Terminal21Server.start()
