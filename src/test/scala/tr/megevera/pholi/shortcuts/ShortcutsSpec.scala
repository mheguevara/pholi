package tr.megevera.pholi.shortcuts

import org.specs2.mutable.Specification
import tr.megevera.pholi.db.Db
import tr.megevera.pholi.shortcuts._
import tr.megevera.pholi.test.TestCases.User
import tr.megevera.pholi.test.{TestCases, TestContext}

/**
 * Created by alaym on 24/11/14.
 */
class ShortcutsSpec extends Specification {

  "Shortcuts" should {

    "behave correctly when using li" in new TestContext {

      TestCases.createUsersTable
      TestCases.insertUsers(List(User(0, "m", "e"), User(1, "s", "e")))

      val users: List[User] = li"select id, username, email from users"

      val expected = List(User(1, "m", "e"), User(2, "s", "e"))

      users must beEqualTo(expected)

    }

    "behave correctly when using se" in new TestContext {

      TestCases.createUsersTable
      TestCases.insertUsers(List(User(0, "m", "e"), User(1, "s", "e")))

      val users: Seq[User] = se"select id, username, email from users"

      val expected = Seq(User(1, "m", "e"), User(2, "s", "e"))

      users must beEqualTo(expected)

    }

    "behave correctly when using m" in new TestContext {

      TestCases.createUsersTable
      TestCases.insertUsers(List(User(0, "m", "e"), User(1, "s", "e")))

      val users: Map[Int, User] = m"select id, username, email from users"

      val expected = Map(1 -> User(1, "m", "e"), 2 -> User(2, "s", "e"))

      users must beEqualTo(expected)

    }

    "behave correctly when using o" in new TestContext {

      TestCases.createUsersTable
      TestCases.insertUsers(List(User(0, "m", "e"), User(1, "s", "e")))

      def users(id: Int): Option[User] = o"select id, username, email from users where id = $id"

      val expectedUser1 = Some(User(1, "m", "e"))
      val expectedUser3 = None

      users(1) must beEqualTo(expectedUser1)
      users(3) must beEqualTo(None)

    }

  }

}
