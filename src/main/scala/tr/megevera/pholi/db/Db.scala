package tr.megevera.pholi.db

import java.sql.Connection

import org.slf4j.LoggerFactory
import scala.util.{Failure, Success}
import scala.util.control.{Exception => MException}

object Db {

  private val logger = LoggerFactory.getLogger(getClass)

  def op[T](f: Connection => T)(implicit connectionProvider: ConnectionProvider): T = {

    val connection = TrackingConnection(connectionProvider)

    MException.
      allCatch[T].
      andFinally {

        connection.close()

      }.withTry {

        connection.setAutoCommit(true)
        f(connection)

      } match {

        case Success(result) => result

        case Failure(throwable) => {

          logger.error(throwable.getMessage, throwable)
          throw throwable

        }

      }

  }

  def trx[T](f: Connection => T)(implicit connectionProvider: ConnectionProvider): T = {

    val connection = TrackingConnection(connectionProvider)

    MException.
      allCatch[T].
      andFinally {

        connection.close()

      }.withTry {

        connection.setAutoCommit(false)
        val result = f(connection)
        connection.commit()
        result

      } match {

        case Success(result) => result

        case Failure(throwable) => {

          logger.error(throwable.getMessage, throwable)
          connection.rollback()
          throw throwable

        }

      }

  }

}
