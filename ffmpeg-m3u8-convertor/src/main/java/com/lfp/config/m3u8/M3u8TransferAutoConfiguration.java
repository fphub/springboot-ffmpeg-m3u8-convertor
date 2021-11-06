package com.lfp.config.m3u8;

import com.lfp.service.M3u8TransferTemplate;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description TODO
 * @Author lfp
 * @Version 1.0
 */
@AllArgsConstructor
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties({ M3u8FileUploadProperties.class })
public class M3u8TransferAutoConfiguration {

    private final M3u8FileUploadProperties m3u8FileUploadProperties;

    @Bean
    @ConditionalOnMissingBean(M3u8TransferTemplate.class)
    @ConditionalOnProperty(name = "oss.enable", havingValue = "true", matchIfMissing = true)
    public M3u8TransferTemplate m3u8TransferTemplate() {
        return new M3u8TransferTemplate(m3u8FileUploadProperties);
    }
}