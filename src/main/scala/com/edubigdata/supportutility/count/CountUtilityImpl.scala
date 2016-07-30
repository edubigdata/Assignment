package com.edubigdata.supportutility.count

import java.nio.charset.StandardCharsets

import com.edubigdata.supportutility.exception.{CUDOutputException, SequenceFileException}
import com.edubigdata.supportutility.util.XmlInputFormat
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.io.{BytesWritable, LongWritable, Text}
import org.apache.log4j.Logger
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.DataFrame

/**
 * Created by deokishore on 07/07/2016.
 */
class CountUtilityImpl extends  CountUtility {

  private val LOGGER: Logger = Logger.getLogger(classOf[CountUtilityImpl])


  override def countInXML(fileLocation: String, searchText: String, startTag: String, endTag: String): Unit = {
    LOGGER.info(" **** Count in XML  ****  ")
    try {
          val conf: Configuration = getConfiguration(startTag, endTag)
          val rawXml = sparkContext.newAPIHadoopFile(fileLocation, classOf[XmlInputFormat], classOf[LongWritable],  classOf[Text], conf)
          if(searchText != null) {
            val xmlRDD = rawXml.filter( xml => xml._2.toString.toLowerCase().contains(searchText.toLowerCase)).map(xml => xml._2)
            sparkContext.parallelize(xmlRDD.count().toString).saveAsTextFile(hdfsOutputPath)
          } else {
            val xmlRDD = rawXml.filter( xml => xml._2.toString.toLowerCase().contains(searchText.toLowerCase)).map(xml => xml._2)
            sparkContext.parallelize(xmlRDD.count().toString).saveAsTextFile(hdfsOutputPath)
          }
//        val finalPath = hdfsOutputPath + "/final/";
//        merge(hdfsOutputPath, finalPath, "countResult.txt");
    } catch {
        case ex: Exception => {
          LOGGER.error(" **** Exception in XML counting, trying to search in Seq Format ", ex)
          countInSeqXML(fileLocation, searchText, startTag, endTag)
        }
        case ex: Exception => {
          val message = " Error in count " + ex
          LOGGER.error(message)
          throw new scala.RuntimeException(message)
        };
    }
  }

  def countInSeqXML(hdfsFileLocation: String, searchText: String, startTag: String, endTag: String) : Unit = {
      try {
        val conf: Configuration = getConfiguration(startTag, endTag)
        val rawXmls:RDD[(LongWritable, Text)] = sparkContext.newAPIHadoopFile(hdfsFileLocation, classOf[XmlInputFormat], classOf[LongWritable],  classOf[Text], conf)
        val xmlRDD:RDD[(Text)] = rawXmls.filter( p => p._2.toString.toLowerCase().contains(searchText.toLowerCase)).map(p => p._2)
        sparkContext.parallelize(xmlRDD.count().toString).saveAsTextFile(hdfsOutputPath)
//      val finalPath = hdfsOutputPath + "/final/";
//      merge(hdfsOutputPath, finalPath, "countResult.txt");
      } catch {
        case ex: Exception => {
          val message = "Error in countInSequenceFile "
          LOGGER.error(message, ex)
          throw new SequenceFileException("Error in Sequence File")
        }
      }
  }


  def handleCUDOutputFormat(fileLocation: String, searchText: String): Unit = {
    LOGGER.info(" **** Searching in CUD Output Format ")
    try {
      countInCUDOutput(fileLocation, searchText)
    } catch {
      case cudOEx: CUDOutputException => {
        LOGGER.info(" **** Exception in CUD Output, trying to search in Seq Format ")
        handleSequenceFileFormat(fileLocation, searchText)
      }
      case ex: Exception => {
        val message = " Error in count " + ex
        LOGGER.error(message)
        throw new scala.RuntimeException(message)
      };
    }
  }

  def handleSequenceFileFormat(fileLocation: String, word: String): Unit = {
    LOGGER.info(" **** Searching in Sequence File Format ")
    try {
      countInSequenceFile(fileLocation, word)
    } catch {
      case seqFileEx: SequenceFileException => {
        val message = " **** Caught Sequence File Exception, not trying anymore " + seqFileEx
        LOGGER.error(message)
        throw new scala.RuntimeException(message)
      };
    }
  }


  @throws(classOf[CUDOutputException])
  private def countInCUDOutput(hdfsLocation:String, searchText:String): Unit = {
    LOGGER.info(" **** Count In CUD Output ")
    val fileRDD = sparkContext.sequenceFile(hdfsLocation, classOf[Text], classOf[BytesWritable])
    try {
      val rowRDD = fileRDD.map { case (k, v) => (k.toString(), new Predef.String(v.getBytes, StandardCharsets.UTF_8)) }
      val result: DataFrame = search(searchText, rowRDD)
      saveResult(result);
    } catch {
      case ex: Exception => {
        val message = "Error in countInCUDOutput "
        LOGGER.error(message, ex)
        throw new CUDOutputException("Error in CUD output")
      };
    }
  }

  @throws(classOf[SequenceFileException])
  private def countInSequenceFile(hdfsFileLocation:String, searchText:String): Unit = {
    LOGGER.info(" **** Searching in sequence file: " + " File Location : " + hdfsFileLocation + " Word to search : " + searchText)

    val fileRDD = sparkContext.sequenceFile(hdfsFileLocation, classOf[Text], classOf[BytesWritable])
    try {
      val rowRDD = fileRDD.map { case (k, v) => (k.toString(), new Predef.String(v.getBytes, StandardCharsets.UTF_8)) }
      val result: DataFrame = search(searchText, rowRDD)
      saveResult(result);
    } catch {
      case ex: Exception => {
        val message = "Error in countInSequenceFile "
        LOGGER.error(message, ex)
        throw new SequenceFileException("Error in Sequence File")
      };
    }
  }

  private def saveResult(result: DataFrame): Unit = {
    deleteFileIfExists(hdfsOutputPath)
    sparkContext.parallelize(result.count().toString).saveAsTextFile(hdfsOutputPath)
    val finalPath = hdfsOutputPath + "/final/";
    merge(hdfsOutputPath, finalPath, "supportUtility.txt");
  }


}
