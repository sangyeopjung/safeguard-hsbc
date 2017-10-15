package hk.ust.cse.safeguardhsbc.HttpUtility;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class PutUtility extends HttpUtility  {

    private String requestURL;
    private String requestBody;
    private String contentType;
    private String accept;

    private static final String charset = "UTF-8";

    public PutUtility(String requestURL) throws IOException {
        this.requestURL = requestURL;
    }

    public void setRequestBody(String requestBody){
        this.requestBody = requestBody;
    }

    public void setContentType(String contentType){ this.contentType = contentType; }

    public void setAccept(String accept){ this.accept = accept; }

    public String getResponse() throws IOException {

        URL url = new URL(requestURL);
        httpConn = (HttpURLConnection) url.openConnection();
        httpConn.setUseCaches(false);
        httpConn.setDoOutput(true);
        httpConn.setRequestMethod("PUT");


        httpConn.setDoInput(true);
        httpConn.setAllowUserInteraction(true);

        if (contentType != null && !contentType.isEmpty()) {
            httpConn.setRequestProperty("Content-Type", contentType);
        }

        if(accept != null && !accept.isEmpty()){
            httpConn.setRequestProperty("Accept", accept);
        }

        OutputStream outputStream = httpConn.getOutputStream();

        PrintWriter writer = new PrintWriter(new OutputStreamWriter(outputStream, charset),
                                 true);

        writer.append(requestBody);

        writer.close();

        return super.getResponse();
    }

    public byte[] getBinaryResponse() throws IOException {

        URL url = new URL(requestURL);
        httpConn = (HttpURLConnection) url.openConnection();
        httpConn.setUseCaches(false);
        httpConn.setDoOutput(true);
        httpConn.setRequestMethod("PUT");

        httpConn.setDoInput(true);
        httpConn.setAllowUserInteraction(true);

        if (contentType != null && !contentType.isEmpty()) {
            httpConn.setRequestProperty("Content-Type", contentType);
        }

        if(accept != null && !accept.isEmpty()){
            httpConn.setRequestProperty("Accept", accept);
        }

        OutputStream outputStream = httpConn.getOutputStream();

        PrintWriter writer = new PrintWriter(new OutputStreamWriter(outputStream, charset),
                                             true);

        writer.append(requestBody);

        writer.close();

        return super.getBinaryResponse();
    }
}