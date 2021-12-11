package com.hoon.ch4.job;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;

/**
 * ExecutionContext 조작 예제
 */
public class ExecutionContextModifyTasklet implements Tasklet {

    private static final String HELLO_WORLD = "Hello, %s";

    @Override
    public RepeatStatus execute(StepContribution step, ChunkContext context) throws Exception {

        String name = (String) context.getStepContext()
                .getJobParameters()
                .get("name");

        ExecutionContext stepContext = context.getStepContext()
                .getStepExecution()
                .getExecutionContext();

        stepContext.put("user.name", name); // step의 execution context에 name 데이터 추가

        ExecutionContext jobContext = context.getStepContext()
                .getStepExecution()
                .getJobExecution()
                .getExecutionContext();

        jobContext.put("user.name", name);  // job의 execution context에 name 데이터 추가

        System.out.println(String.format(HELLO_WORLD, name));
        return RepeatStatus.FINISHED;
    }

}
