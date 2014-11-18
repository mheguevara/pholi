package tr.megevera.pholi.db

import java.sql.Connection
import java.sql.Statement
import java.sql.Blob
import java.sql.SQLXML
import java.sql.NClob
import java.sql.Savepoint
import java.sql.DatabaseMetaData
import java.sql.Clob
import java.sql.CallableStatement
import java.sql.PreparedStatement
import java.sql.Struct
import java.sql.SQLWarning
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

  import TrackingConnection.logger

  private val statements = ListBuffer[Statement]()

  private def archiveStatement[T <: Statement](f: => T): T = {
    val statement = f
    logger.debug(s"adding statement: $statement")
    statements += statement
    logger.debug(s"statements=$statements")
    statement
  }

  override def createStatement(): Statement = archiveStatement {
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

  override def createStatement(p1: Int, p2: Int): Statement = archiveStatement {
    conn.createStatement(p1, p2)
  }

  override def createStatement(p1: Int, p2: Int, p3: Int): Statement = archiveStatement {
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

  override def prepareCall(p1: String): CallableStatement = archiveStatement {
    conn.prepareCall(p1)
  }

  override def prepareCall(p1: String, p2: Int, p3: Int): CallableStatement = archiveStatement {
    conn.prepareCall(p1, p2, p3)
  }

  override def prepareCall(p1: String, p2: Int, p3: Int, p4: Int): CallableStatement = archiveStatement {
    conn.prepareCall(p1, p2, p3, p4)
  }

  override def createArrayOf(p1: String, p2: Array[AnyRef]): java.sql.Array = conn.createArrayOf(p1, p2)

  override def setCatalog(p1: String): Unit = conn.setCatalog(p1)

  override def getAutoCommit: Boolean = conn.getAutoCommit

  override def close(): Unit = {
    logger.debug("closing all tracked statements")
    statements.foreach(_.close())
    logger.debug("clearing statements")
    statements.clear()
    logger.debug("closing underlying connection")
    conn.close()
  }

  override def prepareStatement(p1: String): PreparedStatement = archiveStatement {
    conn.prepareStatement(p1)
  }

  override def prepareStatement(p1: String, p2: Int, p3: Int): PreparedStatement = archiveStatement {
    conn.prepareStatement(p1, p2, p3)
  }

  override def prepareStatement(p1: String, p2: Int, p3: Int, p4: Int): PreparedStatement = archiveStatement {
    conn.prepareStatement(p1, p2, p3, p4)
  }

  override def prepareStatement(p1: String, p2: Int): PreparedStatement = archiveStatement {
    conn.prepareStatement(p1, p2)
  }

  override def prepareStatement(p1: String, p2: Array[Int]): PreparedStatement = archiveStatement {
    conn.prepareStatement(p1, p2)
  }

  override def prepareStatement(p1: String, p2: Array[String]): PreparedStatement = archiveStatement {
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
