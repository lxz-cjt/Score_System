package com.example.score_system1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Score System应用程序主类
 * 基于Spring Boot和JPA的成绩管理系统
 */
@SpringBootApplication
@EnableJpaRepositories("com.example.score_system1.repository")
public class ScoreSystem1Application {

	public static void main(String[] args) {
		SpringApplication.run(ScoreSystem1Application.class, args);
	}

}
