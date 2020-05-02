package com.example.batchdatabase.reader;


import com.example.batchdatabase.model.Order;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class OrderRowMapper implements RowMapper<Order> {

    private static final String ID_COLUMN = "id";

    @Override
    public Order mapRow(ResultSet rs, int rowNum) throws SQLException {
        Order order = new Order();
        order.setId(rs.getLong(ID_COLUMN));
        return order;
    }
}
