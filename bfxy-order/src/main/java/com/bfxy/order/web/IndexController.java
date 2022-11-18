package com.bfxy.order.web;

import com.alibaba.dubbo.config.annotation.Reference;
import com.bfxy.store.service.api.HelloServiceApi;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {

    @Reference(version = "1.0.0",
            application = "${dubbo.application.id}",
            interfaceName = "com.bfxy.store.service.HelloServiceApi",
            check = false,
            timeout = 1000,
            retries = 0
    )
    private HelloServiceApi helloService;

    @RequestMapping("/index")
    public String index(@RequestParam String name){
        System.out.println("------------");
        return helloService.sayHello(name);
    }
}
