package com.tamtam.android.tamtam.services.json;

import android.util.JsonReader;
import android.util.JsonWriter;
import android.util.Log;

import java.io.Closeable;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static android.content.ContentValues.TAG;

/**
 * Created by antoninpa on 14/01/17.
 * I used the following advices to throw/catch exceptions :
 * http://softwareengineering.stackexchange.com/questions/231057/exceptions-why-throw-early-why-catch-late
 */

/**
 * This class implements basic mechanics to transform :
 *  - {@link String}s containing a Json Object to a single Model Object.
 *  - {@link String}s containing Json array of Objects to List of Model objects
 * This is mainly for code factorization between {@link JsonObjectConverter}
 *  and {@link JsonUserConverter}.
 * @param <M> The Model Object's class to convert to/from Json
 */
abstract class JsonObjectConverter<M> implements Mapper<String, M> {


    @Override
    public M fromJson(String inputJson) throws MappingException{
        // "serialize" input String and setup a JsonReader on it
        StringReader jsonStream = new StringReader(inputJson);
        JsonReader reader = new JsonReader(jsonStream);
        M parsedObject = null;
        try {
            parsedObject = readObject(reader);
        } catch (IOException e) {
            throw new MappingException("Reading json error", e);
        } finally {
            close(reader);
        }
        return parsedObject;
    }


    @Override
    public List<M> fromJsonArray(String inputJsonArrayObject) throws MappingException{
        // "serialize" input String and setup a JsonReader on it
        StringReader jsonArrayStream = new StringReader(inputJsonArrayObject);
        JsonReader reader = new JsonReader(jsonArrayStream);
        List<M> parsedObjects = null;
        try {
            parsedObjects = readObjectArray(reader);
        } catch (IOException e) {
            throw new MappingException("Reading json array error", e);
        } finally{
            close(reader);
        }
        return parsedObjects;
    }


    @Override
    public String toJson(M inputModelObject) throws MappingException {
        StringWriter jsonOutputStream = new StringWriter();
        JsonWriter writer = new JsonWriter(jsonOutputStream);
        String resultJson = null;

        try {
            //writer.setIndent("  ");
            writeObject(writer, inputModelObject);
            resultJson = jsonOutputStream.toString();
        } catch (IOException e) {
            throw new MappingException("Writing to json error", e);
        } finally {
            close(writer);
        }
        return resultJson;
    }


    @Override
    public String toJsonArray(Iterable<M> inputModelObjects) throws MappingException {
        StringWriter jsonOutputStream = new StringWriter();
        JsonWriter writer = new JsonWriter(jsonOutputStream);
        String resultJson = null;
        try {
            //writer.setIndent("  ");
            writeObjectsToArray(writer, inputModelObjects);
            resultJson = jsonOutputStream.toString();
        } catch (IOException e) {
            throw new MappingException("Writing to json array error", e);
        } finally {
            close(writer);
        }
        return resultJson;
    }



    private void writeObjectsToArray(JsonWriter writer,
                                     Iterable<M> inputModelObjects) throws IOException {
        writer.beginArray();
        for (M modelObject : inputModelObjects) {
            writeObject(writer, modelObject);
        }
        writer.endArray();
    }

    /**
     * Utility funciton that can be used in sublcasses
     * to write a collection of Strings to a Json array.
     * @param writer writer ready to write a Json Array
     * @param strings collection of {@link String}s to write in the Json array.
     * @throws IOException
     */
    protected void writeStringArray(JsonWriter writer, Iterable<String> strings) throws IOException {
        writer.beginArray();
        for (String value : strings) {
            writer.value(value);
        }
        writer.endArray();
    }

    private List<M> readObjectArray(JsonReader reader) throws IOException {
        List<M> messages = new ArrayList<>();
        reader.beginArray();
        while (reader.hasNext()) {
            messages.add(readObject(reader));
        }
        reader.endArray();
        return messages;
    }

    /**
     * Utility function that can be used in subclasses to read a json array of {@link String}s.
     * @param reader ready to read a Json Array of {@link String}s
     * @return a {@link Set} of Strings contained in the Array
     * @throws IOException
     */
    protected Set<String> readStringArray(JsonReader reader) throws IOException {
        Set<String> strings = new HashSet<>();

        reader.beginArray();
        while (reader.hasNext()) {
            strings.add(reader.nextString());
        }
        reader.endArray();

        return strings;
    }

    // utility function for proper close with exception handling
    private static void close(Closeable c) {
        if (c == null) return;
        try {
            c.close();
        } catch (IOException e) {
            Log.e(TAG, "close: error trying to close" + c.getClass(), e);
            e.printStackTrace();
        }
    }

    /**
     * Abstract method to implement in sublasses
     * This method must take a single Json representation of the model Object
     * and converts it into a model object, consuming the input string
     * @param reader {@link JsonReader} ready to read a string containing a model Object
     * @return an instance of the model object converted from the input Json
     * @throws IOException
     */
    abstract protected M readObject(JsonReader reader) throws IOException;

    /**
     * Abstract method to implement in subclasses
     * This method must take an instance of a model object a write the corresponding Json Object
     * using the {@link JsonWriter} passed as a parameter
     * @param writer to be used to write the Json string
     * @param modelObject input model object to convert to Json
     * @throws IOException
     */
    abstract protected void writeObject(JsonWriter writer, M modelObject) throws IOException;
}
