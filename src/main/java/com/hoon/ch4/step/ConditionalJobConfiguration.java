package com.hoon.ch4.step;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ConditionalJobConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job conditionalJob() {
        return jobBuilderFactory.get("conditionalJob")
                .incrementer(new RunIdIncrementer())
                .start(firstStep())
                .on("Failed").end()
                //.on("Failed").stopAndRestart(successStep())  // stop 상태로 job 종료. 잡을 재실행하면 successsStep에서 부터 실행
                .from(firstStep()).on("*").to(successStep())
                .end()
                .build();
    }

    public Step successStep() {
        return stepBuilderFactory.get("successStep")
                .tasklet(succeessTasklet())
                .build();
    }

    @Bean
    public Step firstStep() {
        return stepBuilderFactory.get("firstStep")
                .tasklet(passTasklet())
                .build();
    }

    @Bean
    public Tasklet passTasklet() {
        return (stepContribution, chunkContext) -> {
            //return RepeatStatus.FINISHED;                     // 성공 상태로 job 종료
            throw new RuntimeException("Causing a failure");    // failed 상태로 job 종료
        };
    }

    @Bean
    public Tasklet succeessTasklet() {
        return (contribution, chunkContext) -> {
            System.out.println("Success!");
            return RepeatStatus.FINISHED;
        };
    }

    @Bean
    public Tasklet failTasklet() {
        return (contribution, chunkContext) -> {
          return RepeatStatus.FINISHED;
        };
    }

    @Bean
    public Step failureStep() {
        return stepBuilderFactory.get("failureStep")
                .tasklet(failTasklet())
                .build();
    }

    @Bean
    public JobExecutionDecider decider() {
        return new RandomDecider();
    }

}
