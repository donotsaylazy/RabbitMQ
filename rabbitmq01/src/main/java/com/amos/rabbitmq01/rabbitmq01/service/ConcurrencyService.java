package com.amos.rabbitmq01.rabbitmq01.service;

import com.amos.rabbitmq01.rabbitmq01.mapper.ProductMapper;
import com.amos.rabbitmq01.rabbitmq01.mapper.ProductRobbingRecordMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConcurrencyService {

    private static final Logger log= LoggerFactory.getLogger(ConcurrencyService.class);

    private static final String ProductNo="product_10010";

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ProductRobbingRecordMapper productRobbingRecordMapper;

    public void manageRobbing(String valueOf) {
    }
}
