package com.edubigdata.supportutility.main

import com.edubigdata.supportutility.count.CountUtilityImpl
import com.edubigdata.supportutility.util.Logging
import org.apache.log4j.Logger


/**
 * Created by deokishore on 04/07/2016.
 */
object CountUtilityMain {

  val LOGGER: Logger = Logger.getLogger(classOf[SupportUtilityMain])

  // file:////Users/deokishore/Downloads/Risks.xml Fantasy <risk> </risk>



  def main(args: Array[String]): Unit = {

    LOGGER.info(" **** Started Count Utility Main : ")

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

    val countUtility = new CountUtilityImpl
    try {
       if(args.length == 4){
        countUtility.countInXML(args(0), args(1), args(2), args(3))
      }
    } catch {
      case ex: Exception => {
        LOGGER.error(" Error while counting, process was not successful ")
        throw new RuntimeException(ex)
      }
    }
    LOGGER.info(" **** Finished Successfully Count Utility Main : ")
  }

  class SupportUtilityMain extends Logging {
    final def logger = CountUtilityMain.LOGGER
  }

}
