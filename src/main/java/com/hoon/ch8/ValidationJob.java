package com.hoon.ch8;

import com.hoon.ch7.jdbccursor.Customer;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.adapter.ItemProcessorAdapter;
import org.springframework.batch.item.support.ClassifierCompositeItemProcessor;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.batch.item.validator.BeanValidatingItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.classify.Classifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class ValidationJob {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    /**
     * stepBuilderFactory에서 .stream(validator()) 로 넣어줌
     * @return
     */
    @Bean
    public UniqueLastNameValidator validator() {
        UniqueLastNameValidator uniqueLastNameValidator = new UniqueLastNameValidator();
        uniqueLastNameValidator.setName("validator");
        return uniqueLastNameValidator;
    }

    /**
     * Customer에 유효성 검증 애너테이션이 있을 경우 ValidationException이 발생하면서 잡 실행이 실패한다.
     * @return
     */
    @Bean
    public BeanValidatingItemProcessor<Customer> customerBeanValidatingItemProcessor() {
        return new BeanValidatingItemProcessor<>();
    }

    /**
     * ItemProcessorAdapter로 기존 서비스를 이용하여 processor 처리
     * StepBuilderFactory에서 .processor(upperItemProcessor(null)) 로 실행
     * @param service
     * @return
     */
    @Bean
    public ItemProcessorAdapter<Customer, Customer> upperItemProcessor(UpperCaseNameService service) {
        ItemProcessorAdapter<Customer, Customer> adapter = new ItemProcessorAdapter<>();
        adapter.setTargetObject(service);
        adapter.setTargetMethod("upperCase");
        return adapter;
    }

    @Bean
    public ItemProcessorAdapter<Customer, Customer> lowerItemProcessor(LowerCaseNameService service) {
        ItemProcessorAdapter<Customer, Customer> adapter = new ItemProcessorAdapter<>();
        adapter.setTargetObject(service);
        adapter.setTargetMethod("lowerCase");
        return adapter;
    }

    /**
     * CompositeItemProcessor를 이용하여 여러가지 itemProcessor를 묶어서 순서대로 실행되게 할 수 있다.
     * @return
     */
    @Bean
    public CompositeItemProcessor<Customer, Customer> itemProcessor() {
        CompositeItemProcessor<Customer, Customer> itemProcessor = new CompositeItemProcessor<>();

        itemProcessor.setDelegates(Arrays.asList(
                customerBeanValidatingItemProcessor(),
                upperItemProcessor(null)));

        return itemProcessor;
    }

    /**
     * zipcode에 따라서 itemProcessor 실행
     *
     * @return
     */
    @Bean
    public Classifier classifier() {
        return new ZipCodeClassifier(upperItemProcessor(null), lowerItemProcessor(null));
    }

    /**
     * zipcode에 따라서 itemProcessor 실행
     *
     * @return
     */
    @Bean
    public ClassifierCompositeItemProcessor<Customer, Customer> classifierCompositeItemProcessor() {
        ClassifierCompositeItemProcessor<Customer, Customer> itemProcessor = new ClassifierCompositeItemProcessor<>();
        itemProcessor.setClassifier(classifier());
        return itemProcessor;
    }

    /**
     * 커스텀 itemProcessor 구현
     * @return
     */
    @Bean
    public EvenFilteringItemProcessor evenFilteringItemProcessor() {
        return new EvenFilteringItemProcessor();
    }

}
