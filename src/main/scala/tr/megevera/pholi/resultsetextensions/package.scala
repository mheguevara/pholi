package tr.megevera.pholi

import java.sql.ResultSet

/**
 * Created by alaym on 16.11.2014.
 */
package object resultsetextensions {

  private class ResultSetIterator[T](rs: ResultSet, converter: ResultSet => T) extends Iterator[T] {

    override def hasNext: Boolean = rs.next()

    override def next(): T = converter(rs)

  }

  implicit class ResultSetExtensions(val rs: ResultSet) extends AnyVal {

    private def it[T](implicit converter: ResultSet => T): Iterator[T] = new ResultSetIterator[T](rs, converter)

    def toList[T](implicit converter: ResultSet => T): List[T] = it(converter).toList

    def toSeq[T](implicit converter: ResultSet => T): Seq[T] = it(converter).toSeq

    def toMap[A, B](implicit converter: ResultSet => (A, B)): Map[A, B] = it(converter).toMap

    def toOption[T](implicit converter: ResultSet => T): Option[T] = {
      val _it = it(converter)
      if (_it.hasNext) {
        Option(_it.next())
      } else {
        None
      }
    }

  }

}
