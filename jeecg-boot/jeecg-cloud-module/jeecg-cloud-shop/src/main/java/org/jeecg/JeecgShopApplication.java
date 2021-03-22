package org.jeecg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class JeecgShopApplication {

    public static void main(String[] args) {
        SpringApplication.run(JeecgShopApplication.class, args);
        System.err.println("service start successful");
    }
}
