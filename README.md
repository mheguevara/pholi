### Pholi

A very simple, fun to use jdbc library which heavily relies on implicits and interpolations

#### How to use

* Wrap database operations using Db helper object. Statements created using connection are closed automatically when operation is done:

```scala
import tr.megevera.pholi.db.Db
import tr.megevera.pholi.db.ConnectionProvider
import java.sql.Connection

implicit val connectionProvider = new ConnectionProvider {
  def connection: Connection = // get a database connection from somewhere
}

Db.op { connection =>

  val statement = connection.createStatement()
  statement.execute("delete from users")

}

Db.trx { connection =>

  val statement = connection.createStatement()
  statement.execute("delete from users")
  statement.execute("delete from posts")

}

```

* Use interpolations to retrieve data and populate objects. See Interpolations for all available interpolators. 

```scala
import tr.megevera.pholi.db._
import tr.megevera.pholi.interpolations._
import java.sql.ResultSet

case class User(id: Int, name: String, age: Int)
object User {
  implicit def rowToUser(resultSet: ResultSet): User = User(
    resultSet.getInt("id"),
    resultSet.getString("name"),
    resultSet.getInt("age")
  )
}

implicit val connectionProvider: ConnectionProvider = // create a connection provider to get database connection from somewhere

def usersLong: List[User] = Db.op { implicit connection =>
  val pst = pst"select id, name, age from users limit 100"
  val rs = pst.executeQuery()
  var result = List.empty[User]
  while(rs.next) { result = User.rowToUser(rs) :: result }
  result
}

def users: List[User] = Db.op { implicit connection =>
  list"select id, name, age from users limit 100"
}

def userOpt(id: Int): Option[User] = Db.op { implicit connection =>
  opt"select id, name, age from users where id = $id"
}

def user(id: Int): User = Db.op { implicit connection =>
  get"select id, name, age from users where id = $id"
}

implicit def rowToIdToUser(resultSet: ResultSet): (Int, User) = Db.op {
  val id = resultSet.getInt("id")
  val name = resultSet.getInt("id")
  val age = resultSet.getInt("id")
  id -> User(id, name, age)
}
def idsToUser: Map[Int, User] = Db.op { implicit connection =>
  map"select id, name, age from users limit 100"
}


```

* Use shortcut interpolations to retrieve data and populate objects. See shortcuts for all available shortcuts. 

```scala
import tr.megevera.pholi.db._
import tr.megevera.pholi.shortcuts._
import java.sql.ResultSet

case class User(id: Int, name: String, age: Int)
object User {
  implicit def rowToUser(resultSet: ResultSet): User = User(
    resultSet.getInt("id"),
    resultSet.getString("name"),
    resultSet.getInt("age")
  )
}

implicit val connectionProvider: ConnectionProvider = // create a connection provider to get database connection from somewhere

def users: List[User] = li"select id, name, age from users limit 100"

def userOpt(id: Int): Option[User] = o"select id, name, age from users where id = $id"

def user(id: Int): User = g"select id, name, age from users where id = $id"

implicit def rowToIdToUser(resultSet: ResultSet): (Int, User) = Db.op {
  val id = resultSet.getInt("id")
  val name = resultSet.getInt("id")
  val age = resultSet.getInt("id")
  id -> User(id, name, age)
}
def idsToUser: Map[Int, User] = m"select id, name, age from users limit 100"

```
* Use result set extensions to extend ResultSet api. See ResultSet extensions for all available extensions:

```scala
import tr.megevera.pholi.db._
import tr.megevera.pholi.shortcuts._
import java.sql.ResultSet

case class User(id: Int, name: String, age: Int)
object User {
  implicit def rowToUser(resultSet: ResultSet): User = User(
    resultSet.getInt("id"),
    resultSet.getString("name"),
    resultSet.getInt("age")
  )
}

implicit val connectionProvider: ConnectionProvider = // create a connection provider to get database connection from somewhere

def users: List[User] = Db.op { implicit connection =>
  val rs = pst"select id, name, age from users limit 100".executeQuery()
  rs.toList
}

```

#### Interpolations
TODO

#### Shortcuts
TODO

#### ResultSet Extensions
TODO
