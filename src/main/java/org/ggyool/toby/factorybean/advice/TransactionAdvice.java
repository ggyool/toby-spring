package org.ggyool.toby.factorybean.advice;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

public class TransactionAdvice implements MethodInterceptor {

    private PlatformTransactionManager transactionManager;


    // 타깃을 호출하는 기능을 가진 콜백 오브젝트 프록로부터 받는다. 덕분에 어드바이스는 타깃에 의존하지 않게 되고 공유하여 사용할 수 있다.
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            // 콜백을 호출해서 타깃의 메소드를 실행한다. 타깃 메서드의 호출 전후로 필요한 부가기능을 넣을 수 있다.
            // 경우에 따라서 아예 호출되지 않게 하거나 재시도를 위한 반복적인 호출도 가능하다.
            Object ret = invocation.proceed();
            transactionManager.commit(status);
            return ret;
        }
        // 이전과 달리 예외가 포장되지 않고 그대로 전달된다.
        catch (RuntimeException e) {
            transactionManager.rollback(status);
            throw e;
        }
    }

    public void setTransactionManager(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }
}
