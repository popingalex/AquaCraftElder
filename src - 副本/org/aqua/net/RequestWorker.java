package org.aqua.net;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class RequestWorker {
    private StringBuffer queryString   = new StringBuffer();
    private StringBuffer requestString = new StringBuffer();
    private Integer      responseCode;
    private String       responseContent;
    private String       responseMesssage;

    public RequestWorker(String url) {
        requestString.append(url);
    }
    public RequestWorker setQueryParameter(String key, String value) {
        queryString.append(key).append("=");
        try {
            value = URLEncoder.encode(value, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        queryString.append(value).append("&");
        return this;
    }
    public RequestWorker Get() {
        return Post(null);
    }
    public RequestWorker Post(String content) {
        if (queryString.length() > 0) {
            queryString.deleteCharAt(queryString.length() - 1);
            requestString.append("?").append(queryString);
            System.out.println(requestString);
        }
        try {
            URL url = new URL(requestString.toString().replace(' ', '+'));
            HttpURLConnection urlConn = HttpURLConnection.class.cast(url.openConnection());

            if (content != null) {
                urlConn.setDoOutput(true);
                urlConn.setRequestMethod("POST");
                urlConn.setRequestProperty("Content-Type", "application/json");
                urlConn.setUseCaches(false);
                urlConn.connect();

                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(urlConn.getOutputStream(), "UTF-8"));
                writer.write(content);
                writer.flush();
                writer.close();
            } else {
                urlConn.connect();
            }

            this.responseCode = urlConn.getResponseCode();
            this.responseMesssage = urlConn.getResponseMessage();
            StringBuffer responseContent = new StringBuffer();
            if (urlConn.getResponseCode() == 200) {
                if (urlConn.getContentLength() > -1) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(urlConn.getInputStream(), "UTF-8"));
                    for (String temp = reader.readLine(); temp != null;) {
                        responseContent.append(temp).append(System.getProperty("line.separator"));
                        temp = reader.readLine();
                    }
                }
            }
            // TODO for debuggin?
            if (urlConn.getResponseCode() == 400) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(urlConn.getErrorStream(), "UTF-8"));
                for (String temp = reader.readLine(); temp != null;) {
                    responseContent.append(temp).append(System.getProperty("line.separator"));
                    temp = reader.readLine();
                }
            }
            this.responseContent = responseContent.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }
    public String getResponseContent() {
        return responseContent;
    }
    public String getResponseMessage() {
        return responseMesssage;
    }
    public Integer getResponseCode() {
        return responseCode;
    }
}