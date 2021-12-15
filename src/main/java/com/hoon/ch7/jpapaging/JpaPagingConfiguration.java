package com.hoon.ch7.jpapaging;

import com.hoon.ch7.jdbccursor.Customer;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import javax.persistence.EntityManagerFactory;
import java.util.Collections;

public class JpaPagingConfiguration {

    @Bean
    @StepScope
    public JpaPagingItemReader<Customer> customerJpaPagingItemReader(EntityManagerFactory entityManagerFactory,
                                                                     @Value("#{jobParameters['city']}") String city) {

        return new JpaPagingItemReaderBuilder<Customer>()
                .name("customerItemReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString("select c from Customer c where c.city = :city")
                .parameterValues(Collections.singletonMap("city", city))
                .build();
    }

    /**
     * JpaQueryProvier 사용
     */
    @Bean
    @StepScope
    public JpaPagingItemReader<Customer> customerItemReader(EntityManagerFactory entityManagerFactory,
                                                            @Value("#{jobParameters['city']}") String city) {

        CustomerByCityQueryProvider queryProvider = new CustomerByCityQueryProvider();
        queryProvider.setCityName(city);

        return new JpaPagingItemReaderBuilder<Customer>()
                .name("customerItemReader")
                .entityManagerFactory(entityManagerFactory)
                .queryProvider(queryProvider)
                .parameterValues(Collections.singletonMap("city", city))
                .build();
    }

}
