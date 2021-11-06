package com.lfp.service;

import cn.hutool.core.io.FileUtil;
import com.lfp.config.m3u8.M3u8FileUploadProperties;
import com.lfp.util.GenerateUtil;
import lombok.extern.slf4j.Slf4j;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import net.bramp.ffmpeg.probe.FFmpegStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @Description
 * @Author lfp
 * @Version 1.0
 */
@SuppressWarnings("ALL")
@Slf4j
public class M3u8TransferTemplate {
    private M3u8FileUploadProperties m3u8Properties;
    @Autowired
    private FFmpeg ffmpeg;
    @Autowired
    private FFprobe ffprobe;

    public M3u8TransferTemplate (M3u8FileUploadProperties m3u8Properties){
        this.m3u8Properties = m3u8Properties;
    }

    /**
    *@Description 视频文件转 m3u8
    *@Author lfp
    *@param file 视频文件
     *            支持： .mp4 | .flv | .avi | .mov | .wmv | .wav
    *@Return
    */
    public String mediaFileToM3u8(MultipartFile file) throws IOException {
        if(file.isEmpty()){
            throw new RuntimeException("文件上传失败");
        }

        //临时目录创建
        String tempFilePath = m3u8Properties.getTempPath();
        if(!FileUtil.exist(tempFilePath)){
            FileUtil.mkdir(tempFilePath);
        }

        String filePathName = tempFilePath + file.getOriginalFilename();

        File dest = new File(filePathName);
        file.transferTo(dest);
        log.info("临时文件上传成功......");


        final FFmpegProbeResult probe = ffprobe.probe(filePathName);
        final List<FFmpegStream> streams = probe.getStreams().stream().filter(fFmpegStream -> fFmpegStream.codec_type != null).collect(Collectors.toList());
        final Optional<FFmpegStream> audioStream = streams.stream().filter(fFmpegStream -> FFmpegStream.CodecType.AUDIO.equals(fFmpegStream.codec_type)).findFirst();
        final Optional<FFmpegStream> videoStream = streams.stream().filter(fFmpegStream -> FFmpegStream.CodecType.VIDEO.equals(fFmpegStream.codec_type)).findFirst();

        if (!audioStream.isPresent()) {
            //throw new RuntimeException("未发现音频流");
            log.error("未发现音频流");
        }
        if (!videoStream.isPresent()) {
            //throw new RuntimeException("未发现视频流");
            log.error("未发现视频流");
        }

        //m3u8文件 存储路径
        String filePath = GenerateUtil.generateFilePath(m3u8Properties.getBasePath());
        if(!FileUtil.exist(filePath)){
            FileUtil.mkdir(filePath);
        }
        String mainName = GenerateUtil.getFileMainName(filePathName);
        String m3u8FileName = filePath+mainName+".m3u8";


        FFmpegBuilder builder = new FFmpegBuilder()
                .setInput(filePathName)
                .overrideOutputFiles(true)
                .addOutput(m3u8FileName)//输出文件
                .setFormat(probe.getFormat().format_name) //"mp4"
                .setAudioBitRate(audioStream.isPresent() ? audioStream.get().bit_rate : 0)
                .setAudioChannels(1)
                .setAudioCodec("aac")        // using the aac codec
                .setAudioSampleRate(audioStream.get().sample_rate)
                .setAudioBitRate(audioStream.get().bit_rate)
                .setStrict(FFmpegBuilder.Strict.STRICT)
                .setFormat("hls")
                .addExtraArgs("-hls_wrap", "0", "-hls_time", "20", "-hls_list_size", "0")
                .done();

        FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);

        // Run a one-pass encode
        executor.createJob(builder).run();

        if(m3u8Properties.isDeleteTempFile()){
            if (dest.isFile() && dest.exists()) {
                System.gc();//启动jvm垃圾回收
                dest.delete();
                log.warn("临时文件{}已删除",file.getOriginalFilename());
            }
        }
        log.info("视频转换已完成 ！");

        return m3u8FileName;
    }}