package com.github.ckroeger.whatismyip;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.InputStream;
import java.util.List;

public class ReadIpHelperListTest {

    @Test
    public void test_read_list() throws Exception{
        InputStream stream = ReadIpHelperListTest.class.getClassLoader().getResourceAsStream("iphelper-sites.txt");
        List<String> list = IOUtils.readLines(stream, "UTF-8");
        for (String site :
                list) {
            System.out.println(site);
        }
    }
}
