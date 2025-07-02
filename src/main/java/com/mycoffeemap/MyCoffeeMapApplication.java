package com.mycoffeemap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication  // 이 패키지 아래를 자동으로 @ComponentScan
public class MyCoffeeMapApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyCoffeeMapApplication.class, args);
    }
}
