package tr.megevera.pholi.db

import org.specs2.mutable.Specification
import tr.megevera.pholi.test.TestContext

/**
 * Created by alaym on 16.11.2014.
 */
class TrackingConnectionSpec extends Specification {

  "TrackingConnection" should {

    "close all statements when closed" in new TestContext {

      val tc = TrackingConnection(connectionProvider)

      val s = tc.createStatement()
      val ps = tc.prepareStatement("select 1")
      val cs = tc.prepareCall("select 1")

      tc.close()

      s.isClosed must beEqualTo(true)
      ps.isClosed must beEqualTo(true)
      cs.isClosed must beEqualTo(true)


    }

  }

}
