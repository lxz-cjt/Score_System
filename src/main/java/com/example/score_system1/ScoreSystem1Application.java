package com.example.score_system1;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@MapperScan("com.example.score_system1.mapper")
public class ScoreSystem1Application {

	public static void main(String[] args) {
		SpringApplication.run(ScoreSystem1Application.class, args);
	}

}
