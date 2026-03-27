## 预加载库

### `FFmpeg`

#### 环境

安装依赖库, `Ubuntu` 系统使用以下命令安装:

```shell
$ sudo apt update
$ sudo apt install yasm nasm
```

**备注**

* 新版本优先使用 `nasm`

#### 编译 `FFmpeg`

克隆 `FFmpeg` 源码:

```shell
$ git clone https://github.com/FFmpeg/FFmpeg.git
```

检出代码至最新发布版本:

```shell
$ git checkout <version>
```

安装编译工具:

```shell
$ sudo apt update
$ sudo apt install gcc g++ make
```

使用以下命令编译 `FFmpeg`:

```shell
$ ./sbin/build.sh <ffmpeg source direcroty>
```

将 `Windows` 下的库放在 `prebuilt/ffmpeg/libs/windows` 目录下。

#### `Android` 支持

安装 [`Android NDK`](https://developer.android.com/ndk)

使用以下命令编译 `FFmpeg`:

```shell
$ export ANDROID_NDK=<android ndk directory>
$ ./sbin/build.sh <ffmpeg source direcroty>
```

构建脚本已在 `NDK r28c` 下验证。

默认情况下, 构建脚本编译 `Android` 版本时也会同时编译 `Linux` 版本库。

#### `Windows` 支持

请前往 `FFmpeg` [官网下载](https://ffmpeg.org)即可。

#### 目录结构

`FFmpeg` 编译完成后, 请调整库目录结构如下(供依赖库链接使用):

```
prebuilt
 - ffmpeg
   - include
   - libs
     - android
       - arm64-v8a
         - libavcodec.so
         - ...
       - armeabi-v7a
         - libavcodec.so
         - ...
       - ...
     - linux
       - x64
         - libavcodec.so
         - ...
     - windows
       - x64
         - avcodec-62.dll
         - ...
   - share
```

### logger

请参考 `hibate-natives` 模块完成编译, 针对 `Linux` 和 `Windows` 环境, 编译完成后, 请调整库目录结构。
