package com.kongfanyu;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Date;

/**
 * Copyright (C), 2018-2021
 * FileName: MyConfig
 * Author:   kongfanyu
 * Date:     2021/1/3 下午7:35
 */
@Configuration
public class MyConfig {
    @Bean
    public Date date(){
        return new Date();
    }
}
