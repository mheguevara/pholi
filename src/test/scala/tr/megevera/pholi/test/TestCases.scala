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

  def insertUsers(users: List[User])(implicit cp: ConnectionProvider) = {

    val c = cp.connection
    val pst = c.prepareStatement("insert into users(username, email) values(?,?)")
    val bacth = users.foldLeft(pst) { (pst, user) =>

      pst.setString(1, user.username)
      pst.setString(2, user.email)
      pst.addBatch()
      pst

    }

    bacth.executeBatch()
    bacth.close()
    c.close()

  }

}
