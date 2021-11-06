package com.lfp.config;

import lombok.SneakyThrows;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFprobe;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description TODO
 * @Author lfp
 * @Version 1.0
 */
@Configuration
public class FFmpegConfig {

    @SneakyThrows
    @Bean
    public FFmpeg fFmpeg(){
        return new FFmpeg();
    }

    @SneakyThrows
    @Bean
    public FFprobe fFprobe(){
        return new FFprobe();
    }
}