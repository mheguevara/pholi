package tr.megevera.pholi

import java.sql.{Timestamp, PreparedStatement, ResultSet, Connection}
import java.util.Date
import resultsetextensions._

package object interpolations {

  implicit class SqlInterpolations(val sc: StringContext) extends AnyVal {

    private def setParameter(ps: PreparedStatement, param: Any, pi: Int): PreparedStatement = {

      param match {
        case s: String        => ps.setString(pi, s)
        case i: Int           => ps.setInt(pi, i)
        case d: Double        => ps.setDouble(pi, d)
        case l: Long          => ps.setLong(pi, l)
        case f: Float         => ps.setFloat(pi, f)
        case s: Short         => ps.setShort(pi, s)
        case t: Timestamp     => ps.setTimestamp(pi, t)
        case d: Date          => ps.setTimestamp(pi, new Timestamp(d.getTime))
        case b: Boolean       => ps.setBoolean(pi, b)
        case b: Byte          => ps.setByte(pi, b)
        case bs: Array[Byte]  => ps.setBytes(pi, bs)
      }

      ps

    }

    private def setParameters(ps: PreparedStatement, args: Seq[Any], pi: Int = 1): PreparedStatement = {

      if (args.isEmpty) {
        ps
      } else {
        setParameters(setParameter(ps, args.head, pi), args.tail, pi + 1)
      }

    }

    def pst(args: Any *)(implicit connection: Connection): PreparedStatement = {
      val sqlCommand = sc.parts.mkString("?").stripMargin
      val preparedStatement = connection.prepareStatement(sqlCommand)
      setParameters(preparedStatement, args)
    }

    def query(args: Any *)(implicit connection: Connection): ResultSet = {
      if (args.isEmpty) {
        connection.createStatement().executeQuery(sc.parts.head)
      } else {
        pst(args)(connection).executeQuery()
      }
    }

    def execute(args: Any *)(implicit connection: Connection): Int = {
      if (args.isEmpty) {
        connection.createStatement().executeUpdate(sc.parts.head)
      } else {
        pst(args)(connection).executeUpdate()
      }
    }

    def list[T](args: Any *)(implicit connection: Connection, converter: ResultSet => T): List[T] = query(args)(connection).toList

    def seq[T](args: Any *)(implicit connection: Connection, converter: ResultSet => T): Seq[T] = query(args)(connection).toSeq

    def map[A, B](args: Any *)(implicit connection: Connection, converter: ResultSet => (A, B)): Map[A, B] = query(args)(connection).toMap

    def opt[T](args: Any *)(implicit connection: Connection, converter: ResultSet => T): Option[T] = query(args)(connection).headOption

    def get[T](args: Any*)(implicit connection: Connection, converter: ResultSet => T): T = opt(args)(connection, converter).getOrElse(throw new NoSuchElementException)

  }

}
