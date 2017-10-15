package org.astri.safeguardhsbc.HttpUtility;

import java.io.*;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

/**
 * This utility class provides an abstraction layer for sending multipart HTTP
 * POST requests to a web server.
 * @author www.codejava.net
 *
 */
public class PostMultipartUtility extends HttpUtility {
    private final String boundary;
    private static final String crlf = "\r\n";
    private static final String charset = "UTF-8";
    private OutputStream outputStream;
    private PrintWriter writer;
    private String requestURL;

    private HashMap<String, String> headerFields = new HashMap<String, String>();
    private HashMap<String, File> uploadFiles = new HashMap<String, File>();
    private HashMap<String, String> formFields = new HashMap<String, String>();

    public PostMultipartUtility(String requestURL) throws IOException {
        this.requestURL = requestURL;
        // creates a unique boundary based on time stamp
        boundary = "---" + UUID.randomUUID() + "---";
    }

    public void addHeaderField(String name, String value) {
        headerFields.put(name, value);
    }

    public void addFormField(String name, String value) {
        formFields.put(name, value);
    }

    public void addFile(String fieldName, File uploadFile) {
        uploadFiles.put(fieldName, uploadFile);
    }

    private void appendHeaderField() {
        for (Map.Entry<String, String> headerField : headerFields.entrySet()
                ) {
            writer.append(headerField.getKey() + ": " + headerField.getValue()).append(crlf);
        }

        writer.flush();
    }

    private void appendFormField() {
        for (Map.Entry<String, String> formField : formFields.entrySet()) {
            writer.append("--" + boundary).append(crlf);
            writer.append("Content-Disposition: form-data; name=\"" + formField.getKey() + "\"")
                    .append(crlf);
            writer.append("Content-Type: text/plain; charset=" + charset).append(
                    crlf);
            writer.append(crlf);
            writer.append(formField.getValue()).append(crlf);
        }

        writer.flush();
    }


    private void appendFilePart() throws IOException {
        for (Map.Entry<String, File> uploadFile : uploadFiles.entrySet()) {
            String fileName = uploadFile.getValue().getName();
            writer.append("--" + boundary).append(crlf);
            writer.append(
                    "Content-Disposition: form-data; name=\"" + uploadFile.getKey()
                            + "\"; filename=\"" + fileName + "\"")
                    .append(crlf);
            String s = URLConnection.guessContentTypeFromName(fileName);

            writer.append("Content-Type: " + s).append(crlf);
            writer.append("Content-Transfer-Encoding: binary").append(crlf);
            writer.append(crlf);

            writer.flush();

            FileInputStream inputStream = new FileInputStream(uploadFile.getValue());
            byte[] buffer = new byte[4096];
            int bytesRead = -1;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.flush();
            inputStream.close();

            writer.append(crlf);

            writer.flush();
        }

        writer.flush();
    }

    public String getResponse() throws IOException {

        URL url = new URL(requestURL);
        httpConn = (HttpURLConnection) url.openConnection();
        httpConn.setUseCaches(false);
        httpConn.setDoOutput(true);
        httpConn.setRequestMethod("POST");
        httpConn.setDoInput(true);
        httpConn.setAllowUserInteraction(true);

        httpConn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
        outputStream = httpConn.getOutputStream();
        writer = new PrintWriter(new OutputStreamWriter(outputStream, charset), true);

        appendHeaderField();
        appendFormField();
        appendFilePart();

        writer.append("--" + boundary + "--").append(crlf).flush();
        writer.close();

        return super.getResponse();
    }
}