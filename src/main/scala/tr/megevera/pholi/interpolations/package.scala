package tr.megevera.pholi

import java.sql.{PreparedStatement, ResultSet, Connection}

package object interpolations {

  private class ResultSetIterator[T](rs: ResultSet, converter: ResultSet => T) extends Iterator[T] {

    override def hasNext: Boolean = rs.next()

    override def next(): T = converter(rs)

  }

  implicit class ResultSetExtensions(val rs: ResultSet) extends AnyVal {

    def it[T](implicit converter: ResultSet => T): Iterator[T] = new ResultSetIterator[T](rs, converter)
    def toList[T](implicit converter: ResultSet => T): List[T] = it(converter).toList
    def toSeq[T](implicit converter: ResultSet => T): Seq[T] = it(converter).toSeq

  }

  implicit class SqlInterpolations(val sc: StringContext) extends AnyVal {

    def pst(args: Any *)(implicit connection: Connection): PreparedStatement = ???

    def query(args: Any *)(implicit connection: Connection): ResultSet = ???

    def execute(args: Any *)(implicit connection: Connection): Int = ???

    def list[T](args: Any *)(implicit connection: Connection, converter: ResultSet => T): List[T] = ???

    def map[A, B](args: Any *)(implicit connection: Connection, converter: ResultSet => Tuple2[A, B]): Map[A, B] = ???

    def opt[T](args: Any *)(implicit connection: Connection, converter: ResultSet => T): Option[T] = ???

    def get[T](args: Any*)(implicit connection: Connection, converter: ResultSet => T): T = ???

  }

}
