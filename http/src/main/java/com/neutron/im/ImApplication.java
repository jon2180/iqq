package com.neutron.im;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@MapperScan("com.neutron.im.mapper") // 本来可以用 MapperScan 注解，运行也不报错，但是 idea 检查不到
public class ImApplication {

    public static void main(String[] args) {
        SpringApplication.run(ImApplication.class, args);
    }

}
