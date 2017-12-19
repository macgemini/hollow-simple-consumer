package configs

import com.typesafe.config.ConfigFactory

/**
  * Created by mac on 28.02.17.
  */
case class Config(scratchDir: String)

object Config{
  def apply(): Config = {
    val configuration = ConfigFactory.load()
    val scratchDir = configuration.getString("scratchDir")
    new Config(scratchDir)
  }
}
