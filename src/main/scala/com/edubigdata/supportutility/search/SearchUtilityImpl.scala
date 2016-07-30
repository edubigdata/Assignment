package com.edubigdata.supportutility.search

import com.edubigdata.supportutility.util.XmlInputFormat
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.io.{LongWritable, Text}
import org.apache.log4j.Logger
import org.apache.spark.rdd.RDD


/**
 * Created by deokishore on 07/07/2016.
 */
class SearchUtilityImpl extends SearchUtility {

  private val LOGGER: Logger = Logger.getLogger(classOf[SearchUtilityImpl])

  override def searchInXML(fileLocation: String, searchText: String, startTag: String, endTag: String): Unit = {
    LOGGER.info(" **** Searching in xml.... ")
    try {
      val conf:Configuration = getConfiguration(startTag, endTag)
      val rawXmls:RDD[(LongWritable, Text)] = sparkContext.newAPIHadoopFile(fileLocation, classOf[XmlInputFormat], classOf[LongWritable],  classOf[Text], conf)

      val xmlRDD:RDD[Text] = rawXmls.filter( p => p._2.toString.toLowerCase().contains(searchText.toLowerCase)).map(p => p._2)

      xmlRDD.saveAsTextFile(hdfsOutputPath)
      val finalPath = hdfsOutputPath + "/final/";
      merge(hdfsOutputPath, finalPath, "countResult.txt");
    } catch {
      case ex: Exception => {
        val message = " Error in count " + ex
        LOGGER.error(message)
        throw new scala.RuntimeException(message)
      };
    }
  }


  def readFile(path: String, start_tag: String, end_tag: String) = {
    val conf = new Configuration()
    conf.set(XmlInputFormat.START_TAG_KEY, start_tag)
    conf.set(XmlInputFormat.END_TAG_KEY, end_tag)
    val rawXmls = sparkContext.newAPIHadoopFile(path, classOf[XmlInputFormat], classOf[LongWritable],  classOf[Text], conf)
    val xmlRDD = rawXmls.filter( p => p._2.toString.contains("Fantasy")).map(p => p._2)
    xmlRDD.saveAsTextFile(hdfsOutputPath)
  }


}
