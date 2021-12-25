package com.hoon.ch7.listener;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterStep;

/**
 * 입력이 없을 때 에러 처리를 해야하면 FAILED 처리를해서 잡을 재실행할 수 있게 구성한다.
 * 마찬가지로 스텝에서 리스너에 등록해준다.
 */
public class EmptyInputStepFailer {

    @AfterStep
    public ExitStatus afterStep(StepExecution execution) {
        if (execution.getReadCount() > 0) {
            return execution.getExitStatus();
        } else {
            return ExitStatus.FAILED;
        }
    }

}
