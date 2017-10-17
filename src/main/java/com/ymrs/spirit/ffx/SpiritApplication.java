package com.ymrs.spirit.ffx;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.ymrs.spirit.ffx.prop.SpiritProperties;

@SpringBootApplication
@MapperScan("com.ymrs.spirit.ffx.mapper")
@EnableConfigurationProperties(SpiritProperties.class)
public class SpiritApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpiritApplication.class, args);
	}
}
