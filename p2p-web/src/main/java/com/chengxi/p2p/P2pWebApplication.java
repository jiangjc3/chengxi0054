package com.chengxi.p2p;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class P2pWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(P2pWebApplication.class, args);
    }

}
