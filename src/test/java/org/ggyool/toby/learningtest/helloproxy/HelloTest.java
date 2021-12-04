package org.ggyool.toby.learningtest.helloproxy;

import org.junit.jupiter.api.Test;

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
    void upperHanlder() {
        Hello proxy = (Hello) Proxy.newProxyInstance(
                getClass().getClassLoader(), // 동적으로 생성되는 다이나믹 프록시 클래스의 로딩에 사용될 클래스 로더
                new Class[]{Hello.class}, // 구현할 인터페이스
                new UppercaseHandler(new HelloTarget()) // 부가기능과 위임 코드를 담은 InvocationHandler
        );

        assertThat(proxy.sayHello("bear")).isEqualTo("HELLO BEAR");
//        assertThat(proxy.sayHi("bear")).isEqualTo("HI BEAR");
//        assertThat(proxy.sayThankYou("bear")).isEqualTo("THANK YOU BEAR");
    }

}
