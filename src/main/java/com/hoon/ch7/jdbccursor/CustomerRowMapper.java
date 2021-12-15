package com.hoon.ch7.jdbccursor;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * RowMapper는 스프링 프레임워크 코어가 제공하는 JDBC 지원 표준 컴포넌트로 이름 그대로 ResultSet에서 로후 하나를 전달 받아 도메인 객체의 필드로 매핑한다.
 */
public class CustomerRowMapper implements RowMapper<Customer> {

    @Override
    public Customer mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
        Customer customer = new Customer();

        customer.setId(resultSet.getLong("id"));
        customer.setAddress(resultSet.getString("address"));
        customer.setCity(resultSet.getString("city"));
        customer.setFirstName(resultSet.getString("firstName"));
        customer.setLastName(resultSet.getString("middleInitial"));
        customer.setMiddleInitial(resultSet.getString("state"));
        customer.setZipCode(resultSet.getString("zipCode"));

        return customer;
    }

}
