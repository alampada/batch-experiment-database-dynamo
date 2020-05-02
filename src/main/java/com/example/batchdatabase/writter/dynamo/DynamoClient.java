package com.example.batchdatabase.writter.dynamo;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class DynamoClient {

    @Bean
    public AmazonDynamoDB client() {
        return AmazonDynamoDBClientBuilder.standard().build();
    }

    @Bean
    public DynamoDB dynamoDB(AmazonDynamoDB client) {
        return new DynamoDB(client);
    }
}
