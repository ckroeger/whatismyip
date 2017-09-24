package com.github.ckroeger.whatismyip.util;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WhatIsMyIp {

    public static final String USER_AGENT_FIREFOX = "Mozilla/5.0 (Windows NT 5.1; rv:19.0) Gecko/20100101 Firefox/19.0";
    public static final String USER_AGENT = "User-Agent";
    public static final String IP_REG_EXP = "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)";
    public static final Pattern IP_PATTERN = Pattern.compile(IP_REG_EXP);

    public static final List<String> HELPER_SITES = readHelperSitesList();

    public static List<String> readHelperSitesList(){
        InputStream stream = WhatIsMyIp.class.getClassLoader().getResourceAsStream("iphelper-sites.txt");
        try {
            List<String> list = IOUtils.readLines(stream, "UTF-8");
            return list;
        } catch (IOException e) {
            return Collections.EMPTY_LIST;
        }
    }

    public static String extractIPfromString(String content){

        if(StringUtils.isBlank(content)){
            return null;
        }


        Matcher matcher = IP_PATTERN.matcher(content);
        // check all occurance
        if(matcher.find()) {
            return matcher.group();
        }
        return null;
    }
    public static String readHttpContent(String urlStr) throws ResolveException {
        BufferedReader serverResponse = null;
        try {
            URL url = new URL(urlStr);
            URLConnection conn = url.openConnection();
            conn.setRequestProperty(USER_AGENT,
                    USER_AGENT_FIREFOX);
            conn.connect();
            serverResponse = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String response = IOUtils.toString(serverResponse);
            return response;
        } catch (IOException e) {
            throw new ResolveException("Failed to call " + urlStr, e);
        } finally {
            IOUtils.closeQuietly(serverResponse);
        }

    }
}
