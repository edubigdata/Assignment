package com.edubigdata.supportutility.util

import org.apache.log4j.Logger

/**
 * Created by deokishore on 08/07/2016.
 */

  trait Logging {
    def logger: Logger
    def debug(message: String) { logger.debug(message) }
    def warn(message: String) { logger.warn(message) }
  }


