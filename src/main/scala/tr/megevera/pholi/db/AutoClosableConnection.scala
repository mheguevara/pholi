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

import scala.collection.mutable.ListBuffer

private class AutoClosableConnection(conn: Connection) extends Connection {

  private val statements = ListBuffer[Statement]()

  private def archiveStatement(f: => Statement): Statement = ???

  override def createStatement(): Statement = ???

  override def setAutoCommit(p1: Boolean): Unit = ???

  override def setHoldability(p1: Int): Unit = ???

  override def clearWarnings(): Unit = ???

  override def createBlob(): Blob = ???

  override def createSQLXML(): SQLXML = ???

  override def getTransactionIsolation: Int = ???

  override def createNClob(): NClob = ???

  override def setSavepoint(): Savepoint = ???

  override def setSavepoint(p1: String): Savepoint = ???

  override def getClientInfo(p1: String): String = ???

  override def getClientInfo: Properties = ???

  override def getMetaData: DatabaseMetaData = ???

  override def getTypeMap: util.Map[String, Class[_]] = ???

  override def rollback(): Unit = ???

  override def rollback(p1: Savepoint): Unit = ???

  override def createStatement(p1: Int, p2: Int): Statement = ???

  override def createStatement(p1: Int, p2: Int, p3: Int): Statement = ???

  override def getHoldability: Int = ???

  override def setReadOnly(p1: Boolean): Unit = ???

  override def setClientInfo(p1: String, p2: String): Unit = ???

  override def setClientInfo(p1: Properties): Unit = ???

  override def setTypeMap(p1: util.Map[String, Class[_]]): Unit = ???

  override def isReadOnly: Boolean = ???

  override def getCatalog: String = ???

  override def createClob(): Clob = ???

  override def setTransactionIsolation(p1: Int): Unit = ???

  override def nativeSQL(p1: String): String = ???

  override def prepareCall(p1: String): CallableStatement = ???

  override def prepareCall(p1: String, p2: Int, p3: Int): CallableStatement = ???

  override def prepareCall(p1: String, p2: Int, p3: Int, p4: Int): CallableStatement = ???

  override def createArrayOf(p1: String, p2: Array[AnyRef]): java.sql.Array = ???

  override def setCatalog(p1: String): Unit = ???

  override def getAutoCommit: Boolean = ???

  override def close(): Unit = ???

  override def prepareStatement(p1: String): PreparedStatement = ???

  override def prepareStatement(p1: String, p2: Int, p3: Int): PreparedStatement = ???

  override def prepareStatement(p1: String, p2: Int, p3: Int, p4: Int): PreparedStatement = ???

  override def prepareStatement(p1: String, p2: Int): PreparedStatement = ???

  override def prepareStatement(p1: String, p2: Array[Int]): PreparedStatement = ???

  override def prepareStatement(p1: String, p2: Array[String]): PreparedStatement = ???

  override def isValid(p1: Int): Boolean = ???

  override def releaseSavepoint(p1: Savepoint): Unit = ???

  override def isClosed: Boolean = ???

  override def createStruct(p1: String, p2: Array[AnyRef]): Struct = ???

  override def getWarnings: SQLWarning = ???

  override def commit(): Unit = ???

  override def unwrap[T](p1: Class[T]): T = ???

  override def isWrapperFor(p1: Class[_]): Boolean = ???
}
