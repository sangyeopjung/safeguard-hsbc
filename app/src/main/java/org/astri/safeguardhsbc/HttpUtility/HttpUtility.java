package org.astri.safeguardhsbc.HttpUtility;


import java.io.*;
import java.net.HttpURLConnection;

/**
 * Created by admin on 28/6/2017.
 */
public abstract class HttpUtility {
    protected HttpURLConnection httpConn;

    protected String getResponse() throws IOException {

        int status = httpConn.getResponseCode();

        BufferedReader reader;
        String response = "";

        if (status >= 200 && status < 400) {
            reader = new BufferedReader(new InputStreamReader(
                    httpConn.getInputStream()));
        } else {
            return httpConn.getResponseMessage();
        }

        String line = null;
        while ((line = reader.readLine()) != null) {
            response += line;
        }
        reader.close();

        httpConn.disconnect();

        return response;
    }

    protected byte[] getBinaryResponse() throws IOException {

        int status = httpConn.getResponseCode();

        InputStream is;

        if (status >= 200 && status < 400) {
            is = httpConn.getInputStream();
        } else {
            is =  httpConn.getErrorStream();
        }

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        int nRead;
        byte[] data = new byte[4096];

        while ((nRead = is.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }

        buffer.flush();

        httpConn.disconnect();

        return buffer.toByteArray();
    }
}
