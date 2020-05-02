package com.example.batchdatabase.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

@Component
// so that it runs before the jobs
@Order(Ordered.HIGHEST_PRECEDENCE)
public class DemoData implements CommandLineRunner {

    private static Logger logger = LoggerFactory.getLogger(DemoData.class);

    private int numberOfOrders;

    private int maxProductsPerOrder;


    private final JdbcTemplate jdbcTemplate;

    public DemoData(JdbcTemplate jdbcTemplate,
                    @Value("${demodata.numberOfOrders}") int numberOfOrders,
                    @Value("${demodata.maxProductsPerOrder}") int maxProductsPerOrder ) {
        this.jdbcTemplate = jdbcTemplate;
        this.numberOfOrders = numberOfOrders;
        this.maxProductsPerOrder = maxProductsPerOrder;
    }

    @Override
    public void run(String... args) throws Exception {
        logger.info("data load started");
        Random r = new Random();
        List<Long> orderIds = LongStream.range(1, numberOfOrders).boxed().collect(Collectors.toList());
        jdbcTemplate.batchUpdate("insert into orders (id) values (?)",
                orderIds,
                12,
                (ps, id) -> ps.setLong(1, id));
        logger.info("orders added");
        for (long i = 1;  i < numberOfOrders; i++) {
            long orderId = i;
            List<Long> productIds = LongStream.range(1, r.nextInt(maxProductsPerOrder)).boxed().collect(Collectors.toList());
            jdbcTemplate.batchUpdate(
                  "insert into ordercontents (productid, orderid) values (?, ?)",
                    productIds,
                    1000,
                    (ps, id) -> {
                      ps.setLong(1, id);
                      ps.setLong(2, orderId);
                    }
            );
        }
        logger.info("data load complete");
    }
}
