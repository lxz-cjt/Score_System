package io.github.lxzcjt.scoresystem.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI / Swagger 文档配置
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI scoreSystemOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("高校成绩管理系统 API")
                .version("1.0.0")
                .description("""
                        面向教务人员、教师、学生三类角色的成绩管理与查询平台。

                        - 教务人员：用户管理、课程与排课管理、申诉审核、统计分析
                        - 教师：成绩录入与修改（自动计算总评）、申诉处理、成绩日志
                        - 学生：成绩与 GPA 查询、课程查询、发起/取消成绩申诉
                        """)
                .contact(new Contact()
                        .name("lxz-cjt")
                        .url("https://github.com/lxz-cjt/Score_System"))
                .license(new License()
                        .name("Apache 2.0")
                        .url("https://www.apache.org/licenses/LICENSE-2.0")));
    }
}
