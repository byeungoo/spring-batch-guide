package com.hoon.ch7.jdbccursor;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.ArgumentPreparedStatementSetter;
import org.springframework.jdbc.core.PreparedStatementSetter;

import javax.sql.DataSource;

/**
 * 대량의 데이터를 처리할 때라면 매번 요청을 할 때마다 네트워크 오버헤드가 추가되는 단점이 있다.
 * 또한 ResultSet은 스레드 안전이 보장되지 않으믈 다중 스레드 환경에서는 사용할 수 없다.
 * 페이징을 선택하는게 더 나은듯
 */
@Configuration
public class JdbcCursorConfiguration {

    @Bean
    public JdbcCursorItemReader<Customer> cursorItemReader(DataSource dataSource) {
        return new JdbcCursorItemReaderBuilder<Customer>()
                .name("customerItemReader")
                .dataSource(dataSource)
                .sql("select * from customer where city = ?")
                .rowMapper(new CustomerRowMapper())
                .preparedStatementSetter(citySetter(null))
                .build();
    }

    @Bean
    @StepScope
    public PreparedStatementSetter citySetter(@Value("#{jobParameters['city']}") String city) {
        return new ArgumentPreparedStatementSetter(new Object[] {city});
    }

}
