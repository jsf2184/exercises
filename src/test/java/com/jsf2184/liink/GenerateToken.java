package com.jsf2184.liink;

import com.squareup.okhttp.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.IOException;

@Slf4j
public class GenerateToken {


    @Test
    public void callLiink() {
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "grant_type=client_credentials&client_id=apiuser2I1&client_secret=daca4723-70fa-46c7-b6e7-58987d83dbcc");
        Request request = new Request.Builder()
                .url("https://xplore.iinconnect.com/api/token")
                .method("POST", body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("x-header-ip", "1.1.1.1")
                .build();
        try {
            Response response = client.newCall(request).execute();
            log.info("Got response. Null equals {}", response == null);
        } catch (IOException e) {
            log.error("CaughtException: {} : {}", e.getClass().getSimpleName(), e.getMessage());
        }
    }
}
