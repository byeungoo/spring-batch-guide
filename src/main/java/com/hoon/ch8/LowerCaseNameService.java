package com.hoon.ch8;

import com.hoon.ch7.jdbccursor.Customer;
import org.springframework.stereotype.Service;

/**
 * 고객 이름을 소문자로 변경하는 서비스
 */
@Service
public class LowerCaseNameService {

    public Customer lowerCase(Customer customer) {
        Customer newCustomer = new Customer(customer);
        newCustomer.setFirstName(customer.getFirstName().toUpperCase());
        newCustomer.setMiddleInitial(customer.getMiddleInitial().toUpperCase());
        newCustomer.setLastName(customer.getLastName().toUpperCase());

        return newCustomer;
    }

}
