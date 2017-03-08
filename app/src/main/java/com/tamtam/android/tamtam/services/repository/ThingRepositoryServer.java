/*
 *     Android application to create and display localized objects.
 *     Copyright (C) 2017  pascal bodin, antonin perrot-audet
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.tamtam.android.tamtam.services.repository;

import android.util.Log;

import com.tamtam.android.tamtam.model.ThingObject;
import com.tamtam.android.tamtam.services.json.JsonThingConverter;
import com.tamtam.android.tamtam.services.json.Mapper;
import com.tamtam.android.tamtam.services.json.MappingException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by antoninpa on 28/01/17.
 */

public class ThingRepositoryServer implements IThingRepository {
    private static final String TAG = "ThingRepositoryServer";

    // timeouts in ms
    private final static int READ_TIMEOUT = 10000;
    private final static int CONNECT_TIMEOUT = 10000;
    private final static int RESPONSE_MAX_SIZE_IN_CHARS = 5000000 ;

    // todo : should the mapper be static ?
    private final Mapper<String,ThingObject> mapper = new JsonThingConverter();

    private URI mServerBaseURI = null;


    // Keep a reference to the NetworkFragment, which owns the AsyncTask object
    // that is used to execute network ops.
    //private NetworkFragment mNetworkFragment;



    public ThingRepositoryServer(URI serverBaseURI){
        if (serverBaseURI != null){
            Log.d(TAG, "ThingRepositoryServer: Creation");
            mServerBaseURI = serverBaseURI;
        } else {
            throw new NullPointerException("ServerURI must be non null");
        }

        // that must go in the RepositoryFactory
        /*
        NetworkInfo networkInfo = mCallback.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected() ||
                (networkInfo.getType() != ConnectivityManager.TYPE_WIFI
                        && networkInfo.getType() != ConnectivityManager.TYPE_MOBILE)) {
            // If no connectivity, cancel task and update Callback with null data.
            mCallback.updateFromDownload(null);
            cancel(true);
        }
        */

    }


