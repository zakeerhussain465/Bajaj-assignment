package com.bajajfinserv.webhookqualifier.runner;

import com.bajajfinserv.webhookqualifier.dto.WebhookRequest;
import com.bajajfinserv.webhookqualifier.service.SqlSolutionService;
import com.bajajfinserv.webhookqualifier.service.WebhookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class StartupRunner implements CommandLineRunner {
    
    private static final Logger logger = LoggerFactory.getLogger(StartupRunner.class);
    
    @Autowired
    private WebhookService webhookService;
    
    @Autowired
    private SqlSolutionService sqlSolutionService;
    
    @Override
    public void run(String... args) throws Exception {
        try {
            // 🚨 UPDATE YOUR DETAILS HERE 🚨
            logger.info("Starting webhook generation...");
            WebhookRequest request = new WebhookRequest("John Smith", "REG12346", "john.smith@email.com");
            
            webhookService.generateWebhook(request)
                .flatMap(response -> {
                    logger.info("Webhook generated successfully");
                    logger.info("Webhook URL: {}", response.getWebhook());
                    logger.info("Access Token: {}", response.getAccessToken());
                    
                    String sqlSolution = sqlSolutionService.getSqlSolution(request.getRegNo());
                    logger.info("Generated SQL Solution: {}", sqlSolution);
                    
                    logger.info("Submitting solution to webhook...");
                    return webhookService.submitSolution(
                        "https://bfhldevapigw.healthrx.co.in/hiring/testWebhook/JAVA",
                        response.getAccessToken(),
                        sqlSolution
                    );
                })
                .subscribe(
                    result -> {
                        logger.info("Solution submitted successfully: {}", result);
                    },
                    error -> {
                        logger.error("Error occurred during process: ", error);
                    }
                );
                
            Thread.sleep(5000);
            
        } catch (Exception e) {
            logger.error("Error in startup process: ", e);
        }
    }
}