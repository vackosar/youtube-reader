package ytcapsdowner.vackosar.com.ytcapsdowner;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        String videoInfo = convertStreamToString(new URL("http://www.youtube.com/get_video_info?video_id=6Mfw_LUwo08").openConnection().getInputStream());
        String captionTracks = extractTokenValue("caption_tracks", videoInfo);
        String url = extractTokenValue("u", captionTracks);
        String caps = convertStreamToString(new URL(url).openConnection().getInputStream());
//        connection.getInputStream()
        // http://www.youtube.com/get_video_info?video_id=6Mfw_LUwo08
        // caption_tracks=
        // decode
        // u=
        // decode
        // get
    }

    private String extractTokenValue(String name, String tokens) throws UnsupportedEncodingException {
        for (String token: tokens.split("&")) {
            if (token.startsWith(name + "=")) {
                return URLDecoder.decode(token.split("=")[1], "UTF-8");
            }
        }
        throw new RuntimeException("Token not found");
    }
    static String convertStreamToString(java.io.InputStream is) throws IOException {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        String output = s.hasNext() ? s.next() : "";
        is.close();
        return output;
    }
}