package com.hoon.ch4.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class SharedExecutionConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job executionContextJob() {
        return jobBuilderFactory.get("sharedExecutionJob2")
                .incrementer(new RunIdIncrementer())
                .start(executionContextStep())
                .next(shareStep2())
                .build();
    }

    @Bean
    public Step executionContextStep() {
        return stepBuilderFactory.get("sharedExecutionStep")
                .tasklet(new ExecutionContextModifyTasklet())
                .build();
    }

    @Bean
    public Step shareStep2(){
        return stepBuilderFactory.get("shareStep2")       //step 이름 설정
                .tasklet((contribution, chunkContext) -> {      //tasklet이라는 step의 실행 단위를 설정 해야함

                    StepExecution stepExecution = contribution.getStepExecution();
                    ExecutionContext stepExecutionContext = stepExecution.getExecutionContext();

                    JobExecution jobExecution = stepExecution.getJobExecution();
                    ExecutionContext jobExecutionContext = jobExecution.getExecutionContext();

                    log.info("jobUsername : {}, stepUsername : {}",
                            jobExecutionContext.getString("user.name", "emptyJobUsername"),            // job execution은 step끼리 공유됨.
                            stepExecutionContext.getString("user.name", "emptyStepUsername"));         // step execution에 공유 안됨

                    return RepeatStatus.FINISHED;
                })
                .build();
    }

}
