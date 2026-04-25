## 预加载库

### `FFmpeg`

请使用编译脚本完成构建, 脚本兼容 `Linux`、`macOS` 操作系统, 已在以下环境完成验证:

name    | abi    | version
--------|--------|------------
Android | all    | `Android NDK r28c`
Linux   | x86_64 | `Ubuntu 24.04`
macOS   | arm64  | `macOS Sequoia 15.7.5`

编译脚本已在以下 `FFmpeg` 版本完成验证:

* `n8.0`、`n8.1`
* `n7.0`、`n7.1`
* `n6.1`

编译脚本支持自动获取源码版本号, 需满足条件:

* 安装 `git` 命令行工具
* 使用克隆方式获取源码, 且包含标签

若不满足以上条件, 在编译前可手动导出版本号:

```shell
$ export VERSION=n8.1
```

#### 环境

强烈建议在编译期开启 `ASM` 汇编优化, 构建脚本已默认支持(需安装依赖库)。

`Ubuntu` 系统环境请执行以下命令安装依赖:

```shell
$ sudo apt update
$ sudo apt install yasm nasm
```

`macOS` 系统环境请执行以下命令安装依赖:

```shell
$ brew install yasm nasm
```

**备注**

* `FFmpeg` 新版本优先使用 `nasm`

#### 源码

克隆 `FFmpeg` 源码:

```shell
$ git clone https://github.com/FFmpeg/FFmpeg.git
```

检出代码至最新发布版本:

```shell
$ git checkout <version>
```

#### `Android`

安装 [`Android NDK`](https://developer.android.com/ndk) 环境。

使用以下命令编译 `FFmpeg`:

```shell
$ export ANDROID_NDK=<android ndk directory>
$ ./sbin/build.sh <ffmpeg source direcroty>
```

可选变量如下:

name                   | default | description
-----------------------|---------|---------------------
ANDROID_ABI            | 21      | Android 版本

#### `Linux`

以下以 `Ubuntu` 系统环境编译为例, 请执行以下命令配置编译环境:

```shell
$ sudo apt update
$ sudo apt install gcc g++ make
```

使用以下命令编译 `FFmpeg`:

```shell
$ ./sbin/build.sh <ffmpeg source direcroty>
```

#### `macOS`

请安装命令行开发工具。

使用以下命令编译 `FFmpeg`:

```shell
$ ./sbin/build.sh <ffmpeg source direcroty>
```

#### `Windows`

请前往 `FFmpeg` [官网下载](https://ffmpeg.org), 解压缩预编译库并将其拷贝至对应目录下(请参考目录结构)。

#### 目录结构

编译完成后, 请调整库目录结构如下(供依赖库链接使用):

```
prebuilt
  - ffmpeg
    - android
      - arm64-v8a
        - include
        - lib
          - libavcodec.so
          - ...
      - armeabi-v7a
        - include
        - lib
          - libavcodec.so
          - ...
      - ...
    - linux
      - arm64
        - include
        - lib
          - libavcodec.so
          - ...
      - x86_64
        - include
        - lib
          - libavcodec.so
          - ...
      - ...
    - macos
      - arm64
        - include
        - lib
          - libavcodec.dylib
          - ...
      - x86_64
        - include
        - lib
          - libavcodec.dylib
          - ...
    - windows
      - x64
        - include
        - lib
          - avcodec.lib
          - avcodec-62.dll
          - ...
      - ...
```

各系统环境架构如下:

name    | abi
--------|--------------
Android | arm64-v8a、armeabi-v7a、riscv64、x86、x86_64
Linux   | arm64、x86_64、x86
macOS   | arm64、x86_64
Windows | arm64、x64、x86

### logger

请参考 `hibate-natives` 模块完成编译, 针对 `Linux`、`macOS`、`Windows` 环境, 编译完成后, 请调整库目录结构。
