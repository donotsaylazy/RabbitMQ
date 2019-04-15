package com.amos.rabbitmq01.rabbitmq01.controller;

import com.amos.rabbitmq01.rabbitmq01.dto.User;
import com.amos.rabbitmq01.rabbitmq01.response.BaseResponse;
import com.amos.rabbitmq01.rabbitmq01.response.StatusCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Declarable;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class RabbitmqController {
    private static final Logger log= LoggerFactory.getLogger(HelloWorldController.class);

    private static final String Prefix="rabbit";

    @Autowired
    private Environment env;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ObjectMapper objectMapper;


    /**
     * 发送简单消息
     * @param message
     * @return
     */
    @RequestMapping(value = Prefix+"/simple/message/send",method = RequestMethod.GET)
    public BaseResponse sendSimpleMessage(@RequestParam String message){
            BaseResponse response=new BaseResponse(StatusCode.Success);
            try {
                log.info("待发送的消息：{}",message);

                rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
                rabbitTemplate.setExchange(env.getProperty("basic.info.mq.exchenge.name"));
                rabbitTemplate.setRoutingKey(env.getProperty("basic.info.mq.routing.key.name"));

                Message msg= MessageBuilder.withBody(message.getBytes("UTF-8")).build();
                rabbitTemplate.send(msg);


            }catch (Exception e){
                log.error("发送简单消息发送异常:"+e.fillInStackTrace());
            }

            return response;
    }


    /**
     * 发送对象消息
     * @param user
     * @return
     */
    @RequestMapping(value = Prefix+"/object/message/send",method = RequestMethod.POST)
    public BaseResponse sendObjectMessage(@RequestBody User user){
        BaseResponse baseResponse=new BaseResponse(StatusCode.Success);
        try {
            log.info("待发送的消息:",user);

            rabbitTemplate.setExchange(env.getProperty("basic.info.mq.exchenge.name"));
            rabbitTemplate.setRoutingKey(env.getProperty("basic.info.mq.routing.key.name"));
            rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());

            Message message=MessageBuilder.withBody(objectMapper.writeValueAsBytes(user)).setDeliveryMode(MessageDeliveryMode.PERSISTENT).build();

            rabbitTemplate.send(message);
        }catch (Exception e){
            log.error("发送对象消息发生异常:",e.fillInStackTrace());
        }


        return baseResponse;
        }


    /**
     *  发送多类型字段
     * @return
     */
    @RequestMapping(value = Prefix+"/multi/message/send",method = RequestMethod.POST)
    public BaseResponse sednMultiTypeMessage(){
            BaseResponse baseResponse=new BaseResponse(StatusCode.Success);
            try{
                Integer id=120;
                String name="i喜喜";
                Long idd=12000l;
                Map<String,Object> mapp=new HashMap<>();

                mapp.put("id",id);
                mapp.put("name",name);
                mapp.put("idd",idd);

                rabbitTemplate.setExchange(env.getProperty("basic.info.mq.exchenge.name"));
                rabbitTemplate.setRoutingKey(env.getProperty("basic.info.mq.routing.key.name"));
                rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());

                Message mes=MessageBuilder.withBody(objectMapper.writeValueAsBytes(mapp)).setDeliveryMode(MessageDeliveryMode.PERSISTENT).build();

                rabbitTemplate.convertAndSend(mes);


            }catch (Exception e){
                log.error("发送多字段消息异常：",e.fillInStackTrace());
            }

            return baseResponse;
        }

}
