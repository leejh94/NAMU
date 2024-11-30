package com.namu;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableEncryptableProperties
@MapperScan(basePackages = {
		"com.namu.search.mapper",
		"com.namu.stats.mapper",
		"com.namu.helpInfo.mapper",
		"com.namu.auth.mapper",
		"com.namu.board.mapper"})
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

}
