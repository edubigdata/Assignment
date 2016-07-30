package com.assignment.users.deo;

import com.assignment.exception.EduBigDataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by deokishore on 29/07/2015.
 */
public class AssignmentImpl {

    private static final Logger LOGGER = LoggerFactory.getLogger(AssignmentImpl.class);

    public static void wordWount(String inputPath, String outputPath, String wordToCount) throws EduBigDataException {
        try {
            WordCount.wordCount(inputPath, outputPath, wordToCount);
        } catch (Exception ex) {
            String message = " Error while counting" + wordToCount;
            LOGGER.error(message, ex);
            throw new EduBigDataException(message, ex);
        }
    }

    public static void wordSearch(String inputPath, String outputPath, String wordToSearch){

    }


}









