package com.example.batchdatabase.reader;

import com.example.batchdatabase.model.Order;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class OrderJdbcReaderConfiguration {

    private final OrderRowMapper rowMapper;

    private final DataSource domainDataSource;

    public OrderJdbcReaderConfiguration(OrderRowMapper rowMapper, DataSource domainDataSource) {
        this.rowMapper = rowMapper;
        this.domainDataSource = domainDataSource;
    }

    @Bean
    public JdbcCursorItemReader<Order> itemReader() {
        return new JdbcCursorItemReaderBuilder<Order>()
                .dataSource(domainDataSource)
                .fetchSize(100)
                .queryTimeout(1)
                .name("orderReader")
                .sql("select id from orders")
                .rowMapper(rowMapper)
                .build();
    }

}
