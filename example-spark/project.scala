//> using jvm "21"
//> using scala 3

// these java params are needed for spark to work with jdk 21
//> using javaOpt --add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/sun.nio.ch=ALL-UNNAMED --add-opens java.base/sun.util.calendar=ALL-UNNAMED --add-opens java.base/sun.security.action=ALL-UNNAMED --add-opens java.base/java.io=ALL-UNNAMED

// configure logback
//> using javaOpt -Dlogback.configurationFile=file:etc/logback.xml

// terminal21 dependencies
//> using dep io.github.kostaskougios::terminal21-ui-std:0.12
//> using dep io.github.kostaskougios::terminal21-spark:0.12
//> using dep io.github.kostaskougios::terminal21-nivo:0.12
//> using dep io.github.kostaskougios::terminal21-mathjax:0.12

//> using dep ch.qos.logback:logback-classic:1.4.14

//> using file model
