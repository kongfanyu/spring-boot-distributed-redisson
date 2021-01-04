package com.kongfanyu;

import org.redisson.Redisson;
import org.redisson.config.Config;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * Copyright (C), 2018-2021
 * FileName: MAIN
 * Author:   kongfanyu
 * Date:     2021/1/3 下午5:09
 */
@SpringBootApplication
public class MAIN {
    public static void main(String[] args) {
        SpringApplication.run(MAIN.class,args);

        //ApplicationContext ctx =  new AnnotationConfigApplicationContext(MyConfig.class);
        //Object date = ctx.getBean("date");
        //System.out.println(date);
    }
    @Bean
    public Redisson redisson(){
        //单机模式
        Config config = new Config();
        config.useSingleServer().setAddress("redis://127.0.0.1:6379").setDatabase(0);
        return (Redisson) Redisson.create(config);
    }
}
