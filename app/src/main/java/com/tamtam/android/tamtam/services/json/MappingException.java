package com.tamtam.android.tamtam.services.json;

/**
 * This exception is raised when something bad happened trying to write Json.
 **/
public class MappingException extends Exception{
    public MappingException() {}
    public MappingException(String msg) { super(msg); }
    public MappingException(Throwable cause) { super(cause); }
    public MappingException(String msg, Throwable cause) { super(msg, cause); }
}
