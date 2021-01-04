package com.kongfanyu.controller;

import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Copyright (C), 2018-2021
 * FileName: IndexController
 * Author:   kongfanyu
 * Date:     2021/1/3 下午5:07
 */
@RestController
public class IndexController {

    @Autowired
    private Redisson redisson;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    //版本4.0 使用redisson分布式锁
    @GetMapping("/deductStock")
    public String deductStock()  {
        String lockKey = "product_001";
        String clientId = UUID.randomUUID().toString();
        RLock redissonLock = redisson.getLock(lockKey);
        try {
            redissonLock.lock(30, TimeUnit.SECONDS);
            int stock = Integer.parseInt(stringRedisTemplate.opsForValue().get("stock"));
            if (stock > 0) {
                int realStock = stock - 1;
                stringRedisTemplate.opsForValue().set("stock", realStock + "");
                System.out.println("扣减库存成功，剩余库存:" + realStock);
            } else {
                System.out.println("扣减库存失败，库存不足。");
            }
        }finally {
            redissonLock.unlock();//释放锁
        }
        return "end";
    }
/*
    //版本3.0 使用redis分布式锁
    @GetMapping("/deductStock")
    public String deductStock() throws InterruptedException {
        String lockKey = "product_001";
        String clientId = UUID.randomUUID().toString();
        try {
            //Boolean result = stringRedisTemplate.opsForValue().setIfAbsent(lockKey, "fuzi");
            //stringRedisTemplate.expire(lockKey,10, TimeUnit.SECONDS);
            Boolean present = stringRedisTemplate.opsForValue().setIfAbsent(lockKey, clientId, 10, TimeUnit.SECONDS);
            if (!present){
                return "error";
            }

            int stock = Integer.parseInt(stringRedisTemplate.opsForValue().get("stock"));
            if (stock > 0) {
                int realStock = stock - 1;
                stringRedisTemplate.opsForValue().set("stock", realStock + "");
                System.out.println("扣减库存成功，剩余库存:" + realStock);
            } else {
                System.out.println("扣减库存失败，库存不足。");
            }
        }finally {
            if (clientId.equals(stringRedisTemplate.opsForValue().get(lockKey))){
                //释放锁
                stringRedisTemplate.delete(lockKey);
            }
        }

        return "end";
    }*/

    /*
    //版本v2.0 使用同步锁机制--不能解决集群情况下的数据一致性
    @GetMapping("/deductStock")
    public String deductStock() throws InterruptedException{
        synchronized (this) {
            int stock = Integer.parseInt(stringRedisTemplate.opsForValue().get("stock"));
            if (stock > 0) {
                int realStock = stock - 1;
                stringRedisTemplate.opsForValue().set("stock", realStock + "");
                System.out.println("扣减库存成功，剩余库存:" + realStock);
            } else {
                System.out.println("扣减库存失败，库存不足。");
            }
        }
        return "end";
    }*/

    /*
    //版本v1.0 扣减数量不对
    @GetMapping("/deductStock")
    public String deductStock() throws InterruptedException{

        int stock = Integer.parseInt(stringRedisTemplate.opsForValue().get("stock"));
        if (stock > 0){
            try { TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e) { e.printStackTrace(); }
            int realStock = stock - 1;
            stringRedisTemplate.opsForValue().set("stock",realStock+"");
            System.out.println("扣减库存成功，剩余库存:"+realStock);
        }else{
            System.out.println("扣减库存失败，库存不足。");
        }
        return "end";
    }
    */
}
