package com.example.batchdatabase.writter.dynamo;

import com.amazonaws.services.dynamodbv2.document.BatchWriteItemOutcome;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.TableWriteItems;
import com.amazonaws.services.dynamodbv2.model.AmazonDynamoDBException;
import com.example.batchdatabase.model.OrderWithProducts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderWithProductsDynamoWriter implements ItemWriter<OrderWithProducts> {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderWithProductsDynamoWriter.class);

    private final String tableName;

    private final DynamoDB dynamoDb;

    public OrderWithProductsDynamoWriter(
            @Value("${dynamodb.tableName}") String tableName,
            DynamoDB dynamoDb) {
        this.tableName = tableName;
        this.dynamoDb = dynamoDb;
    }

    @Override
    public void write(List<? extends OrderWithProducts> items) throws Exception {
        LOGGER.info("called with " + items);
        LOGGER.info(String.format("Writing %d items to dynamo", items.size()));
        try {
            List<Item> itemsToPut = items.stream()
                    .map(i -> new Item().withPrimaryKey("OrderId", i.getOrderId())
                            .withList("products", i.getProductIds()))
                    .collect(Collectors.toList());
            TableWriteItems tableWriteItems = new TableWriteItems(tableName)
                    .withItemsToPut(itemsToPut);
            BatchWriteItemOutcome batchWriteItemOutcome = dynamoDb.batchWriteItem(tableWriteItems);
            LOGGER.info("Result: " + batchWriteItemOutcome);
        }
        catch (AmazonDynamoDBException e) {
            LOGGER.error("dynamo failed");
            throw e;
        }
    }
}
