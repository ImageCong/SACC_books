package org.microservice.api.gateway.jsonutil;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GsonConfig {
    @Bean
    public Gson gsonObject(){
        Gson gson = new GsonBuilder()
                .serializeNulls()
                .setDateFormat("yyyy-MM-dd")
                .disableInnerClassSerialization()
                .setPrettyPrinting()
                .create();

        return gson;
    }
}
