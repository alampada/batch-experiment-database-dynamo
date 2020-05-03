package com.example.batchdatabase;

import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.TableWriteItems;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.ArgumentMatchers.any;

@SpringBatchTest
@RunWith(SpringRunner.class)
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
        Assert.assertEquals("COMPLETED", jobExecution.getExitStatus().getExitCode());
        Mockito.verify(dynamoDB, Mockito.times(1)).batchWriteItem(any(TableWriteItems.class));
    }


}
