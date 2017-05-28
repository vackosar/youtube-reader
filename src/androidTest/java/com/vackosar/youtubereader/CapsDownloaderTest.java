package com.vackosar.youtubereader;

import org.junit.Test;

public class CapsDownloaderTest {

    @Test
    public void download() throws Exception {
        new CapsDownloader().download("https://www.youtube.com/watch?v=GPaYrhUZSYQ");
    }

}