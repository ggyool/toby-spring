package org.ggyool.toby.learningtest.messagefactorybean;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = "/message-factory-bean.xml")
public class MessageFactoryBeanTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void getMessageFromFactoryBean() {
        Object message = applicationContext.getBean("message");
        assertThat(message).isInstanceOf(Message.class);
        assertThat(((Message) message).getText()).isEqualTo("Factory Bean Test");
    }

    @Test
    void getFactoryBean() {
        // &를 붙이면 Factory Bean 을 가져올 수 있다.
        Object message = applicationContext.getBean("&message");
        assertThat(message).isInstanceOf(MessageFactoryBean.class);
    }

}
