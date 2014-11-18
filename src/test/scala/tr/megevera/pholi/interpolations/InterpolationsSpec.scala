package tr.megevera.pholi.interpolations

import java.sql.ResultSet

import org.specs2.mutable.Specification
import tr.megevera.pholi.db.Db
import tr.megevera.pholi.test.TestCases.User
import tr.megevera.pholi.test.{TestCases, TestContext}
import tr.megevera.pholi.resultsetextensions._

/**
 * Created by alaym on 18.11.2014.
 */
class InterpolationsSpec extends Specification {

  "Interpolatios" should {

    "behave correctly when using list" in new TestContext {

      TestCases.createUsersTable
      TestCases.insertUsers(List(User(0, "m", "e"), User(1, "s", "e")))

      implicit val rsToUser: ResultSet => User = { rs =>
        User(rs.getInt("id"), rs.getString("username"), rs.getString("email"))
      }

      val users: List[User] = Db.op { implicit c =>
        list"select id, username, email from users"
      }

      val expected = List(User(1, "m", "e"), User(2, "s", "e"))

      users must beEqualTo(expected)

    }

    "behave correctly when using seq" in new TestContext {

      TestCases.createUsersTable
      TestCases.insertUsers(List(User(0, "m", "e"), User(1, "s", "e")))

      implicit val rsToUser: ResultSet => User = { rs =>
        User(rs.getInt("id"), rs.getString("username"), rs.getString("email"))
      }

      val users: Seq[User] = Db.op { implicit c =>
        seq"select id, username, email from users"
      }

      val expected = Seq(User(1, "m", "e"), User(2, "s", "e"))

      users must beEqualTo(expected)

    }

    "behave correctly when using map" in new TestContext {

      TestCases.createUsersTable
      TestCases.insertUsers(List(User(0, "m", "e"), User(1, "s", "e")))

      implicit val rsToUser: ResultSet => (Int, User) = { rs =>
        rs.getInt("id") -> User(rs.getInt("id"), rs.getString("username"), rs.getString("email"))
      }

      val users: Map[Int, User] = Db.op { implicit c =>
        map"select id, username, email from users"
      }

      val expected = Map(1 -> User(1, "m", "e"), 2 -> User(2, "s", "e"))

      users must beEqualTo(expected)

    }

    "behave correctly when using opt" in new TestContext {

      TestCases.createUsersTable
      TestCases.insertUsers(List(User(0, "m", "e"), User(1, "s", "e")))

      implicit val rsToUser: ResultSet => User = { rs =>
        User(rs.getInt("id"), rs.getString("username"), rs.getString("email"))
      }

      def users(id: Int): Option[User] = Db.op { implicit c =>
        opt"select id, username, email from users where id = $id"
      }

      val expectedUser1 = Some(User(1, "m", "e"))
      val expectedUser3 = None

      users(1) must beEqualTo(expectedUser1)
      users(3) must beEqualTo(None)

    }

    "behave correctly when using pst" in new TestContext {

      TestCases.createUsersTable
      TestCases.insertUsers(List(User(0, "m", "e"), User(1, "s", "e")))

      val expectedRowCount = Db.op { implicit c =>

        val p = pst"delete from users"
        p.executeUpdate()

      }

      expectedRowCount must beEqualTo(2)

    }

    "behave correctly when using q" in new TestContext {

      TestCases.createUsersTable
      TestCases.insertUsers(List(User(0, "m", "e"), User(1, "s", "e")))

      implicit val rsToUser: ResultSet => User = { rs =>
        User(rs.getInt("id"), rs.getString("username"), rs.getString("email"))
      }

      val users: List[User] = Db.op { implicit c =>
        q"select id, username, email from users".toList
      }

      val expected = List(User(1, "m", "e"), User(2, "s", "e"))

      users must beEqualTo(expected)

    }

    "behave correctly when using exc" in new TestContext {

      TestCases.createUsersTable
      TestCases.insertUsers(List(User(0, "m", "e"), User(1, "s", "e")))

      val expectedRowCount = Db.op { implicit c =>

        exc"delete from users"

      }

      expectedRowCount must beEqualTo(2)

    }

  }

}
