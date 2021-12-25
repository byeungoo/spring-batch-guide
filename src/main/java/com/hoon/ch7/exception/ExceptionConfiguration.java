package com.hoon.ch7.exception;

import com.hoon.ch7.jdbccursor.Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
public class ExceptionConfiguration {

    private final StepBuilderFactory stepBuilderFactory;
    private final  DataSource dataSource;

    @Bean
    public Step copyFileStep() {
        return stepBuilderFactory.get("copyFileStep")
                .<Customer, Customer>chunk(10)
                .reader(mockItemReader(dataSource))
                .writer(mockItemWriter(dataSource))
                .faultTolerant()
                .skip(ParseException.class) // parseException을 10개까지 건너뛰는 구성
                .skipLimit(10)
                .build();
    }

    @Bean
    public Step copyFileStep2() {
        return stepBuilderFactory.get("copyFileStep")
                .<Customer, Customer>chunk(10)
                .reader(mockItemReader(dataSource))
                .writer(mockItemWriter(dataSource))
                .faultTolerant()
                .skip(Exception.class)
                .noSkip(ParseException.class)   // ParseException을 제외한 모든 예외 건너 뛰기
                .skipLimit(10)  // ParseException 제외하고 10번까지 건너 뛸 수 있다.
                .build();
    }

    @Bean("mockitemReader")
    public JdbcCursorItemReader<Customer> mockItemReader(DataSource dataSource) {
        return new JdbcCursorItemReaderBuilder<Customer>()
                .build();
    }

    @Bean("mockItemWriter")
    public ItemWriter mockItemWriter(DataSource dataSource) {
        return new JpaItemWriterBuilder<Customer>()
                .build();
    }

}
