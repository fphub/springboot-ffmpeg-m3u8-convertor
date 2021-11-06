# springboot-ffmpeg-m3u8-convertor

#### 介绍
springboot+ffmpeg，将视频转换为 m3u8 格式。支持 `.mp4` | `.flv` | `.avi` | `.mov` | `.wmv` | `.wav` 格式视频转换。指定文件路径 + 文件上传转换两种转换方式。

已集成knife4j，可直接使用文档测试功能测试（一定要安装FFmpeg）。

#### 软件架构
> SpringBoot + FFmpeg + hutool + knife4j

#### 使用说明

1. git clone 代码

2. 修改 ffmpeg-m3u8-client 模块配置文件 application.yml

   ```yml
   #文件上传方式转换配置
   m3u8:
     transfer:
       base-path: D:/temp/video/base/  # 上传基础目录。（可以配合nginx，做视频服务器）
       temp-path: D:/temp/video/tmp/   # 临时文件目录
       delete-temp-file: true          #转换完成后，是否删除临时文件。默认false
   ```

3. **安装 FFmpeg**

4. 启动、测试即可。

#### 参与贡献

1.  Fork 本仓库
2.  新建 Feat_xxx 分支
3.  提交代码
4.  新建 Pull Request


#### 特技

1.  使用 Readme\_XXX.md 来支持不同的语言，例如 Readme\_en.md, Readme\_zh.md
2.  Gitee 官方博客 [blog.gitee.com](https://blog.gitee.com)
3.  你可以 [https://gitee.com/explore](https://gitee.com/explore) 这个地址来了解 Gitee 上的优秀开源项目
4.  [GVP](https://gitee.com/gvp) 全称是 Gitee 最有价值开源项目，是综合评定出的优秀开源项目
5.  Gitee 官方提供的使用手册 [https://gitee.com/help](https://gitee.com/help)
6.  Gitee 封面人物是一档用来展示 Gitee 会员风采的栏目 [https://gitee.com/gitee-stars/](https://gitee.com/gitee-stars/)
