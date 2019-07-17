package com.util;

import com.model.CustomResponse;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class RestRequestsUtil {


    public static CustomResponse doRequest(String URL, String requestMethod, String body) throws IOException {
        URL url = new URL(URL);
        HttpURLConnection httpURLConnection = initConnection(url, requestMethod, body);

        int responseCode = 0;
        try {
            responseCode = httpURLConnection.getResponseCode();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        InputStream inputStream;
        if (200 <= responseCode && responseCode <= 299) {
            inputStream = httpURLConnection.getInputStream();
        } else {
            inputStream = httpURLConnection.getErrorStream();
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));

        StringBuilder response = new StringBuilder();
        String currentLine;

        while ((currentLine = in.readLine()) != null)
            response.append(currentLine);

        in.close();

        return new CustomResponse(responseCode, cleanQuotes(response.toString()));
    }

    private static HttpURLConnection initConnection(URL url, String requestMethod, String body) throws IOException {
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod(requestMethod);
        httpURLConnection.setRequestProperty("User-Agent", "Mozilla/5.0");
        httpURLConnection.setRequestProperty("Content-Type", "application/json; utf-8");
        httpURLConnection.setRequestProperty("Accept", "application/json");

        if (requestMethod.equals("POST")) {
            httpURLConnection.setDoOutput(true);

            try (OutputStream os = httpURLConnection.getOutputStream()) {
                byte[] input = body.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }
        }

        return httpURLConnection;
    }

    private static String cleanQuotes(String text) {
        return text.replaceAll("^\"|\"$", "");
    }
}
