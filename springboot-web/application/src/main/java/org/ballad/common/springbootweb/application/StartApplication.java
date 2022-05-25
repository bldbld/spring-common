package org.ballad.common.springbootweb.application;

import org.ballad.common.springbootweb.sample.mybatisplus.bean.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan("org.ballad.common.springbootweb.sample.mybatisplus.mapper")
public class StartApplication {

	public static void main(String[] args) {
		SpringApplication.run(StartApplication.class, args);
	}

}
