package com.edubigdata.supportutility.main

import com.edubigdata.supportutility.search.SearchUtilityImpl
import com.edubigdata.supportutility.util.Logging
import org.apache.log4j.Logger


/**
 * Created by deokishore on 04/07/2016.
 */
object SearchUtilityMain {

  private val LOGGER: Logger = Logger.getLogger(classOf[SupportUtilityMain])

  def main(args: Array[String]): Unit = {

    if (args.length < 2) {
      LOGGER.info(" Please provide valid path for avro file for processing..... : ")

      LOGGER.info("spark-submit "
        + "--master yarn "
        + "--deploy-mode cluster "
        + "--num-executors 100 "
        + "--queue ecpqedlinkeddata "
        + "--class com.thomsonreuters.bold.main.SupportUtilityMain "
        + "support-utility-assembly-1.0.jar"
        + "args[0] -> Input Path "
        + "args[1] -> Search Text "
        + "args[2] -> xml start tag  [if it is xml file]"
        + "args[3] -> xml end tag [if it is xml file]")
      System.exit(0)
    }

    val supportUtility = new SearchUtilityImpl
    try {
     if (args.length == 4) {
        supportUtility.searchInXML(args(0), args(1), args(2), args(3))
      }
    } catch {
      case ex: Exception => {
        LOGGER.error(" Error while searching, process was not successful ", ex)
      }
    }
  }

  class SupportUtilityMain extends Logging {
    final def logger = SearchUtilityMain.LOGGER
  }

}
