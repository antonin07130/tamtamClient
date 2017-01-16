package com.tamtam.android.tamtam.services.json;

/**
 * This exception is raised when something bad happened trying to read Json.
 **/
public class JsonToModelReaderException extends Exception{
    public JsonToModelReaderException() {}
    public JsonToModelReaderException(String msg) { super(msg); }
    public JsonToModelReaderException(Throwable cause) { super(cause); }
    public JsonToModelReaderException(String msg, Throwable cause) { super(msg, cause); }
}
