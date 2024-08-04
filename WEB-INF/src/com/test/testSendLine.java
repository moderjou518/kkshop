package com.test;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import java.io.OutputStream;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;

import java.net.URL;

//import org.json.JSONArray;

public class testSendLine {
    
    private static final String USER_AGENT = "Mozilla/5.0";

    private static final String GET_URL = "https://maker.ifttt.com/trigger/line_notify/with/key/crYSCpUqjKt9Y07srljoc9";
    //private static final String GET_URL = "http://eicdbreport.evergreen.com.tw/monitor/rrd/jsp/RPT1_LockInfo.jsp?dbName=HMSPATPE";    

    private static final String POST_URL = "https://google.com/search";

    private static final String POST_PARAMS = "value1=testJava1&value2=testMsg2";

    

    private static void sendGET() throws IOException {
        
        URL obj = new URL(GET_URL+"?"+POST_PARAMS);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", USER_AGENT);        
        int responseCode = con.getResponseCode();
        System.out.println("GET Response Code: " + responseCode);
        
        if (responseCode == HttpURLConnection.HTTP_OK) { 
            // success
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // print result
            System.out.println(response.toString());
        } else {
            System.out.println("GET request did not work.");
        }

    }

    private static void sendPOST() throws IOException {
        
        URL obj = new URL(POST_URL);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);

        // For POST only - START
        con.setDoOutput(true);
        OutputStream os = con.getOutputStream();
        os.write(POST_PARAMS.getBytes());
        os.flush();
        os.close();
        // For POST only - END

        int responseCode = con.getResponseCode();
        System.out.println("POST Response Code :: " + responseCode);

        if (responseCode == HttpURLConnection.HTTP_OK) { //success
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // print result
            System.out.println(response.toString());
        } else {
            System.out.println("POST request did not work.");
        }
    }    
    
    public static void main(String args[]) {
        //System.out.println("Hello");
        //executePost("http://www.evergreen.com.tw/tw/intro.html", "");
        //System.out.println("World");
        
        try {            
            sendGET();
            System.out.println("GET DONE");
            //sendPOST();
            //System.out.println("POST DONE");    
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        
        
    }
}