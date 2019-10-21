package com.chengxi.p2p;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class P2pPayApplication {

    public static void main(String[] args) {
        SpringApplication.run(P2pPayApplication.class, args);
    }

}
