package com.hoon.ch4.job;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.DefaultJobParametersValidator;
import org.springframework.batch.core.listener.JobListenerFactoryBean;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class HelloWorldConfiguration {

    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job job() {
        return jobBuilderFactory.get("helloJob8")
                .validator(validator())
                .start(step1())
                //.incrementer(new RunIdIncrementer())    // batch_job_execution_params 테이블에 long 타입을 갖는 run.id 파라미터 생성. 여러번 실행 가능하게해줌
                .incrementer(new DailyJobTimestamper())   // increment 직접 구현 가능
                //.listener(new JobLoggerListener())        // 잡 실행 전 후로 특정 로직 실행 가능. 배치의 실행에 영향을 미치면 안된다.
                .listener(JobListenerFactoryBean.getListener(new JobLoggerListenerAnnotation()))    // 애노테이션 기반으로 리스너 등록
                .build();
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .tasklet(helloWorldTasklet())
                .tasklet(helloWorldTasklet2(null))
                .build();
    }

    /**
     * job parameter 검증
     * @return
     */
    @Bean
    public JobParametersValidator validator() {
        DefaultJobParametersValidator validator = new DefaultJobParametersValidator();
        validator.setRequiredKeys(new String[] {"name"});
        //validator.setOptionalKeys(new String[] {"name"});
        return validator;
    }

    /**
     * 스프링 구성을 사용하여 JobParameter 접근
     * @return
     */
    @Bean
    public Tasklet helloWorldTasklet() {
        // stepContribution : 아직 커밋되지 않은 현재 트랜잭션에 대한 정보(쓰기 수,읽기 수 등)를 가지고있음
        // chunkContext : 실행 시점의 job상태를 제공. 처리중인 청크와 관련된 정보도 가지고 있음
        return (stepContribution, chunkContext) -> {
            String name = (String) chunkContext.getStepContext()
                    .getJobParameters()
                    .get("name");

            System.out.println(String.format("Hello, %s!", name));

            return RepeatStatus.FINISHED;
        };
    }

    /**
     * 스프링의 늦은 바인딩으로 JobParameters 코드를 참조하지 않고 잡 파라미터를 컴포넌트에 주입
     * @param name
     * @return
     */
    @Bean
    @StepScope  // 스탭 실행 범위에 들어갈 때 까지 빈 생성 지연
    public Tasklet helloWorldTasklet2(@Value("#{jobParameters[name]}") String name) {
        return (stepContribution, chunkContext) -> {
            System.out.println(String.format("Hello, %s!", name));
            return RepeatStatus.FINISHED;
        };
    }

}
