package com.amos.rabbitmq01.rabbitmq01.service;

import com.amos.rabbitmq01.rabbitmq01.entity.Product;
import com.amos.rabbitmq01.rabbitmq01.entity.ProductRobbingRecord;
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

    /**
     *处理订单
     * @param mobile
     */
    public void manageRobbing(String mobile) {

        /*try {
            Product product=productMapper.selectByProductNo(ProductNo);
            if (product!=null && product.getTotal()>0){
                log.info("当前手机号：{} 恭喜您抢到单了!",mobile);
                productMapper.updateTotal(product);
            }else{
                log.error("当前手机号：{} 抢不到单!",mobile);

            }
        }catch (Exception e){
            log.error("处理抢单发生异常：mobile={} ",mobile);
        }*/ //--v1.0

        try {
            Product product=productMapper.selectByProductNo(ProductNo);
            if (product!=null && product.getTotal()>0){
               int result=productMapper.updateTotal(product);
               if(result>0){
                   //添加抢单生成记录
                   ProductRobbingRecord entity=new ProductRobbingRecord();
                   entity.setMobile(mobile);
                   entity.setProductId(product.getId());
                   productRobbingRecordMapper.insertSelective(entity);
               }
            }
        }catch (Exception e){
            log.error("处理抢单发生异常：mobile={} ",mobile);
        } //--v2.0

    }
}
