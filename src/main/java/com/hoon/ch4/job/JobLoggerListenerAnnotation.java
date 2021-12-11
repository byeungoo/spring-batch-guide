package com.hoon.ch4.job;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.annotation.AfterJob;
import org.springframework.batch.core.annotation.BeforeJob;

/**
 * 애너테이션 기반으로 배치 실행 전/후에 특정 테스크 실행 가능
 */
public class JobLoggerListenerAnnotation {

    @BeforeJob
    public void beforeJob(JobExecution jobExecution) {
        System.out.println("before job");
    }

    @AfterJob
    public void afterJob(JobExecution jobExecution) {
        System.out.println("after job");
    }

}
