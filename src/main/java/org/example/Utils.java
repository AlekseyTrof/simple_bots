package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.FileOutputStream;

public class Utils {

    public static String getImageUrl(String nasaUrl) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(nasaUrl);

        try {
            CloseableHttpResponse response = httpClient.execute(httpGet);
            ObjectMapper mapper = new ObjectMapper();
            NasaAnswer answer = mapper.readValue(response.getEntity().getContent(), NasaAnswer.class);
            String imageUrl = answer.getUrl();
            return imageUrl;
        } catch (Exception e) {
            System.out.println("Произошла ошибка доступа к серверу Nasa");;
            return "";
        }

    }
}
