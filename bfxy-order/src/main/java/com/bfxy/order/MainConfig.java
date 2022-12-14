package com.bfxy.order;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = { "com.bfxy.order.*" , "com.bfxy.order.config.*"})
@MapperScan(basePackages = "com.bfxy.order.mapper")
public class MainConfig {

}
