package com.example.batchdatabase;

import com.amazonaws.services.dynamodbv2.model.AmazonDynamoDBException;
import com.example.batchdatabase.model.Order;
import com.example.batchdatabase.model.OrderWithProducts;
import com.example.batchdatabase.processor.AddProductsProcessor;
import com.example.batchdatabase.writter.dynamo.OrderWithProductsDynamoWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableBatchProcessing
@Configuration
public class BatchProcessConfiguration {

    @Bean
    public Job job(JobBuilderFactory jobBuilderFactory,
                   Step step) {
        return jobBuilderFactory
                .get("job")
                .flow(step)
                .end()
                .build();
    }

    @Bean
    public Step step(
            StepBuilderFactory stepBuilderFactory,
            JdbcCursorItemReader<Order> itemReader,
            AddProductsProcessor addProductsProcessor,
            OrderWithProductsDynamoWriter orderWithProductsDynamoWriter) {
        return stepBuilderFactory
                .get("step")
                .<Order, OrderWithProducts>chunk(25)
                .faultTolerant()
                .retryLimit(2)
                .retry(AmazonDynamoDBException.class)
                .reader(itemReader)
                .processor(addProductsProcessor)
                .writer(orderWithProductsDynamoWriter)
                .build();
    }
}
