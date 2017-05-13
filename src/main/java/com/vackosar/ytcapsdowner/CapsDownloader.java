package com.vackosar.ytcapsdowner;

import android.os.AsyncTask;

import org.apache.commons.lang.StringEscapeUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;

class CapsDownloader extends AsyncTask<String, Void, String> {

    private String download(String uri) {
        try {
            String videoInfo = convertStreamToString(createVideoInfoUrl(uri).openConnection().getInputStream());
            String captionTracks = extractTokenValue("caption_tracks", videoInfo);
            String url = extractTokenValue("u", captionTracks);
            String englishUrl = setTokenValue("lang", "en", url);
            String subs = convertStreamToString(new URL(englishUrl).openConnection().getInputStream());
            return extractText(subs);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String setTokenValue(String key, String value, String url) {
        return url.replaceFirst("([&?])" + key + "=[^&]*", "$1" + key + "=" + value);
    }

    private URL createVideoInfoUrl(String url) throws MalformedURLException {
        for (String tuple: new URL(url).getQuery().split("&")) {
            if (tuple.startsWith("v=")) {
                String value = tuple.replaceAll("v=", "");
                return new URL("http://www.youtube.com/get_video_info?video_id=" + value);
            }
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
        throw new RuntimeException("Token not found");
    }

    private static String convertStreamToString(java.io.InputStream is) throws IOException {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        String output = s.hasNext() ? s.next() : "";
        is.close();
        return output;
    }

    @Override
    protected String doInBackground(String... uris) {
        return download(uris[0]);
    }

}
