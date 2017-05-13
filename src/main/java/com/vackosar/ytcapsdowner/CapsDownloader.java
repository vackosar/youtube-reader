package com.vackosar.ytcapsdowner;

import android.os.AsyncTask;

import org.apache.commons.lang.StringEscapeUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;

public class CapsDownloader extends AsyncTask<String, Void, CapsDownloader.Result> {

    private static final int MAX_RETRY = 3;
    private static final String VIDEO_INFO_PREFIX = "http://www.youtube.com/get_video_info?video_id=";
    public static final String CAPTIONS_TOKEN = "caption_tracks";
    public static final String URL_TOKEN = "u";
    public static final String LANG_TOKEN = "lang";
    public static final String EN_LANG_TOKEN_VALUE = "en";

    private String download(String uri) {
        Exception exception = null;
        for (int i = 0; i < MAX_RETRY; i++) {
            try {
                String videoInfo = convertStreamToString(createVideoInfoUrl(uri).openConnection().getInputStream());
                String captionTracks = extractTokenValue(CAPTIONS_TOKEN, videoInfo);
                String url = extractTokenValue(URL_TOKEN, captionTracks);
                String englishUrl = setTokenValue(LANG_TOKEN, EN_LANG_TOKEN_VALUE, url);
                String subs = convertStreamToString(new URL(englishUrl).openConnection().getInputStream());
                return extractText(subs);
            } catch (TokenNotFound | IOException ignored) {
                exception = ignored;
            }
        }
        throw new IllegalArgumentException("English captions couldn't be downloaded for URL:" + uri + ". Error message: " + exception.getMessage());
    }

    private String setTokenValue(String key, String value, String url) {
        return url.replaceFirst("([&?])" + key + "=[^&]*", "$1" + key + "=" + value);
    }

    private URL createVideoInfoUrl(String urlString) throws MalformedURLException {
        URL url = new URL(urlString);
        if ("www.youtube.com".equals(url.getHost())) {
            for (String tuple: url.getQuery().split("&")) {
                if (tuple.startsWith("v=")) {
                    String value = tuple.replaceAll("v=", "");
                    return new URL(VIDEO_INFO_PREFIX + value);
                }
            }
        } else if ("youtu.be".equals(url.getHost())) {
            return new URL(VIDEO_INFO_PREFIX + url.getPath().replaceFirst("^/", ""));
        } else {
            throw new IllegalArgumentException("Invalid Youtube address.");
        }
        throw new RuntimeException("Could not parse exception.");
    }

    private String extractText(String subs) {
        return StringEscapeUtils.unescapeHtml(
                subs
                        .replaceAll("</text>", " ")
                        .replaceAll("<[^>]*>", "")
        )
                .replaceAll("<[^>]*>", "")
                .replaceAll("[‘’]", "'")
                .replaceAll("&#39;", "'")
                .replaceAll("\n", " ");
    }

    private String extractTokenValue(String name, String tokens) throws UnsupportedEncodingException {
        for (String token: tokens.split("&")) {
            if (token.startsWith(name + "=")) {
                return URLDecoder.decode(token.split("=")[1], "UTF-8");
            }
        }
        throw new TokenNotFound("Token " + name + " not found in " + tokens);
    }

    private static String convertStreamToString(java.io.InputStream is) throws IOException {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        String output = s.hasNext() ? s.next() : "";
        is.close();
        return output;
    }

    @Override
    protected Result doInBackground(String... uris) {
        try {
            return new Result(download(uris[0]));
        } catch (Exception e) {
            return new Result(e);
        }
    }

    private static class TokenNotFound extends IllegalArgumentException {
        TokenNotFound(String msg) { super(msg); }
    }


    public static class Result {

        public Result(String result) {
            this.result = result;
        }

        public Result(Exception exception) {
            this.exception = exception;
        }

        String result;
        Exception exception;

    }

}
