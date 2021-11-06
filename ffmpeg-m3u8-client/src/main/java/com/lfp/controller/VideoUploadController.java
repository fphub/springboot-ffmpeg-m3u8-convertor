package com.lfp.controller;

import cn.hutool.core.io.FileUtil;
import com.lfp.entity.MediaInputInfo;
import com.lfp.service.M3u8TransferTemplate;
import com.lfp.util.GenerateUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import net.bramp.ffmpeg.probe.FFmpegStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @Description TODO
 * @Author lfp
 * @Version 1.0
 */
@SuppressWarnings("ALL")
@Api(tags = "视频转换")
@RestController
@RequestMapping("video")
@Slf4j
public class VideoUploadController {
    @Autowired
    private FFmpeg ffmpeg;
    @Autowired
    private FFprobe ffprobe;

    @Autowired
    private M3u8TransferTemplate m3u8TransferTemplate;


    /**
    *@Description 通过指定本地路径，将本地视频转为 m3u8
    *@Author lfp
    *@param mediaInfo
    *@Return
    */
    @ApiOperation(value = "本地视频 转 m3u8",notes = "这种方式适合操作本机文件")
    @PostMapping("transfer")
    public String transferToM3u8(@RequestBody MediaInputInfo mediaInfo) throws IOException {

        final FFmpegProbeResult probe = ffprobe.probe(mediaInfo.getMediaPath());
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

        //创建文件夹
        String filePath = GenerateUtil.generateFilePath(mediaInfo.getOutputPath());
        if(!FileUtil.exist(filePath)){
            FileUtil.mkdir(filePath);
        }
        String mainName = GenerateUtil.getFileMainName(mediaInfo.getMediaPath());
        String fileName = filePath+mainName+".m3u8";


        FFmpegBuilder builder = new FFmpegBuilder()
                .setInput(mediaInfo.getMediaPath())
                .overrideOutputFiles(true)
                .addOutput(fileName)//输出文件
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

        return fileName;
    }

    @ApiOperation(value = "上传视频文件转m3u8",notes = "适用于服务器上传。服务器需要安装ffmpeg")
    @PostMapping("stream/transfer")
    public String streamToM3u8(@RequestPart("file") MultipartFile file) throws IOException {
        String result = m3u8TransferTemplate.mediaFileToM3u8(file);
        return result;
    }
}