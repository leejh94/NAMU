package com.namu;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableEncryptableProperties
@MapperScan(basePackages = {
		"com.namu.common.mapper",
		"com.namu.auth.mapper"})
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

}
