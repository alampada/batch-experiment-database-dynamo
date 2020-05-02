package com.example.batchdatabase.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfiguration {

    @Bean
    @Primary
    @ConfigurationProperties("domain.datasource")
    public DataSource domainDataSource() {
        return DataSourceBuilder.create().build();
    }


    @Bean("metadataDataSource")
    @ConfigurationProperties("batch.datasource")
    public DataSource batchDataSource() {
        return DataSourceBuilder.create().build();
    }
}
