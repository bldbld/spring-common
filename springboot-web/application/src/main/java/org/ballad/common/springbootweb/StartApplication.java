package org.ballad.common.springbootweb;

import org.ballad.common.springbootweb.aop.EnableAuditLog;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan("org.ballad.common.springbootweb.mybatisplus.mapper")
@EnableAuditLog
public class StartApplication {

	public static void main(String[] args) {SpringApplication.run(StartApplication.class, args);
	}

}
