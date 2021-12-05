package org.ggyool.toby.learningtest.helloproxy;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.jupiter.api.Test;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;

import java.lang.reflect.Proxy;

import static org.assertj.core.api.Assertions.assertThat;

public class HelloTest {

    @Test
    void upperHelloProxy() {
        Hello proxy = new HelloUppercase(new HelloTarget());
        assertThat(proxy.sayHello("bear")).isEqualTo("HELLO BEAR");
        assertThat(proxy.sayHi("bear")).isEqualTo("HI BEAR");
        assertThat(proxy.sayThankYou("bear")).isEqualTo("THANK YOU BEAR");
    }

    @Test
    void dynamicProxyWithHandler() {
        Hello proxy = (Hello) Proxy.newProxyInstance(
                getClass().getClassLoader(), // 동적으로 생성되는 다이나믹 프록시 클래스의 로딩에 사용될 클래스 로더
                new Class[]{Hello.class}, // 구현할 인터페이스
                new UppercaseHandler(new HelloTarget()) // 부가기능과 위임 코드를 담은 InvocationHandler
        );

        assertThat(proxy.sayHello("bear")).isEqualTo("HELLO BEAR");
        assertThat(proxy.sayHi("bear")).isEqualTo("HI BEAR");
        assertThat(proxy.sayThankYou("bear")).isEqualTo("THANK YOU BEAR");
    }

    @Test
    void dynamicProxyWithProxyFactoryBean() {
        ProxyFactoryBean proxyFactoryBean = new ProxyFactoryBean();
        proxyFactoryBean.setTarget(new HelloTarget());
        proxyFactoryBean.addAdvice(new UppercaseAdvice());

        Hello proxy = (Hello) proxyFactoryBean.getObject();

        assertThat(proxy.sayHello("bear")).isEqualTo("HELLO BEAR");
        assertThat(proxy.sayHi("bear")).isEqualTo("HI BEAR");
        assertThat(proxy.sayThankYou("bear")).isEqualTo("THANK YOU BEAR");
    }

    @Test
    void dynamicProxyWithAdviceAndPointcut() {
        ProxyFactoryBean proxyFactoryBean = new ProxyFactoryBean();
        proxyFactoryBean.setTarget(new HelloTarget());

        // 스프링에서 제공하는 메서드 이름으로 advice 를 결정하는 포인트
        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        pointcut.setMappedName("sayH*");

        MethodInterceptor advice = new UppercaseAdvice();

        // pointcut + advice == advisor
        proxyFactoryBean.addAdvisor(new DefaultPointcutAdvisor(pointcut, advice));

        Hello proxy = (Hello) proxyFactoryBean.getObject();

        assertThat(proxy.sayHello("bear")).isEqualTo("HELLO BEAR");
        assertThat(proxy.sayHi("bear")).isEqualTo("HI BEAR");
        assertThat(proxy.sayThankYou("bear")).isEqualTo("Thank You bear");
    }

    private static class UppercaseAdvice implements MethodInterceptor {

        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            // 리플렉션의 Method 와 달리 메서드 실행 시 타깃 오브젝트를 전달할 필요가 없다.
            // MethodInvocation 은 메서드 정보와 함꼐 타깃 오브젝트를 알고 있기 때문이다.
            String ret = (String) invocation.proceed();
            return ret.toUpperCase();
        }
    }
}
