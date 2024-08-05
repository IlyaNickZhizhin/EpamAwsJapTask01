package com.task09.weahter;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherRequest {


    public String request(Context context) {
        StringBuilder response = new StringBuilder();
        LambdaLogger logger = context.getLogger();
        try {
            URL url = new URL("https://api.open-meteo.com/v1/forecast?latitude=52.52&longitude=13.41&current=temperature_2m,wind_speed_10m&hourly=temperature_2m,relative_humidity_2m,wind_speed_10m");
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            logger.log("API Response: " + response);
            connection.disconnect();
        } catch (Exception var6) {
            Exception e = var6;
            logger.log(e.getMessage());
        }
        return response.toString();
    }
}
