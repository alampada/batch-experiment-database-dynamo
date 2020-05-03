package com.example.batchdatabase;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.TableWriteItems;

import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.dynamodb.DynaliteContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBatchTest
@Testcontainers
@ContextConfiguration(classes = {BatchDatabaseApplication.class, JobEndToEndTest.DynaliteConfiguration.class})
@ActiveProfiles("test")
@TestPropertySource("classpath:application-test.properties")
public class JobEndToEndTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Container
    private static DynaliteContainer dynamo = new DynaliteContainer();


    @Autowired
    private DynamoDB dynamoDB;

    @Test
    public void testJob() throws Exception {
        //given:
        jdbcTemplate.update("insert into orders (id) values (1)");
        jdbcTemplate.update("insert into ordercontents (productid, orderid) values (1, 1)");
        jdbcTemplate.update("insert into ordercontents (productid, orderid) values (2, 1)");
        Table table = dynamoDB.createTable("batch-demo-orders",
                List.of(new KeySchemaElement("OrderId", KeyType.HASH)),
                List.of(new AttributeDefinition("OrderId", ScalarAttributeType.N)),
                new ProvisionedThroughput(10L, 10L)); //ignored by the local dynamo
        table.waitForActive();

        //when:
        JobExecution jobExecution = jobLauncherTestUtils.launchJob();

        //then:
        assertThat(jobExecution.getExitStatus().getExitCode()).isEqualTo("COMPLETED");
    }

    @Configuration
    public static class DynaliteConfiguration {

        @Bean
        public AmazonDynamoDB amazonDynamoDB() {
            return dynamo.getClient();
        }

        @Bean
        public DynamoDB dynamoDB(AmazonDynamoDB amazonDynamoDB) {
            return new DynamoDB(amazonDynamoDB);
        }
    }


}
