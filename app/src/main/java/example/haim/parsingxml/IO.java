package example.haim.parsingxml;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by DELL e7440 on 01/06/2017.
 */

public class IO {

    public static String readWebSite(String url, String charset) throws IOException{
        URL address = new URL(url);
        URLConnection con = address.openConnection();
        InputStream in = con.getInputStream();
        return getString(in, charset);
    }

    public static String readWebSite(String url) throws IOException{
        return readWebSite(url, "utf-8");
    }

    public static String getString(InputStream in, String charset) throws IOException {
        StringBuilder builder = new StringBuilder();
        String line = null;

        BufferedReader reader = new BufferedReader(new InputStreamReader(in, charset));

        try {
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
        }
        finally {
            reader.close();
        }

        return builder.toString();
    }

    public static String getString(InputStream in) throws IOException {
    return getString(in, "utf-8");

    }
}
