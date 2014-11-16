package tr.megevera.pholi

import java.sql.ResultSet

import interpolations._
import tr.megevera.pholi.db.{Db, ConnectionProvider}

/**
 * Created by alaym on 16.11.2014.
 */
package object shortcuts {

  implicit class SqlShortcuts(val sc: StringContext) extends AnyVal {

    def li[T](args: Any *)(implicit connectionProvider: ConnectionProvider, converter: ResultSet => T): List[T] = Db.op { implicit conn =>

      sc.list(args)

    }

    def ex(args: Any *)(implicit connectionProvider: ConnectionProvider): Int = Db.op { implicit c =>

      sc.exc(args)

    }

    def se[T](args: Any *)(implicit connectionProvider: ConnectionProvider, converter: ResultSet => T): Seq[T] = Db.op { implicit conn =>

      sc.seq(args)

    }

    def m[A, B](args: Any *)(implicit connectionProvider: ConnectionProvider, converter: ResultSet => (A, B)): Map[A, B] = Db.op { implicit conn =>

      sc.map(args)

    }

    def o[T](args: Any *)(implicit connectionProvider: ConnectionProvider, converter: ResultSet => T): Option[T] = Db.op { implicit conn =>

      sc.opt(args)

    }

    def g[T](args: Any *)(implicit connectionProvider: ConnectionProvider, converter: ResultSet => T): T = Db.op { implicit conn =>

      sc.get(args)

    }

  }

}
