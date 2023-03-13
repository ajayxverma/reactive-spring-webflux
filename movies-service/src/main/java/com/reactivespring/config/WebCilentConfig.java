package com.reactivespring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

public class WebCilentConfig {

    @Bean
    public WebClient webClient(WebClient.Builder builder){
        return builder.build();
    }
}
