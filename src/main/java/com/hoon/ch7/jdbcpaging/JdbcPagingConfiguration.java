package com.hoon.ch7.jdbcpaging;

import com.hoon.ch7.jdbccursor.Customer;
import com.hoon.ch7.jdbccursor.CustomerRowMapper;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class JdbcPagingConfiguration {

    @Bean
    @StepScope
    public JdbcPagingItemReader<Customer> customerJdbcPagingItemReader(DataSource dataSource, PagingQueryProvider queryProvider,
                                                                       @Value("#{jobParameters['city']}") String city) {

        Map<String, Object> parameterValues = new HashMap<>(1);
        parameterValues.put("city", city);

        return new JdbcPagingItemReaderBuilder<Customer>()
                .name("customerItemReader")
                .dataSource(dataSource)
                .queryProvider(queryProvider)
                .parameterValues(parameterValues)
                .pageSize(10)
                .rowMapper(new CustomerRowMapper())
                .build();
    }

    @Bean
    public SqlPagingQueryProviderFactoryBean pagingQueryProvider(DataSource dataSource) {
        SqlPagingQueryProviderFactoryBean factoryBean = new SqlPagingQueryProviderFactoryBean();

        factoryBean.setSelectClause("select *");
        factoryBean.setFromClause("from Customer");
        factoryBean.setWhereClause("where city = :city");

        Map<String, Order> sortKeys = new HashMap<>(1);
        sortKeys.put("lastName", Order.ASCENDING);

        factoryBean.setSortKeys(sortKeys);
        factoryBean.setDataSource(dataSource);

        return factoryBean;
    }

}
