package com.example.batchdatabase.configuration;

import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.batch.BatchDataSourceInitializer;
import org.springframework.boot.autoconfigure.batch.BatchProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;

import javax.sql.DataSource;

/**
 * So that we use a different db for the source of data and a different database for spring batch metadata.
 * https://stackoverflow.com/questions/25256487/how-to-java-configure-separate-datasources-for-spring-batch-data-and-business-da
 */
@Configuration
public class BatchConfiguration extends DefaultBatchConfigurer {

    @Override
    public void setDataSource(@Qualifier("metadataDataSource") DataSource batchDataSource) {
        super.setDataSource(batchDataSource);
    }

    @Bean
    public BatchDataSourceInitializer batchDataSourceInitializer(@Qualifier("metadataDataSource") DataSource batchDataSource,
                                                                 ResourceLoader resourceLoader) {
        return new BatchDataSourceInitializer(batchDataSource, resourceLoader, new BatchProperties());
    }
}
