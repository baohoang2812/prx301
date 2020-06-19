/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prx.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import prx.config.SystemConfig;

/**
 *
 * @author Gia Bảo Hoàng
 */
public class HttpUtils {

    private static InputStream getInputStream(String urlString) throws MalformedURLException, IOException {
        URL url = new URL(urlString);
        URLConnection connection = url.openConnection();
        connection.setConnectTimeout(SystemConfig.TIME_OUT);
        connection.setReadTimeout(SystemConfig.TIME_OUT);
        return connection.getInputStream();
    }

    public static String getContent(String urlString) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        try ( InputStreamReader isr = new InputStreamReader(getInputStream(urlString), StandardCharsets.UTF_8); 
                BufferedReader bufferedReader = new BufferedReader(isr)) {
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            return stringBuilder.toString();
        }
    }
}
