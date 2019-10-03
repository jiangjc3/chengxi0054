package com.chengxi.p2p;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDubbo
public class P2pDataserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(P2pDataserviceApplication.class, args);
    }

}
