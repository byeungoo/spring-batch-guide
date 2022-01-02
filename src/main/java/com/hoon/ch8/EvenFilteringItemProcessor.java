package com.hoon.ch8;

import com.hoon.ch7.jdbccursor.Customer;
import org.springframework.batch.item.ItemProcessor;

/**
 * 홀수 우편 번호만 처리하도록 필터링하기
 */
public class EvenFilteringItemProcessor implements ItemProcessor<Customer, Customer> {

    @Override
    public Customer process(Customer item) throws Exception {
        return Integer.parseInt(item.getZipCode()) % 2 == 0 ? null : item;
    }

}
