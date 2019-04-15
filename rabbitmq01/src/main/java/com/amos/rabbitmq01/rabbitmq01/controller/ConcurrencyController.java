package com.amos.rabbitmq01.rabbitmq01.controller;

import com.amos.rabbitmq01.rabbitmq01.response.BaseResponse;
import com.amos.rabbitmq01.rabbitmq01.response.StatusCode;
import com.amos.rabbitmq01.rabbitmq01.service.InitService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConcurrencyController {

    private static final Logger log= LoggerFactory.getLogger(HelloWorldController.class);

    private static final String Prefix="concurrency";

    @Autowired
    private InitService initService;

    @RequestMapping(value = Prefix+"/robbing/thread",method = RequestMethod.GET)
    public BaseResponse robbingThread(){
        BaseResponse response=new BaseResponse(StatusCode.Success);
        initService.generateMultiThread();
        return response;
    }
}
