package com.github.ckroeger.whatismyip.util;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(DataProviderRunner.class)
public class WhatIsMyIpTest {

    private final static String EXPECTED_IP = "201.209.86.139";
    public static final String SRC_TEST_RESOURCES = "./src/test/resources";

    @Test
    public void readHelperSitesList() throws Exception {
        List<String> list = WhatIsMyIp.readHelperSitesList();
        assertThat(list).isNotNull().isNotEmpty().hasSize(42);
    }

    @Test
    public void extractIPfromString() throws Exception {
        String testStr = getTestStr("test_response.html");
        String iPfromString = WhatIsMyIp.extractIPfromString(testStr);
        assertThat(iPfromString).isNotNull().isNotBlank();
        assertThat(iPfromString).isEqualTo(EXPECTED_IP);
    }

    @Ignore
    @Test
    public void readHttpContent() throws Exception {
        String content = WhatIsMyIp.readHttpContent("http://formyip.com/");
        assertThat(content).isNotBlank();
        System.out.println(content);
    }

    @DataProvider
    public static Object[][] multipleTestdata() throws Exception {
        List<String> list = WhatIsMyIp.readHelperSitesList();
        Object[][] testdata = new Object[list.size()][2];
        for (int pos = 0; pos < list.size(); pos++) {
            String site = list.get(pos);
            String fileName = StringUtils.substringAfter(site, "//");
            fileName = encodeFilename(fileName);
            //System.out.println(fileName);
            String testStr = getTestStr(fileName);
            assertThat(testStr).isNotNull();
            testdata[pos][0] = fileName;
            testdata[pos][1] = testStr;
            //String iPfromString = WhatIsMyIp.extractIPfromString(testStr);
            //assertThat(iPfromString).isNotNull().isEqualTo(EXPECTED_IP);
        }
        return testdata;
    }

    @Test
    @UseDataProvider("multipleTestdata")
    public void testwithMultipleTestdata(String fileName, String content) throws Exception {
        assertThat(content).isNotNull();
        String iPfromString = WhatIsMyIp.extractIPfromString(content);
        if (!EXPECTED_IP.equals(iPfromString)) {
            System.out.println("Faild with " + fileName);
        }
        assertThat(iPfromString).isNotNull().isEqualTo(EXPECTED_IP);
    }

    private static String getTestStr(String uri) throws IOException {
        InputStream stream = WhatIsMyIpTest.class.getResourceAsStream("/" + uri);
        if (stream == null) {
            return null;
        }
        String string = IOUtils.toString(stream, "UTF-8");
        return string;
    }

    @Ignore
    @Test
    public void readMultipleTestdata() throws Exception {
        List<String> list = WhatIsMyIp.readHelperSitesList();
        for (String site :
                list) {
            String fileName = StringUtils.substringAfter(site, "//");
            //System.out.println(encodeFilename(fileName));
            String content = null;
            try {
                content = WhatIsMyIp.readHttpContent(site);
            } catch (ResolveException e) {
                System.out.println("Failed to read from " + site);
            }
            if (content == null) {
                continue;
            }
            assertThat(content).isNotBlank();
            File file = new File(SRC_TEST_RESOURCES + "/" + encodeFilename(fileName));
            System.out.println(file.getAbsolutePath());
            FileUtils.writeStringToFile(file, content, "UTF-8");
        }

    }

    public static String encodeFilename(String s) {
        try {
            String newFilename = java.net.URLEncoder.encode(s, "UTF-8");
            newFilename = StringUtils.substringBeforeLast(newFilename, "%2F");
            newFilename = StringUtils.replaceAll(newFilename, "%2F", "_");
            return newFilename + ".txt";
        } catch (java.io.UnsupportedEncodingException e) {
            throw new RuntimeException("UTF-8 is an unknown encoding!?");
        }
    }

}