package com.example.batchdatabase;

import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.TableWriteItems;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(SpringExtension.class)
@SpringBatchTest
@ContextConfiguration(classes = {BatchDatabaseApplication.class})
@TestPropertySource("classpath:application-test.properties")
public class JobEndToEndTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @MockBean
    DynamoDB dynamoDB;

    @Test
    public void testJob() throws Exception {
        //given:
        jdbcTemplate.update("insert into orders (id) values (1)");
        jdbcTemplate.update("insert into ordercontents (productid, orderid) values (1, 1)");
        jdbcTemplate.update("insert into ordercontents (productid, orderid) values (2, 1)");


        JobExecution jobExecution = jobLauncherTestUtils.launchJob();
        assertThat(jobExecution.getExitStatus().getExitCode()).isEqualTo("COMPLETED");
        Mockito.verify(dynamoDB, Mockito.times(1)).batchWriteItem(any(TableWriteItems.class));
    }


}
