package com.vackosar.ytcapsdowner;

import android.os.AsyncTask;
import android.widget.TextView;

import org.apache.commons.lang.StringEscapeUtils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;

class CapsDownloader extends AsyncTask<String, Void, String> {

    private final TextView textView;

    public CapsDownloader(TextView textView) {
        this.textView = textView;
    }

    public String downloadCaps(String uri) {
        try {
            String videoInfo = convertStreamToString(createVideoInfoUrl(uri).openConnection().getInputStream());
            String captionTracks = extractTokenValue("caption_tracks", videoInfo);
            String url = extractTokenValue("u", captionTracks);
            String subs = convertStreamToString(new URL(url).openConnection().getInputStream());
            String text = extractText(subs);
            return text;
//            return punctuate(text);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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

    private String punctuate(String text) throws IOException {
        byte[] data = ("text=" + text).getBytes();
        HttpURLConnection conn= (HttpURLConnection) new URL("http://bark.phon.ioc.ee/punctuator").openConnection();
        conn.setDoOutput(true);
        conn.setInstanceFollowRedirects(false);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("charset", "utf-8");
        conn.setRequestProperty("Content-Length", Integer.toString(data.length));
        conn.setUseCaches(false);
        DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
        wr.write(data);
        wr.close();
        return convertStreamToString(conn.getInputStream());
    }

    private String extractText(String subs) {
        return StringEscapeUtils.unescapeHtml((subs.replaceAll("</text>", " ").replaceAll("<[^>]*>", ""))).replaceAll("<[^>]*>", "").replaceAll("&#39;", "'");
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
        return downloadCaps(uris[0]);
    }

    protected void onPostExecute(String result) {
        textView.setText(result);
    }

}
