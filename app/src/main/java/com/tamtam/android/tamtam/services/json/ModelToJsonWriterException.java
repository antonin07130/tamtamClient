package com.tamtam.android.tamtam.services.json;

/**
 * This exception is raised when something bad happened trying to write Json.
 **/
public class ModelToJsonWriterException extends Exception{
    public ModelToJsonWriterException() {}
    public ModelToJsonWriterException(String msg) { super(msg); }
    public ModelToJsonWriterException(Throwable cause) { super(cause); }
    public ModelToJsonWriterException(String msg, Throwable cause) { super(msg, cause); }
}
