package tr.megevera.pholi.db

import java.sql.{ SQLWarning, Struct, PreparedStatement, SQLException, CallableStatement, Clob }
import java.sql.{ DatabaseMetaData, Savepoint, NClob, SQLXML, Blob, Statement, Connection}
import java.util
import java.util.Properties
import java.util.concurrent.Executor

import org.slf4j.LoggerFactory

import scala.collection.mutable.ListBuffer

private[db] object TrackingConnection {

  private val logger = LoggerFactory.getLogger(getClass)

  def apply(connection: Connection): Connection = new TrackingConnection(connection)
  def apply(connectionProvider: ConnectionProvider): Connection = new TrackingConnection(connectionProvider.connection)

}

private[db] class TrackingConnection(conn: Connection) extends Connection {

  import tr.megevera.pholi.db.TrackingConnection.logger

  private val statements = ListBuffer[Statement]()

  private def trackStatement[T <: Statement](f: => T): T = {
    val statement = f
    logger.debug(s"adding statement: $statement")
    statements += statement
    logger.debug(s"statements=$statements")
    statement
  }

  override def createStatement(): Statement = trackStatement {
    conn.createStatement()
  }

  override def setAutoCommit(p1: Boolean): Unit = conn.setAutoCommit(p1)

  override def setHoldability(p1: Int): Unit = conn.setHoldability(p1)

  override def clearWarnings(): Unit = conn.clearWarnings()

  override def createBlob(): Blob = conn.createBlob()

  override def createSQLXML(): SQLXML = conn.createSQLXML()

  override def getTransactionIsolation: Int = conn.getTransactionIsolation

  override def createNClob(): NClob = conn.createNClob()

  override def setSavepoint(): Savepoint = conn.setSavepoint()

  override def setSavepoint(p1: String): Savepoint = conn.setSavepoint(p1)

  override def getClientInfo(p1: String): String = conn.getClientInfo(p1)

  override def getClientInfo: Properties = conn.getClientInfo

  override def getMetaData: DatabaseMetaData = conn.getMetaData

  override def getTypeMap: util.Map[String, Class[_]] = conn.getTypeMap

  override def rollback(): Unit = conn.rollback()

  override def rollback(p1: Savepoint): Unit = conn.rollback(p1)

  override def createStatement(p1: Int, p2: Int): Statement = trackStatement {
    conn.createStatement(p1, p2)
  }

  override def createStatement(p1: Int, p2: Int, p3: Int): Statement = trackStatement {
    conn.createStatement(p1, p2, p3)
  }

  override def getHoldability: Int = conn.getHoldability

  override def setReadOnly(p1: Boolean): Unit = conn.setReadOnly(p1)

  override def setClientInfo(p1: String, p2: String): Unit = conn.setClientInfo(p1, p2)

  override def setClientInfo(p1: Properties): Unit = conn.setClientInfo(p1)

  override def setTypeMap(p1: util.Map[String, Class[_]]): Unit = conn.setTypeMap(p1)

  override def isReadOnly: Boolean = conn.isReadOnly

  override def getCatalog: String = conn.getCatalog

  override def createClob(): Clob = conn.createClob()

  override def setTransactionIsolation(p1: Int): Unit = conn.setTransactionIsolation(p1)

  override def nativeSQL(p1: String): String = conn.nativeSQL(p1)

  override def prepareCall(p1: String): CallableStatement = trackStatement {
    conn.prepareCall(p1)
  }

  override def prepareCall(p1: String, p2: Int, p3: Int): CallableStatement = trackStatement {
    conn.prepareCall(p1, p2, p3)
  }

  override def prepareCall(p1: String, p2: Int, p3: Int, p4: Int): CallableStatement = trackStatement {
    conn.prepareCall(p1, p2, p3, p4)
  }

  override def createArrayOf(p1: String, p2: Array[AnyRef]): java.sql.Array = conn.createArrayOf(p1, p2)

  override def setCatalog(p1: String): Unit = conn.setCatalog(p1)

  override def getAutoCommit: Boolean = conn.getAutoCommit

  override def close(): Unit = {
    logger.debug("closing all tracked statements")
    statements foreach { statement =>

      try {
        statement.close()
      } catch {
        case e: SQLException => {
          logger.error(e.getMessage, e)
        }
      }

    }
    logger.debug("clearing statements")
    statements.clear()
    logger.debug("closing underlying connection")
    conn.close()
  }

  override def prepareStatement(p1: String): PreparedStatement = trackStatement {
    conn.prepareStatement(p1)
  }

  override def prepareStatement(p1: String, p2: Int, p3: Int): PreparedStatement = trackStatement {
    conn.prepareStatement(p1, p2, p3)
  }

  override def prepareStatement(p1: String, p2: Int, p3: Int, p4: Int): PreparedStatement = trackStatement {
    conn.prepareStatement(p1, p2, p3, p4)
  }

  override def prepareStatement(p1: String, p2: Int): PreparedStatement = trackStatement {
    conn.prepareStatement(p1, p2)
  }

  override def prepareStatement(p1: String, p2: Array[Int]): PreparedStatement = trackStatement {
    conn.prepareStatement(p1, p2)
  }

  override def prepareStatement(p1: String, p2: Array[String]): PreparedStatement = trackStatement {
    conn.prepareStatement(p1, p2)
  }

  override def isValid(p1: Int): Boolean = conn.isValid(p1)

  override def releaseSavepoint(p1: Savepoint): Unit = conn.releaseSavepoint(p1)

  override def isClosed: Boolean = conn.isClosed

  override def createStruct(p1: String, p2: Array[AnyRef]): Struct = conn.createStruct(p1, p2)

  override def getWarnings: SQLWarning = conn.getWarnings

  override def commit(): Unit = conn.commit()

  override def unwrap[T](p1: Class[T]): T = conn.unwrap(p1)

  override def isWrapperFor(p1: Class[_]): Boolean = conn.isWrapperFor(p1)

  override def setSchema(schema: String): Unit = conn.setSchema(schema)

  override def getNetworkTimeout: Int = conn.getNetworkTimeout

  override def getSchema: String = conn.getSchema

  override def setNetworkTimeout(executor: Executor, milliseconds: Int): Unit = conn.setNetworkTimeout(executor, milliseconds)

  override def abort(executor: Executor): Unit = conn.abort(executor)

}
