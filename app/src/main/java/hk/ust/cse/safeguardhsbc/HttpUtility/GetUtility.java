package hk.ust.cse.safeguardhsbc.HttpUtility;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by admin on 22/6/2017.
 */
public class GetUtility extends HttpUtility {

    private String requestURL;
    private String accept;

    public GetUtility(String requestURL) throws IOException {
        this.requestURL = requestURL;
    }

    public void setAccept(String accept){ this.accept = accept; }

    public String getResponse() throws IOException {

        URL url = new URL(requestURL);
        httpConn = (HttpURLConnection) url.openConnection();
        httpConn.setUseCaches(false);
        httpConn.setRequestMethod("GET");
        httpConn.setAllowUserInteraction(true);

        if(accept != null && !accept.isEmpty()){
            httpConn.setRequestProperty("Accept", accept);
        }

        return super.getResponse();
    }
}
