package me.iqq.client;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

// https://hc.apache.org/httpcomponents-client-5.0.x/quickstart.html
public class HttpUtil {

    public static void get(String url, Map<String, Object> params) throws IOException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet("https://hc.apache.org/httpcomponents-client-5.0.x/quickstart.html");

            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                System.out.println(response.getStatusLine().getStatusCode()
                    + " "
                    + response.getStatusLine().getReasonPhrase()
                );
                HttpEntity entity = response.getEntity();
                System.out.println(new String(entity.getContent().readAllBytes(), StandardCharsets.UTF_8));
                EntityUtils.consume(entity);
            }
        }
    }

    public static void main(String[] args) {
//        try {
//            get();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

}