    /**
     * <p>
     * Given a URL, sets up a connection and gets the HTTP response body from the server.
     * If the network request is successful, it returns the response body in String form. Otherwise,
     * it will throw an IOException.
     * </p>
     * Got from <a href="https://developer.android.com/training/basics/network-ops/connecting.html#HeadlessFragment">
     * android documentation</a>.
     */
    private String downloadUrl(URL url) throws IOException {
        InputStream stream = null;
        InputStream errorStream = null;
        HttpURLConnection connection = null;
        String result = null;
//todo change to https of course
        try {
            Log.d(TAG, "downloadUrl: URL = " + url.toString());
            connection = (HttpURLConnection) url.openConnection();

            // Timeout for reading InputStream arbitrarily set to 3000ms.
            connection.setReadTimeout(READ_TIMEOUT);

            // Timeout for connection.connect() arbitrarily set to 3000ms.
            connection.setConnectTimeout(CONNECT_TIMEOUT);

            // For this use case, set HTTP method to GET.
            connection.setRequestMethod("GET");

            // Already true by default but setting just in case; needs to be true since this request
            // is carrying an input (response) body.
            connection.setDoInput(true);
            // Open communications link (network traffic occurs here).
            connection.connect();

            // todo if needed setup callbacks responses to track progress
            //publishProgress(DownloadCallback.Progress.CONNECT_SUCCESS);

            int responseCode = connection.getResponseCode();
            Log.d(TAG, "downloadUrl: Response Code = " + responseCode);

            /*
            if (responseCode != HttpsURLConnection.HTTP_OK) {
                throw new IOException("HTTP error code: " + responseCode);
            }*/
            Log.d(TAG, "downloadUrl: Response Msg = " + connection.getResponseMessage());



            errorStream = connection.getErrorStream();
            if (errorStream != null) {
                // Converts Stream to String with max length of 500000.
                result = readStream(errorStream, RESPONSE_MAX_SIZE_IN_CHARS);
                Log.d(TAG, "downloadUrl: ErrorResponse = " + result);
            }


            // Retrieve the response body as an InputStream.
            stream = connection.getInputStream();

            // todo if needed setup callbacks responses to track progress
            // publishProgress(DownloadCallback.Progress.GET_INPUT_STREAM_SUCCESS, 0);

            if (stream != null) {
                // Converts Stream to String with max length of 500000.
                result = readStream(stream, RESPONSE_MAX_SIZE_IN_CHARS);
            }
        } finally {
            // Close Stream and disconnect HTTPS connection.
            if (stream != null) {
                stream.close();
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
        return result;
    }



    private String deleteUrl(URL url) throws IOException {
        InputStream stream = null;
        InputStream errorStream = null;
        HttpURLConnection connection = null;
        String result = null;
//todo change to https of course
        try {
            Log.d(TAG, "downloadUrl: URL = " + url.toString());
            connection = (HttpURLConnection) url.openConnection();

            // Timeout for reading InputStream arbitrarily set to 3000ms.
            connection.setReadTimeout(READ_TIMEOUT);

            // Timeout for connection.connect() arbitrarily set to 3000ms.
            connection.setConnectTimeout(CONNECT_TIMEOUT);

            // For this use case, set HTTP method to GET.
            connection.setRequestMethod("DELETE");

            // Already true by default but setting just in case; needs to be true since this request
            // is carrying an input (response) body.
            connection.setDoInput(true);
            // Open communications link (network traffic occurs here).
            connection.connect();

            // todo if needed setup callbacks responses to track progress
            //publishProgress(DownloadCallback.Progress.CONNECT_SUCCESS);

            int responseCode = connection.getResponseCode();
            Log.d(TAG, "deleteURL: Response Code = " + responseCode);

            /*
            if (responseCode != HttpsURLConnection.HTTP_OK) {
                throw new IOException("HTTP error code: " + responseCode);
            }*/
            Log.d(TAG, "deleteURL: Response Msg = " + connection.getResponseMessage());



            errorStream = connection.getErrorStream();
            if (errorStream != null) {
                // Converts Stream to String with max length of 500000.
                result = readStream(errorStream, RESPONSE_MAX_SIZE_IN_CHARS);
                Log.d(TAG, "deleteURL: ErrorResponse = " + result);
            }


            // Retrieve the response body as an InputStream.
            stream = connection.getInputStream();

            // todo if needed setup callbacks responses to track progress
            // publishProgress(DownloadCallback.Progress.GET_INPUT_STREAM_SUCCESS, 0);

            if (stream != null) {
                // Converts Stream to String with max length of 500000.
                result = readStream(stream, RESPONSE_MAX_SIZE_IN_CHARS);
            }
        } finally {
            // Close Stream and disconnect HTTPS connection.
            if (stream != null) {
                stream.close();
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
        return result;
    }




    /**
     * <p>
     * Given a URL, sets up a connection
     * create a PUT request with body as body
     *
     * and gets the HTTP response body from the server.
     * If the network request is successful, it returns the response body in String form. Otherwise,
     * it will throw an IOException.
     * </p>
     */
    private String uploadUrl(URL url, String jsonString) throws IOException {
        InputStream stream = null;
        InputStream errorStream = null;
        HttpURLConnection connection = null;
        String result = null;

        try {
            Log.d(TAG, "uploadUrl: URL = "+ url.toString());
            connection = (HttpURLConnection) url.openConnection();

            // Timeout for reading InputStream arbitrarily set to 3000ms.
            connection.setReadTimeout(READ_TIMEOUT);

            // Timeout for connection.connect() arbitrarily set to 3000ms.
            connection.setConnectTimeout(CONNECT_TIMEOUT);

            // For this use case, set HTTP method to PUT.
            connection.setRequestMethod("PUT");

            connection.setRequestProperty("Content-Type","application/json");

            connection.setDoOutput(true);
            connection.setDoInput(true);
            // Open communications link (network traffic occurs here).
            connection.connect();

            // todo if needed setup callbacks responses to track progress
            //publishProgress(DownloadCallback.Progress.CONNECT_SUCCESS);

            OutputStream os = connection.getOutputStream();
            Log.d(TAG, "uploadUrl: json = " + jsonString);
            os.write( jsonString.getBytes("UTF-8") );
            os.close();
            Log.d(TAG, "uploadUrl: connecion = " + connection.toString());
            int responseCode = connection.getResponseCode();
            Log.d(TAG, "uploadUrl: Response Code = " + responseCode);

            /*
            if (responseCode != HttpsURLConnection.HTTP_OK) {
                throw new IOException("HTTP error code: " + responseCode);
            }*/
            Log.d(TAG, "uploadUrl: Response Msg = " + connection.getResponseMessage());

            errorStream = connection.getErrorStream();
            if (errorStream != null) {
                // Converts Stream to String with max length of 500000.
                result = readStream(errorStream, RESPONSE_MAX_SIZE_IN_CHARS);
                Log.d(TAG, "uploadUrl: ErrorResponse = " + result);
            }

            // Retrieve the response body as an InputStream.
            stream = connection.getInputStream();

            // todo if needed setup callbacks responses to track progress
            // publishProgress(DownloadCallback.Progress.GET_INPUT_STREAM_SUCCESS, 0);


            if (stream != null) {
                // Converts Stream to String with max length of 500000.
                result = readStream(stream, RESPONSE_MAX_SIZE_IN_CHARS);

                Log.d(TAG, "uploadUrl: Response = " + result);
            }
        } finally {
            // Close Stream and disconnect HTTPS connection.
            if (stream != null) {
                stream.close();
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
        return result;
    }


    /**
     * <p>
     * Converts the contents of an InputStream to a String.
     * </p>
     * Got from <a href="https://developer.android.com/training/basics/network-ops/connecting.html#HeadlessFragment">
     * android documentation</a>.
     */
    private String readStream(InputStream stream, int maxLength) throws IOException {
        String result = null;
        // Read InputStream using the UTF-8 charset.
        InputStreamReader reader = new InputStreamReader(stream, "UTF-8");
        // Create temporary buffer to hold Stream data with specified max length.
        char[] buffer = new char[maxLength];
        // Populate temporary buffer with Stream data.
        int numChars = 0;
        int readSize = 0;
        while (numChars < maxLength && readSize != -1) {
            numChars += readSize;
            // todo if needed setup callbacks responses to track progress
            //int pct = (100 * numChars) / maxLength;
            //publishProgress(DownloadCallback.Progress.PROCESS_INPUT_STREAM_IN_PROGRESS, pct);
            readSize = reader.read(buffer, numChars, buffer.length - numChars);
        }

        if (numChars != -1) {
            // The stream was not empty.
            // Create String that is actual length of response body if actual length was less than
            // max length.
            numChars = Math.min(numChars, maxLength);
            result = new String(buffer, 0, numChars);
        }
        return result;
    }







    @Override
    public Collection<ThingObject> getByIds(Iterable<String> ids) {
        ArrayList<ThingObject> foundThingList = new ArrayList<ThingObject>();
        for (String id:ids){
            foundThingList.add(getById(id));
        }
        return foundThingList;
    }

    @Override
    public ThingObject getById(String id) {
        String requestPath = "/things/" + id;
        ThingObject thing = null;

        try{
            URI request = new URI (null, // scheme
                    null, // host
                    requestPath, // path
                    null); // fragment

            // construct URI by resolving relative path and mServerBaseURI
            Log.d(TAG, "getById: URI" + mServerBaseURI.resolve(request));

            String jsonThing = downloadUrl(mServerBaseURI.resolve(request).toURL());

            thing = mapper.fromJson(jsonThing);

        } catch (URISyntaxException e) {
            Log.e(TAG, "getById: URI Syntax is not correct  ", e);
        }catch (IOException e) {
            Log.e(TAG, "getById: IO Error executing request",e );
        } catch (MappingException e){
            Log.e(TAG, "getById: Mapping to thing problem", e );
        }

        return thing;

    }

    @Override
    public Collection<ThingObject> getAll()
    {
        return null;
    }

    @Override
    public void add(ThingObject item) {
        Log.d(TAG, "add: Start Adding procedure");

        if (item != null) {

            String requestPath = "/things/" + item.getThingId();
            ThingObject thing = null;

            try{
                URI request = new URI (null, // scheme
                        null, // host
                        requestPath, // path
                        null); // fragment
                Log.d(TAG, "getById: URI" + mServerBaseURI.resolve(request));

                String response = uploadUrl(mServerBaseURI.resolve(request).toURL(), mapper.toJson(item));

                Log.i(TAG, "add: " + response);

            } catch (URISyntaxException e) {
                Log.e(TAG, "getById: URI Syntax is not correct  ", e);
            }catch (IOException e) {
                Log.e(TAG, "getById: IO Error executing request",e );
            } catch (MappingException e){
                Log.e(TAG, "getById: Mapping to thing problem", e );
            }

        }else {
            throw new NullPointerException("item can't be null");
        }
    }

    @Override
    public void add(Iterable<ThingObject> items) {
        for (ThingObject item:items ){
            add(item);
        }
    }

    @Override
    public void update(ThingObject item) {
        removeById(item.getThingId());
        add(item);
        // do something
    }

    @Override
    public void remove(ThingObject item) {
        removeById(item.getThingId());
    }

    @Override
    public void removeAll() {
        // do something
    }

    @Override
    public void removeById(String id){
        Log.d(TAG, "add: Start Removing procedure");

        if (id != null && !id.isEmpty()) {

            String requestPath = "/things/" + id;

            try{
                URI request = new URI (null, // scheme
                        null, // host
                        requestPath, // path
                        null); // fragment
                Log.d(TAG, "getById: URI" + mServerBaseURI.resolve(request));

                String response = deleteUrl(mServerBaseURI.resolve(request).toURL());

                Log.i(TAG, "remove: " + response);

            } catch (URISyntaxException e) {
                Log.e(TAG, "getById: URI Syntax is not correct  ", e);
            }catch (IOException e) {
                Log.e(TAG, "getById: IO Error executing request",e );
            }

        }else {
            throw new NullPointerException("id can't be null or empty");
        }
    }

    @Override
    public void removeByIds(Iterable<String> ids) {
        for (String id:ids){
            removeById(id);
        }
    }
}
