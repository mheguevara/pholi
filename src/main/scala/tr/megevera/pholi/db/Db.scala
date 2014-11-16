package tr.megevera.pholi.db

import java.sql.Connection

import org.slf4j.LoggerFactory

object Db {

  private val logger = LoggerFactory.getLogger(getClass)

  def op[T](f: Connection => T)(implicit connectionProvider: ConnectionProvider): T = {

    val connection = TrackingConnection(connectionProvider)

    try {

      connection.setAutoCommit(true)
      f(connection)

    } catch {

      case e: Exception => {

        logger.error(e.getMessage, e)
        throw e

      }

    } finally {

      connection.close()

    }

  }

  def trx[T](f: Connection => T)(implicit connectionProvider: ConnectionProvider): T = {

    val connection = TrackingConnection(connectionProvider)

    try {

      connection.setAutoCommit(false)
      val result = f(connection)
      connection.commit()
      result

    } catch {

      case e: Exception => {

        logger.error(e.getMessage, e)
        connection.rollback()
        throw e

      }

    } finally {

      connection.close()

    }

  }

}
