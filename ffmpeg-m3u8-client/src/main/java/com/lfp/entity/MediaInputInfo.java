package com.lfp.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description TODO
 * @Author lfp
 * @Date 2021/11/6 15:42
 * @Version 1.0
 */
@Data
@ApiModel(description = "媒体信息")
public class MediaInputInfo {
    @ApiModelProperty(value = "资源路径")
    private String mediaPath;

    @ApiModelProperty(value = "输出路径")
    private String outputPath;
}