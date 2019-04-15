package com.amos.rabbitmq01.rabbitmq01.listener;

import com.amos.rabbitmq01.rabbitmq01.service.ConcurrencyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class RobbingListener {

    private static final Logger log= LoggerFactory.getLogger(RobbingListener.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ConcurrencyService concurrencyService;

    /**
     * 监听抢单消息，处理请求
     */
    @RabbitListener(queues = "${product.robbing.mq.queue.name}",containerFactory = "singleListenerContainer")
    public void consumerMessage(@Payload byte[] message){
        try{
            String mobile=new String(message,"UTF-8");
            log.info("监听到抢单手机号码{}:",mobile);

            concurrencyService.manageRobbing(String.valueOf(mobile));
        }catch (Exception e){
            log.error("监听抢单消息，发生异常：",e.fillInStackTrace());
        }
    }

}
