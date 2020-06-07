package io.mocky.db

import com.typesafe.scalalogging.StrictLogging
import doobie.util.log.{ ExecFailure, LogHandler, ProcessingFailure, Success }

trait DoobieLogHandler {
  self: StrictLogging =>

  implicit protected val doobieLogHandler: LogHandler = {
    LogHandler {

      case Success(s, a, e1, e2) =>
        // If trace level is not enabled, just log a succinct message
        if (!self.logger.underlying.isTraceEnabled) {
          self.logger.debug(s"SQL request executed with success in ${(e1 + e2).toMillis.toString} ms")
        } else {
          self.logger.trace(s"""Successful Statement Execution:
                               |
                               |  ${s.linesIterator.dropWhile(_.trim.isEmpty).mkString("\n  ")}
                               |
                               | arguments = [${a.mkString(", ")}]
                               |   elapsed = ${e1.toMillis.toString} ms exec + ${e2.toMillis.toString} ms processing (${(e1 + e2).toMillis.toString} ms total)
            """.stripMargin)
        }
      case ProcessingFailure(s, a, e1, e2, t) =>
        self.logger.error(s"""Failed Resultset Processing:
                             |
                             |  ${s.linesIterator.dropWhile(_.trim.isEmpty).mkString("\n  ")}
                             |
                             | arguments = [${a.mkString(", ")}]
                             |   elapsed = ${e1.toMillis.toString} ms exec + ${e2.toMillis.toString} ms processing (failed) (${(e1 + e2).toMillis.toString} ms total)
                             |   failure = ${t.getMessage}
          """.stripMargin)

      case ExecFailure(s, a, e1, t) =>
        self.logger.error(s"""Failed Statement Execution:
                             |
                             |  ${s.linesIterator.dropWhile(_.trim.isEmpty).mkString("\n  ")}
                             |
                             | arguments = [${a.mkString(", ")}]
                             |   elapsed = ${e1.toMillis.toString} ms exec (failed)
                             |   failure = ${t.getMessage}
          """.stripMargin)
    }
  }
}
