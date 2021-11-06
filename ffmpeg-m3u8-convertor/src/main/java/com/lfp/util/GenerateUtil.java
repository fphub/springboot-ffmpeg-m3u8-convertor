package com.lfp.util;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;

import java.time.LocalDateTime;

/**
 * @Description TODO
 * @Author lfp
 * @Date 2021/11/6 16:52
 * @Version 1.0
 */
public class GenerateUtil {

    public static String generateFilePath(String basePath){
        String temp = basePath;
        if(StrUtil.isNotBlank(basePath)){
            if(basePath.endsWith("/")){
                temp = basePath.substring(0,basePath.lastIndexOf("/"));
            }
        }
        return temp+"/"+generateDatePath()+"/";
    }

    /**
    * 根据当前时间，生成目录
    */
    public static String generateDatePath(){
        LocalDateTime now = LocalDateTime.now();
        return DateUtil.format(now, "yyyyMMdd/HH/mm");
    }

    /**
     * 根据文件路径，获取文件主名称
     */
    public static String getFileMainName(String path){
        String fileName = FileUtil.getName(path);
        return fileName.substring(0,fileName.lastIndexOf("."));
    }
}