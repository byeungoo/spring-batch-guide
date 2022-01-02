package com.hoon.ch8;

import com.hoon.ch7.jdbccursor.Customer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.classify.Classifier;

/**
 * 경우에 따라서 processor가 다르게 동작해야하는 경우 Classifier를 사용한다.
 * Classifier의 구현체로 사용할 ItemProcessor를 선택한다.
 */
public class ZipCodeClassifier implements Classifier<Customer, ItemProcessor<Customer, Customer>> {

    private ItemProcessor<Customer, Customer> oddItemProcessor;
    private ItemProcessor<Customer, Customer> evenItemProcessor;

    public ZipCodeClassifier(ItemProcessor<Customer, Customer> oddItemProcessor,
                             ItemProcessor<Customer, Customer> evenItemProcessor) {

        this.oddItemProcessor = oddItemProcessor;
        this.evenItemProcessor = evenItemProcessor;

    }

    @Override
    public ItemProcessor<Customer, Customer> classify(Customer classifiable) {
        if(Integer.parseInt(classifiable.getZipCode()) % 2 == 0) {
            return evenItemProcessor;
        } else {
            return oddItemProcessor;
        }
    }

}
