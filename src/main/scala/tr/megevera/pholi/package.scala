package tr.megevera

import java.util.concurrent.TimeUnit

import com.typesafe.config.ConfigFactory
import org.slf4j.LoggerFactory

/**
 * Created by alaym on 01/12/14.
 */
package object pholi {

  private val logger = LoggerFactory.getLogger("tr.megevera.pholi")

  val config = ConfigFactory.load().getConfig("pholi")

  logger.trace(s"pholi.config=$config")

  val statementQueryTimeout = config.getDuration("statement.queryTimeout", TimeUnit.SECONDS).toInt

}
