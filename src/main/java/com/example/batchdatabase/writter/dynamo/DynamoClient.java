package com.example.batchdatabase.writter.dynamo;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.util.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
public class DynamoClient {

    @Bean
    public AmazonDynamoDB client(@Value("${dynamodb.endpoint:}") String dynamoEndpoint) {
        AmazonDynamoDBClientBuilder amazonDynamoDBClientBuilder = AmazonDynamoDBClientBuilder
                .standard();
        if (!StringUtils.isNullOrEmpty(dynamoEndpoint)) {
                amazonDynamoDBClientBuilder
                        .withEndpointConfiguration(
                                new AwsClientBuilder.EndpointConfiguration(dynamoEndpoint, "us-west-2"));
        }
        return amazonDynamoDBClientBuilder.build();
    }

    @Bean
    public DynamoDB dynamoDB(AmazonDynamoDB client) {
        return new DynamoDB(client);
    }
}
