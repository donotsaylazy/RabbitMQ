package com.amos.rabbitmq01.rabbitmq01.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * Created by steadyjack on 2018/8/20.
 */
@Configuration
public class RabbitmqConfig {

    private static final Logger log= LoggerFactory.getLogger(RabbitmqConfig.class);


}
