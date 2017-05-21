package com.vackosar.youtubereader;

import org.apache.commons.lang.StringEscapeUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class CapsDownloader {

    private static final int MAX_RETRY = 2;
    private static final String VIDEO_INFO_PREFIX = "http://www.youtube.com/get_video_info?video_id=";
    private static final String SLASH_PREFIX = "^/";
    private static final String EMPTY = "";
    private static final String DESKTOP_URL = "www.youtube.com";
    private static final String MOBILE_URL = "m.youtube.com";
    private static final String SHORT_URL = "youtu.be";
    private static final String TITLE_TOKEN = "title";

    public Result download(String uri) {
        try {
            String videoInfo = convertStreamToString(createVideoInfoUrl(uri).openConnection().getInputStream());
            String title = VideoInfoParser.extractTokenValue(TITLE_TOKEN, videoInfo);
            String captionsUrl = VideoInfoParser.extractCaptionsUrl(videoInfo);
            String captions = convertStreamToString(new URL(captionsUrl).openConnection().getInputStream());
            String text = extractText(captions);
            return new Result(title, text);
        } catch (Exception e) {
            throw new IllegalArgumentException("English captions couldn't be downloaded for URL: '" + uri + "'", e);
        }
    }

    private URL createVideoInfoUrl(String urlString) throws MalformedURLException {
        if (!urlString.startsWith("http://") && !urlString.startsWith("https://")) {
            urlString = "https://" + urlString;
        }
        URL url = new URL(urlString);
        if (DESKTOP_URL.equals(url.getHost()) || MOBILE_URL.equals(url.getHost())) {
            for (String tuple: url.getQuery().split("&")) {
                if (tuple.startsWith("v=")) {
                    String value = tuple.replaceAll("v=", "");
                    return new URL(VIDEO_INFO_PREFIX + value);
                }
            }
        } else if (SHORT_URL.equals(url.getHost())) {
            return new URL(VIDEO_INFO_PREFIX + url.getPath().replaceFirst(SLASH_PREFIX, EMPTY));
        } else {
            throw new IllegalArgumentException("Invalid Youtube address.");
        }
        throw new RuntimeException("Could not parse exception.");
    }

    private String extractText(String subs) {
        return StringEscapeUtils.unescapeHtml(subs.replaceAll("<[^>]*>", " "))
                .replaceAll("<[^>]*>", " ")
                .replaceAll("[‘’]", "'")
                .replaceAll("&#39;", "'")
                .replaceAll("\n", " ")
                .replaceAll(" +", " ");
    }

    private static String convertStreamToString(java.io.InputStream is) throws IOException {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        String output = s.hasNext() ? s.next() : "";
        is.close();
        return output;
    }

    public static class Result {

        public final String title;
        public final String text;

        public Result(String title, String text) {
            this.title = title;
            this.text = text;
        }
    }

}
