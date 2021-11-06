package com.lfp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

import java.util.ArrayList;


@Configuration
@EnableSwagger2WebMvc
public class SwaggerConfig {
    //配置swagger的Docket的bean实例
    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                //注意basePackage改成自己每个项目的路径
                .apis(RequestHandlerSelectors.basePackage("com.lfp.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    //配置swagger信息
    private ApiInfo apiInfo() {
        Contact contact = new Contact("lfp", "", "");
        return new ApiInfo("ffmpeg-converter 接口文档",
                "Api Documentation",
                "1.0",
                "urn:tos",
                contact,
                "Apache 2.0",
                "http://www.apache.org/licenses/LICENSE-2.0",
                new ArrayList<VendorExtension>()
        );
    }
}