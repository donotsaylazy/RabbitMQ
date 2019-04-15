package com.amos.rabbitmq01.rabbitmq01.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
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

    @Autowired
    private Environment env;

    @Autowired
    private CachingConnectionFactory connectionFactory;

    @Autowired
    private SimpleRabbitListenerContainerFactoryConfigurer factoryConfigurer;

    /**
     * 单一消费者
     * @return
     */
    @Bean(name = "singleListenerContainer")
    public SimpleRabbitListenerContainerFactory listenerContainer(){
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(new Jackson2JsonMessageConverter());
        factory.setConcurrentConsumers(1);
        factory.setMaxConcurrentConsumers(1);
        factory.setPrefetchCount(1);
        factory.setTxSize(1);
        return factory;
    }

    /**
     * 多个消费者
     * @return
     */
    @Bean(name = "multiListenerContainer")
    public SimpleRabbitListenerContainerFactory multiListenerContainer(){
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factoryConfigurer.configure(factory,connectionFactory);
        factory.setMessageConverter(new Jackson2JsonMessageConverter());
        factory.setAcknowledgeMode(AcknowledgeMode.NONE);
        factory.setConcurrentConsumers(env.getProperty("spring.rabbitmq.listener.concurrency",int.class));
        factory.setMaxConcurrentConsumers(env.getProperty("spring.rabbitmq.listener.max-concurrency",int.class));
        factory.setPrefetchCount(env.getProperty("spring.rabbitmq.listener.prefetch",int.class));
        return factory;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(){
        connectionFactory.setPublisherConfirms(true);
        connectionFactory.setPublisherReturns(true);
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMandatory(true);
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
                log.info("消息发送成功:correlationData({}),ack({}),cause({})",correlationData,ack,cause);
            }
        });
        rabbitTemplate.setReturnCallback(new RabbitTemplate.ReturnCallback() {
            @Override
            public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
                log.info("消息丢失:exchange({}),route({}),replyCode({}),replyText({}),message:{}",exchange,routingKey,replyCode,replyText,message);
            }
        });
        return rabbitTemplate;
    }

    /**
     * 基本消息模型构建
     * 队列和交换机绑定
     */
    /**
     * 声明交换机
     * @return
     */
    @Bean
    public DirectExchange basicExchange(){
        return new DirectExchange(env.getProperty("basic.info.mq.exchenge.name"),true,false,null);
    }

    /**
     * 声明消息队列
     */
    @Bean(name = "basicQueue")
    public Queue basicQueue(){
        return new Queue(env.getProperty("basic.info.mq.queue.name"),true);
    }

    /**
     * 交换机和队列绑定
     */
    @Bean
    public Binding basicBinding(){
        return  BindingBuilder.bind(basicQueue()).to(basicExchange()).with(env.getProperty("basic.info.mq.routing.key.name"));
    }


    /**
     * 模拟抢单消息队列模型创建
     */
    @Bean
    public DirectExchange robbingExchange(){
        return new DirectExchange(env.getProperty("product.robbing.mq.exchange.name"),true,false,null);
    }


    @Bean(name = "robbingQueue")
    public Queue robbingQueue(){
        return new Queue(env.getProperty("product.robbing.mq.queue.name"),true);
    }

    @Bean
    public Binding robbingBinding(){
        return BindingBuilder.bind(robbingQueue()).to(robbingExchange()).with(env.getProperty("product.robbing.mq.routing.key.name"));
    }

}
