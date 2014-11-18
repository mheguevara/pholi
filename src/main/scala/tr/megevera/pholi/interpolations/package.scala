package tr.megevera.pholi

import java.sql.{Timestamp, PreparedStatement, ResultSet, Connection}
import java.util.Date
import org.slf4j.LoggerFactory
import resultsetextensions._

package object interpolations {

  private val logger = LoggerFactory.getLogger(getClass)

  implicit class SqlInterpolations(val sc: StringContext) extends AnyVal {

    private def setParameter(ps: PreparedStatement, param: Any, pi: Int): PreparedStatement = {

      logger.trace(s"setting param $param in index $pi in prepared statement $ps")

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
        setParameters(
          setParameter(ps, args.head, pi),
          args.tail,
          pi + 1
        )
      }

    }

    def pst(args: Any *)(implicit connection: Connection): PreparedStatement = {

      val sqlCommand = sc.parts.mkString("?").stripMargin

      logger.debug(s"preparing statement with sql command: $sqlCommand")
      val preparedStatement = connection.prepareStatement(sqlCommand)

      setParameters(preparedStatement, args)

    }

    def q(args: Any *)(implicit connection: Connection): ResultSet = {
      if (args.isEmpty) {

        logger.debug(s"creating statement with ${sc.parts.head} and executing query")
        connection.createStatement().executeQuery(sc.parts.head)

      } else {
        pst(args: _*)(connection).executeQuery()
      }
    }

    def exc(args: Any *)(implicit connection: Connection): Int = {
      if (args.isEmpty) {

        logger.debug(s"creating statement with ${sc.parts.head} and executing update")
        connection.createStatement().executeUpdate(sc.parts.head)

      } else {
        pst(args: _*)(connection).executeUpdate()
      }
    }

    def list[T](args: Any *)(implicit connection: Connection, converter: ResultSet => T): List[T] = q(args: _*)(connection).toList

    def seq[T](args: Any *)(implicit connection: Connection, converter: ResultSet => T): Seq[T] = q(args: _*)(connection).toList(converter).toSeq

    def map[A, B](args: Any *)(implicit connection: Connection, converter: ResultSet => (A, B)): Map[A, B] = q(args: _*)(connection).toMap

    def opt[T](args: Any *)(implicit connection: Connection, converter: ResultSet => T): Option[T] = q(args: _*)(connection).toOption

    def get[T](args: Any *)(implicit connection: Connection, converter: ResultSet => T): T = opt(args: _*)(connection, converter).getOrElse(throw new NoSuchElementException)

  }

}
