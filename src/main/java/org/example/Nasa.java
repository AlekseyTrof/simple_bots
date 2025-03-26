package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.FileOutputStream;
import java.io.IOException;

public class Nasa {
    public static void promo() throws IOException {
        String url = "https://api.nasa.gov/planetary/apod?" +
                "api_key=tS9CFvpcIFY5yE9K9E2cp907zzv1mLLnocOAvoUW" +
                "&date=2025-03-26";

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);

        CloseableHttpResponse response = httpClient.execute(httpGet);

        ObjectMapper mapper = new ObjectMapper();
        NasaAnswer answer = mapper.readValue(response.getEntity().getContent(), NasaAnswer.class);

        String imageUrl = answer.getUrl();
        String[] splittedAnswer = imageUrl.split("/");
        String fileName = splittedAnswer[splittedAnswer.length - 1];

        HttpGet imageRequest = new HttpGet(imageUrl);
        CloseableHttpResponse image = httpClient.execute(imageRequest);

        FileOutputStream fos = new FileOutputStream("images/" + fileName);
        image.getEntity().writeTo(fos);

    }
}
