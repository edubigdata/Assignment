package com.edubigdata.supportutility.count

import com.edubigdata.supportutility.common.AbstractSupportSearchUtility

/**
  * Created by deokishore on 07/07/2016.
  */
trait CountUtility extends AbstractSupportSearchUtility {

   def countInXML(fileLocation:String, word:String, startTag:String, endTag:String)

 }
