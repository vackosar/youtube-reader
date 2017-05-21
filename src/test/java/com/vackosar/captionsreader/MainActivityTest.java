package com.vackosar.captionsreader;

import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;

public class MainActivityTest {

    @Test
    public void addition_isCorrect() throws Exception {
        URL url = new URL("https://www.youtube.com/watch?v=6Mfw_LUwo08&t=444s");
        System.out.println(createVideoInfoUrl(url));
    }

    private URL createVideoInfoUrl(URL url) throws MalformedURLException {
        for (String tuple: url.getQuery().split("&")) {
            if (tuple.startsWith("v=")) {
                String value = tuple.replaceAll("v=", "");
                return new URL("http://www.youtube.com/get_video_info?video_id=" + value);
            }
        }
        throw new RuntimeException("Could not parse exception.");
    }

}