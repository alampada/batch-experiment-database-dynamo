package com.example.batchdatabase.processor;

import com.example.batchdatabase.model.Order;
import com.example.batchdatabase.model.OrderWithProducts;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddProductsProcessor implements ItemProcessor<Order, OrderWithProducts> {

    private final JdbcTemplate jdbcTemplate;

    public AddProductsProcessor(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public OrderWithProducts process(Order order) throws Exception {
        List<Long> productIds = jdbcTemplate.query("select productId from ordercontents where orderId = ?",
                new Object[] { order.getId() }, (rs, rowNum) -> rs.getLong("productId"));
        return new OrderWithProducts(order.getId(), productIds);
    }
}
