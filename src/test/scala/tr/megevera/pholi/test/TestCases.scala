package tr.megevera.pholi.test

import tr.megevera.pholi.db.ConnectionProvider

/**
 * Created by alaym on 16.11.2014.
 */
object TestCases {

  case class User(id: Int, username: String, email: String)

  def createUsersTable(implicit cp: ConnectionProvider) = {
    val c = cp.connection
    val st = c.createStatement()
    st.execute(
      """
        |create table users(id int primary key auto_increment, username varchar(255), email varchar(255))
      """.stripMargin)
    st.close()
    c.close()
  }

}
