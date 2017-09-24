package com.github.ckroeger.whatismyip;

import org.apache.commons.io.IOUtils;
import org.junit.Ignore;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class RequestWithUserAgentTest {


    @Ignore
    @Test
    public void test_connection() throws Exception {
        String target = "http://formyip.com/";
        URL url = new URL(target);
        URLConnection conn = url.openConnection();
        conn.setRequestProperty("User-Agent",
                "Mozilla/5.0 (Windows NT 5.1; rv:19.0) Gecko/20100101 Firefox/19.0");
        conn.connect();
        BufferedReader serverResponse = new BufferedReader(
                new InputStreamReader(conn.getInputStream()));

        String response = IOUtils.toString(serverResponse);

        //System.out.println(serverResponse.readLine());
        System.out.println(response);

        serverResponse.close();
    }
}
