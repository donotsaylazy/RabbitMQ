package com.amos.rabbitmq01.rabbitmq01.rabbit;

import com.amos.rabbitmq01.rabbitmq01.dto.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class CommonListener {
    private static final Logger log= LoggerFactory.getLogger(CommonListener.class);

    @Autowired
    private ObjectMapper objectMapper;


    /**
     * 监听消费消息
     * @param message
     */
    @RabbitListener(queues = "${basic.info.mq.queue.name}",containerFactory = "singleListenerContainer")
    public void  consumerMessage(@Payload byte[] message){
        try {
           /* String result=new String(message,"UTF-8");
            log.info("接收到消息：{}",result);*/

           /*User user=objectMapper.readValue(message,User.class);
            log.info("接收到消息：{}",user);*/

            Map<String,Object> resMap=objectMapper.readValue(message,Map.class);
            log.info("接收到消息：{}",resMap);
        }catch (Exception e){
            log.error("监听消息异常：",e.fillInStackTrace());
        }
    }
}
