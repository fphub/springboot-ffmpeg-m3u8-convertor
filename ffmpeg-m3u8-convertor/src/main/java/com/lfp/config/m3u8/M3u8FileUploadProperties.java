package com.lfp.config.m3u8;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Author lfp
 * @Version 1.0
 */
@Data
@ConfigurationProperties(prefix = "m3u8.transfer")
public class M3u8FileUploadProperties {
    /**
    * 文件上传临时路径
    */
    private String tempPath = "/tmp";

    /**
     * m3u8文件转换后，储存的根路径
     */
    private String basePath = "/data";

    /**
     * 文件转换完，删除临时文件
     */
    private boolean deleteTempFile;
}