package com.assignment.common;


import com.assignment.exception.EduBigDataException;

/**
 * Created by deokishore on 30/07/2016.
 */
public interface Assignment {

    void wordWount(String inputPath, String outputPath, String wordToCount) throws EduBigDataException;

    void wordSearch(String inputPath, String outputPath, String wordToSearch) throws EduBigDataException;
}
