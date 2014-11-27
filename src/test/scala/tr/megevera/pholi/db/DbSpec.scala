package tr.megevera.pholi.db

import java.sql.Connection

import org.specs2.mutable.Specification
import tr.megevera.pholi.test.{TestCases, TestContext}

/**
 * Created by alaym on 16.11.2014.
 */
class DbSpec extends Specification {

  "Db" should {

    "close connection when using op" in new TestContext {

      val c = connectionProvider.connection

      Db.op(_ => ())(new ConnectionProvider {
        override def connection: Connection = c
      })

      c.isClosed must beEqualTo(true)

    }

    "close connection when using trx" in new TestContext {

      val c = connectionProvider.connection

      Db.trx(_ => ())(new ConnectionProvider {
        override def connection: Connection = c
      })

      c.isClosed must beEqualTo(true)

    }

    "commit changes when using trx without any errors" in new TestContext {

      TestCases.createUsersTable

      Db.trx { c =>

        val pst = c.prepareStatement("insert into users(username, email) values (?,?)")
        pst.setString(1, "m")
        pst.setString(2, "a")
        pst.execute()
        pst.setString(1, "s")
        pst.setString(2, "e")
        pst.execute()

      }

      var rowCount = 0
      val c = connectionProvider.connection
      val s = c.createStatement()
      val rs = s.executeQuery("select * from users")
      while(rs.next()) {
        rowCount += 1
      }
      rs.close()
      s.close()
      c.close()

      rowCount must beEqualTo(2)

    }

    "rollback changes when using trx with some errors" in new TestContext {

      TestCases.createUsersTable

      try {
        Db.trx { c =>

          val pst = c.prepareStatement("insert into users(username, email) values (?,?)")
          pst.setString(1, "m")
          pst.setString(2, "a")
          pst.execute()
          pst.setString(1, "s")
          pst.setString(2, "e")
          throw new Exception()
          pst.execute()

        }
      } catch {
        case e: Exception =>
      }

      var rowCount = 0
      val c = connectionProvider.connection
      val s = c.createStatement()
      val rs = s.executeQuery("select * from users")
      while(rs.next()) {
        rowCount += 1
      }
      rs.close()
      s.close()
      c.close()

      rowCount must beEqualTo(0)

    }

  }

}
