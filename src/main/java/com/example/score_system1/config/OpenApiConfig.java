package com.example.score_system1.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration

public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("高校本科生成绩管理系统api文档")
                .version("1.0")
                .description("高校本科生成绩管理系统旨在为学校教务管理部门、教师以及学生提供一个高效、便捷的成绩管理与查询平台。通过本系统，教务管理人员可以轻松完成成绩的录入、审核与发布；教师能够便捷地提交学生成绩、查看成绩统计分析；学生则可以及时查询自己的成绩情况，了解学习状况。本 API 文档旨在为开发者提供详细的接口信息，以便进行系统集成、移动应用开发或其他相关应用的扩展。")
                //服务条款的url
                .termsOfService("http://example.com/terms")
                //联系人信息
                .contact(new Contact()
                        .name("吴明志")
                        .url("http://example.com/contact")
                        .email("123@example.com"))
                //许可证信息
                .license(new License()
                        .name("Apache 2.0")
                        .url("https://www.apache.org/licenses/LICENSE-2.0")));
    }
}
