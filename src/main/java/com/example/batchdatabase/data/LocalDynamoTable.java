package com.example.batchdatabase.data;

import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Profile("local")
@Order(Ordered.HIGHEST_PRECEDENCE)
public class LocalDynamoTable implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(LocalDynamoTable.class);

    private final String tableName;

    private final DynamoDB dynamoDB;

    public LocalDynamoTable(@Value("${dynamodb.tableName}") String tableName, DynamoDB dynamoDB) {
        this.tableName = tableName;
        this.dynamoDB = dynamoDB;
    }

    @Override
    public void run(String... args) throws Exception {
        LOGGER.info("Creating dynamo table");
        Table table = dynamoDB.createTable(tableName,
                List.of(new KeySchemaElement("OrderId", KeyType.HASH)),
                List.of(new AttributeDefinition("OrderId", ScalarAttributeType.N)),
                new ProvisionedThroughput(10L, 10L)); //ignored by the local dynamo
        table.waitForActive();
        LOGGER.info("Dynamo Table Created");
    }
}
