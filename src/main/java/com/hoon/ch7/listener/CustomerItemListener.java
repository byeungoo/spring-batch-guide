package com.hoon.ch7.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.annotation.OnReadError;
import org.springframework.batch.item.file.FlatFileParseException;

/**
 * 입력 도중에 예외가 발생하면 발생한 예외와 예외가 발생한 레코드르 로그로 남겨준다.
 * 이 리스너를 step만들 때 .listener(customerListener()) 이런식으로 추가해준다.
 * 예외 건너뛸 때 이런식으로 남겨준다
 */
@Slf4j
public class CustomerItemListener {

    @OnReadError
    public void onReadError(Exception e) {
        if(e instanceof FlatFileParseException) {
            FlatFileParseException ffpe = (FlatFileParseException) e;

            StringBuilder errorMessage = new StringBuilder();
            errorMessage.append("An error occured while processing the " +
                    ffpe.getLineNumber() +
                    " line of the file. Below was the faulty " +
                    "input.\n");

            errorMessage.append(ffpe.getInput() + "\n");
            log.error(errorMessage.toString(), ffpe);
        }
    }

}
