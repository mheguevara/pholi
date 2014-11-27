package tr.megevera.pholi.test

import java.sql.{Connection, DriverManager}
import java.util.UUID

import org.specs2.execute.{AsResult, Result}
import org.specs2.mutable.Around
import org.specs2.specification.Scope
import tr.megevera.pholi.db.ConnectionProvider

/**
 * Created by alaym on 16.11.2014.
 */
abstract class TestContext extends Around with Scope {

  Class.forName("org.h2.Driver")

  private val dbId = UUID.randomUUID().toString

  private val url = s"jdbc:h2:mem:$dbId;DB_CLOSE_DELAY=-1;MODE=MySQL"

  implicit val connectionProvider: ConnectionProvider = new ConnectionProvider {
    override def connection: Connection = DriverManager.getConnection(url, "sa", "")
  }

  def around[T: AsResult](t: => T): Result = AsResult.effectively(t)


}
