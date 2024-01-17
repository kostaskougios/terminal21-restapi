package functions.tastyextractor

object BetterErrors:
  def betterError[R](name: String)(f: => R): R =
    try f
    catch case t: Throwable => throw new IllegalStateException(name, t)
