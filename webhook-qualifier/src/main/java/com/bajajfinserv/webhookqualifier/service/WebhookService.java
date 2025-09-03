package com.bajajfinserv.webhookqualifier.service;

import com.bajajfinserv.webhookqualifier.dto.WebhookRequest;
import com.bajajfinserv.webhookqualifier.dto.WebhookResponse;
import com.bajajfinserv.webhookqualifier.dto.SolutionRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class WebhookService {
    
    private final WebClient webClient;
    
    @Autowired
    public WebhookService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }
    
    public Mono<WebhookResponse> generateWebhook(WebhookRequest request) {
        return webClient
                .post()
                .uri("https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(WebhookResponse.class);
    }
    
    public Mono<String> submitSolution(String webhookUrl, String accessToken, String finalQuery) {
        SolutionRequest solutionRequest = new SolutionRequest(finalQuery);
        
        return webClient
                .post()
                .uri(webhookUrl)
                .header(HttpHeaders.AUTHORIZATION, accessToken)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(solutionRequest)
                .retrieve()
                .bodyToMono(String.class);
    }
}