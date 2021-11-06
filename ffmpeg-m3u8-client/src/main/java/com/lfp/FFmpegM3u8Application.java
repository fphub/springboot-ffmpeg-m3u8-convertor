package com.lfp;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

/**
 * @Author lfp
 * @Version 1.0
 */
@Slf4j
@SpringBootApplication
public class FFmpegM3u8Application {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(FFmpegM3u8Application.class, args);

        Environment environment = context.getBean(Environment.class);
        //environment.getProperty("server.servlet.context-path") 应用的上下文路径，也可以称为项目路径
        String path = environment.getProperty("server.servlet.context-path");
        if (StringUtils.isEmpty(path)) {
            path = "";
        }
        log.info("\n访问地址：http://localhost:{}{}", environment.getProperty("server.port"), path);
        log.info("\n文档地址：http://localhost:{}{}/doc.html",environment.getProperty("server.port"), path);

    }
}