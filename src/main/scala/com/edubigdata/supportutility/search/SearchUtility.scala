package com.edubigdata.supportutility.search

import com.edubigdata.supportutility.common.AbstractSupportSearchUtility

/**
  * Created by deokishore on 07/07/2016.
  */
trait SearchUtility extends AbstractSupportSearchUtility {

   def searchInXML(fileLocation:String, word:String, startTag:String, endTag:String)

 }
