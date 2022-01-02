package com.hoon.ch8;

import com.hoon.ch7.jdbccursor.Customer;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamSupport;
import org.springframework.batch.item.validator.ValidationException;
import org.springframework.batch.item.validator.Validator;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * 아이템 유효성 검증을 직접 구현하고 싶은 경우
 */
public class UniqueLastNameValidator extends ItemStreamSupport implements Validator<Customer> { // 재시작 시에도 상태를 유지하려면 유효성 검증기는 ItemStreamSupport를 상속
                                                                                                // ItemStream 인터페이스를 구현함으로써, 각 커밋과 lastName을 ExecutionContext에 저장해야한다.

    private Set<String> lastNames = new HashSet<>();

    @Override
    public void validate(Customer value) throws ValidationException {
        if(lastNames.contains(value.getLastName())) {
            throw new ValidationException("Duplicate last name was found: " + value.getLastName());
        }
        this.lastNames.add(value.getLastName());
    }


    /**
     * lastNames 필드가 이전 Execution에 저장돼 있는지 확인한다.
     * 만약 저장돼 있다면 스텝 처리가 시작되기 전에 해당 값으로 원복한다.
      * @param executionContext
     */
    @Override
    public void open(ExecutionContext executionContext) {
        String lastNames = getExecutionContextKey("lastNames");

        if(executionContext.containsKey(lastNames)) {
            this.lastNames = (Set<String>) executionContext.get(lastNames);
        }
    }

    /**
     * 다음 청크에 오류가 발생할 경우 현재 상태를 ExecutionContext에 저장한다.
     * @param executionContext
     */
    @Override
    public void update(ExecutionContext executionContext) {
        Iterator<String> itr = lastNames.iterator();
        Set<String> copiedLastNames = new HashSet<>();

        while (itr.hasNext()) {
            copiedLastNames.add(itr.next());
        }

        executionContext.put(getExecutionContextKey("lastNames"), copiedLastNames);
    }
}
