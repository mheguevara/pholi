package tr.megevera.pholi.db

import java.sql.Connection

object Db {

  def op[T](f: Connection => T)(implicit connectionProvider: ConnectionProvider): T = ???

  def trx[T](f: Connection => T)(implicit connectionProvider: ConnectionProvider): T = ???

}
