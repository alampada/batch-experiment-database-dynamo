package com.example.batchdatabase.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.BatchConfigurer;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.batch.BatchDataSourceInitializer;
import org.springframework.boot.autoconfigure.batch.BatchProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

/**
 * So that we use a different db for the source of data and a different database for spring batch metadata.
 * https://stackoverflow.com/questions/25256487/how-to-java-configure-separate-datasources-for-spring-batch-data-and-business-da
 */
@Component
public class BatchConfiguration extends DefaultBatchConfigurer {

    @Autowired
    private BatchProperties batchProperties;

    public BatchConfiguration() {
    }

    public BatchConfiguration(DataSource dataSource) {
        super(dataSource);
    }

    public void setBatchProperties(BatchProperties batchProperties) {
        this.batchProperties = batchProperties;
    }

    @Override
    @Autowired
    public void setDataSource(@Qualifier("metadataDataSource") DataSource dataSource) {
        super.setDataSource(dataSource);
    }

    @Bean
    public BatchDataSourceInitializer  batchDataSourceInitializer(@Qualifier("metadataDataSource") DataSource dataSource, ResourceLoader resourceLoader){
        BatchDataSourceInitializer batchDatabaseInitializer = new  BatchDataSourceInitializer(dataSource, resourceLoader, batchProperties);
        return batchDatabaseInitializer;
    }
}
