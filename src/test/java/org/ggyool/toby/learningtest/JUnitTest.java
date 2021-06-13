package org.ggyool.toby.learningtest;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@DisplayName("테스트 오브젝트는 테스트 메서드마다 달라지고, application context는 동일한 객체를 사용한다.")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = "/junit.xml")
public class JUnitTest {

    @Autowired
    private ApplicationContext applicationContext;

    private ApplicationContext storedInitialApplicationContext;

    private Set<JUnitTest> testObjects = new HashSet<>();

    @BeforeEach
    void setUp() {
        if (Objects.isNull(storedInitialApplicationContext)) {
            storedInitialApplicationContext = applicationContext;
        }
    }

    @Test
    void test1() {
        assertThat(testObjects).doesNotContain(this);
        testObjects.add(this);

        assertThat(applicationContext).isSameAs(storedInitialApplicationContext);
    }

    @Test
    void test2() {
        assertThat(testObjects).doesNotContain(this);
        testObjects.add(this);

        assertThat(applicationContext).isSameAs(storedInitialApplicationContext);
    }

    @Test
    void test3() {
        assertThat(testObjects).doesNotContain(this);
        testObjects.add(this);

        assertThat(applicationContext).isSameAs(storedInitialApplicationContext);
    }
}
