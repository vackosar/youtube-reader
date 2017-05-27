package com.vackosar.youtubereader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class VideoInfoParser {

    private static final String CAPTIONS_TOKEN = "caption_tracks";
    private static final String URL_TOKEN = "u";
    private static final String QUERY_SEPARATOR = "([&?])";
    private static final String QUERY_VALUE_SEPARATOR = "=[^&]*";
    private static final String FIRST_MATCH = "$1";
    private static final String EQUALS = "=";
    private static final String LANG_TOKEN = "lang";
    private static final String EN_LANG_TOKEN_VALUE = "en";

    public static String extractCaptionsUrl(String videoInfo) {
        return setTokenValue(LANG_TOKEN, EN_LANG_TOKEN_VALUE, getDefaultLangCaptionsUrl(videoInfo));
    }

    private static String getDefaultLangCaptionsUrl(String videoInfo) {
        try {
            return getUrlFromVideoInfoVersion1(videoInfo);
        } catch (TokenNotFound e) {
            return getUrlFromVideoInfoVersion2(videoInfo);
        }
    }

    private static String getUrlFromVideoInfoVersion1(String videoInfo) {
        String captionTracks = extractTokenValue(CAPTIONS_TOKEN, videoInfo);
        return extractTokenValue(URL_TOKEN, captionTracks);
    }

    private static String getUrlFromVideoInfoVersion2(String videoInfo) {
        String playerResponse = null;
        try {
            playerResponse = extractTokenValue("player_response", videoInfo);
            return new JSONObject(playerResponse)
                    .getJSONObject("captions")
                    .getJSONObject("playerCaptionsTracklistRenderer")
                    .getJSONArray("captionTracks")
                    .getJSONObject(0)
                    .getString("baseUrl")
                    .replaceAll("\\u0026", "&");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private static String setTokenValue(String key, String value, String url) {
        return url.replaceFirst(QUERY_SEPARATOR + key + QUERY_VALUE_SEPARATOR, FIRST_MATCH + key + EQUALS + value);
    }

    static String extractTokenValue(String name, String tokens) {
        try {
            for (String token: tokens.split("&")) {
                if (token.startsWith(name + EQUALS)) {
                    return URLDecoder.decode(token.split(EQUALS)[1], "UTF-8");
                }
            }
            throw new TokenNotFound("Token " + name + " not found in " + tokens);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    static class TokenNotFound extends IllegalArgumentException {
        TokenNotFound(String msg) { super(msg); }
    }
}
