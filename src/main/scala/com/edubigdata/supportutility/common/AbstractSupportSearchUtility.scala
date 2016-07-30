package com.edubigdata.supportutility.common

import java.io.IOException
import java.util
import java.util.ArrayList
import com.edubigdata.supportutility.util.XmlInputFormat
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileSystem, FileUtil, Path}
import org.apache.hadoop.security.SecurityUtil
import org.apache.log4j.Logger
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.types.{DataTypes, StructField, StructType}
import org.apache.spark.sql.{DataFrame, Row, RowFactory, SQLContext}
import org.apache.spark.{SparkConf, SparkContext}

/**
 * Created by deokishore on 07/07/2016.
 */
abstract class AbstractSupportSearchUtility {
  private val LOGGER: Logger = Logger.getLogger(classOf[AbstractSupportSearchUtility])

  protected val hdfsOutputPath = "file:///Users/deokishore/Downloads/deoFolder"
  protected val tempOutputPath = "file:///Users/deokishore/Downloads/tempBook.xml"

  protected var sparkContext: SparkContext = _
  protected var sqlContext: SQLContext = _
  protected var dataFrame: DataFrame = _

  initializeSparkAndSQLContext

  def initializeSparkAndSQLContext: Unit = {

    // Uncomment it, in order to run it locally.
    val conf: SparkConf = runLocally

    // Production
    //val conf: SparkConf = new SparkConf()
    // .setAppName("support-utility")
    // .set("spark.default.parallelism", "100")
    // .set("spark.executor.memory", "2g")

    sparkContext = new SparkContext(conf)
    sqlContext = new SQLContext(sparkContext)
    deleteFileIfExists(hdfsOutputPath)
  }

  def runLocally: SparkConf = {
    loginToHadoopEnvironment

    // Local
    val conf: SparkConf = new SparkConf()
      .setAppName("SparkConfig")
      .setMaster("local")
      .set("spark.executor.memory", "128m")
      .set("spark.cores.max", "1")
      .set("spark.default.parallelism", "3")
    conf
  }

  protected def getConfiguration(startTag: String, endTag: String): Configuration = {
    val conf = new Configuration()
    conf.set(XmlInputFormat.START_TAG_KEY, startTag)
    conf.set(XmlInputFormat.END_TAG_KEY, endTag)
    conf
  }

  protected def getSchema: StructType = {
    val fields = new ArrayList[StructField]
    fields.add(DataTypes.createStructField("line", DataTypes.StringType, true))
    val schema: StructType = DataTypes.createStructType(fields)
    schema
  }

  @throws(classOf[Exception])
  protected def initializeDataFrame(avroInputPath: String) {
    sqlContext = new SQLContext(sparkContext)
    val options: util.HashMap[String, String] = new util.HashMap[String, String]
    LOGGER.info("Loading file ...... " + avroInputPath)
    options.put("path", avroInputPath)
    try {
      dataFrame = sqlContext.load("com.databricks.spark.avro", options)
    }
    catch {
      case ex: Exception => {
        val message: String = "Error while loading file : " + avroInputPath
        LOGGER.error(message, ex)
        throw new Exception(ex)
      }
    }
    LOGGER.info("Lodded file successfully ...... " + avroInputPath)
  }

  protected def search(searchText: String, rowRDD: RDD[(String, String)]): DataFrame = {
    val row2RDD: RDD[Row] = rowRDD.map(line => RowFactory.create(line._2))
    val dataFrame: DataFrame = sqlContext.createDataFrame(row2RDD, getSchema)
    val result: DataFrame = dataFrame.filter(dataFrame.col("line").like("%" + searchText + "%"))
    result
  }

  protected def deleteFileIfExists(filePath:String) = {
    val hadoopConf = new org.apache.hadoop.conf.Configuration()
    val hdfs = org.apache.hadoop.fs.FileSystem.get(new java.net.URI(filePath), hadoopConf)
    try {
      hdfs.delete(new org.apache.hadoop.fs.Path(filePath), true)
    } catch
      {
        case _ : Throwable => { }
      }
  }


  @throws(classOf[IOException])
  protected def merge(srcPath: String, dstPath: String, fileName: String) {
    val hadoopConfig: Configuration = sparkContext.hadoopConfiguration
    val hdfs: FileSystem = FileSystem.get(hadoopConfig)
    val destinationPath: Path = new Path(dstPath)
    if (!hdfs.exists(destinationPath)) {
      hdfs.mkdirs(destinationPath)
    }
    FileUtil.copyMerge(hdfs, new Path(srcPath), hdfs, new Path(dstPath + "/" + fileName), false, hadoopConfig, null)
  }

  def loginToHadoopEnvironment {
    LOGGER.info(" trying to login in hadoop: ")
    val principal: String = "bigdata-app-ecpdemolinkeddata-srvc@INTPROD.THOMSONREUTERS.COM"
    val keytab: String = "/Users/deokishore/Development2/patents-ingester/src/main/resources/input/bigdata-app-ecpdemolinkeddata-srvc.keytab"
    val conf: Configuration = new Configuration
    val USER_KEY: String = "appuser.principal"
    val KEYTAB_KEY: String = "appuser.keytab.filename"
    conf.set(USER_KEY, principal)
    conf.set(KEYTAB_KEY, keytab)
    try {
      SecurityUtil.login(conf, KEYTAB_KEY, USER_KEY)
    }
    catch {
      case ex: IOException => {
        val message: String = " Error while connecting to hadoop  "
        throw new RuntimeException(message, ex)
      }
    }
    LOGGER.info("Connection was successful: ")
  }


}
