package tr.megevera.pholi.db

import java.sql.Connection

trait ConnectionProvider {

  def connection: Connection

}
