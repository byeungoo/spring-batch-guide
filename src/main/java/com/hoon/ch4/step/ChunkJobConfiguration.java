package com.hoon.ch4.step;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.batch.repeat.CompletionPolicy;
import org.springframework.batch.repeat.policy.CompositeCompletionPolicy;
import org.springframework.batch.repeat.policy.SimpleCompletionPolicy;
import org.springframework.batch.repeat.policy.TimeoutTerminationPolicy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class ChunkJobConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job chunkBasedJob() {
        return jobBuilderFactory.get("chunkBasedJob")
                .incrementer(new RunIdIncrementer())
                .start(chunkStep())
                .build();
    }

    @Bean
    public Step chunkStep() {
        return stepBuilderFactory.get("chunkStep")
                //.<String, String>chunk(1000)        // chunk 사이즈 크기 하드 코딩
                .<String, String>chunk(completionPolicy())    // chunk 사이즈 크기 하드 코딩
                .reader(itemReader())
                .writer(itemWriter())
                .listener(new LoggingStepStartStopListener())   // Step Listener
                .build();
    }

    @Bean
    public ItemReader<String> itemReader() {
        List<String> items = new ArrayList<>(100000);

        for (int i = 0; i < 100000; i++) {
            items.add(UUID.randomUUID().toString());
        }

        return new ListItemReader<>(items);
    }

    @Bean
    public ItemWriter<String> itemWriter() {
        return items -> {
            for (String item : items) {
                log.info(">> current item = {}", item);
            }
        };
    }

    @Bean
    public CompletionPolicy completionPolicy() {
        CompositeCompletionPolicy policy = new CompositeCompletionPolicy();

        policy.setPolicies(new CompletionPolicy[]{
                new TimeoutTerminationPolicy(3), new SimpleCompletionPolicy(1000)
        });

        return policy;
    }

}