package com.hoon.ch4.job;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

/**
 * 배치 실행 전/후 (라이프 사이클)에 특정 로직 실행을 할 수 있게 해준다.
 */
public class JobLoggerListener implements JobExecutionListener {

    @Override
    public void beforeJob(JobExecution jobExecution) {
        System.out.println("before job");
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        System.out.println("after job");
    }
}
