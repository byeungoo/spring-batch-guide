package com.hoon.ch7.springdatarepository;


import com.hoon.ch7.jdbccursor.Customer;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Sort;

import java.util.Collections;

public class SpringDataRepositoryConfiguration {

    @Bean
    @StepScope
    public RepositoryItemReader<Customer> customerItemReader(CustomerRepository repository, @Value("#{jobParameters['city']}") String city) {
        return new RepositoryItemReaderBuilder<Customer>()
                .name("customerItemReader")
                .arguments(Collections.singletonList(city))
                .methodName("findByCity")
                .repository(repository)
                .sorts(Collections.singletonMap("lastName", Sort.Direction.ASC))
                .build();
    }

}
