## 说明

适用于 `Java` 和 `Android` 的音视频库。

## 示例

示例              | 描述
-----------------|-------------------
`ReMuxTest`      | 转封装示例
`ThumbnailTest`  | 截图示例
`TranscodeTest`  | 转码示例

## Media library

## 构建

### 预加载库

参考源码路径下 `prebuilt/README.md` 文件, 编译完成 `FFMPEG` 和 `logger` 预加载库。

### 编译

以下以 `Ubuntu` 系统环境编译为例。可执行源码路径下的 `build.sh` 脚本一键编译 `Linux` 和 `Windows` 版本。

#### Android

配置 `Android NDK` 环境, 将其路径添加到系统环境变量 `PATH` 中。

配置 `Android NDK` 环境, 将其路径添加到系统环境变量 `PATH` 中。

`Windows` 系统下双击源码路径下 `build.bat` 脚本执行编译，双击 `clean.bat` 脚本执行清理。

其他系统下进入源码路径下执行 `ndk-build` 命令编译。

或编辑 `gradle.properties` 文件, 修改变量 `environment.native` 值为 `dev`, 使用 `Android Studio` 自动编译均可。

#### Linux

* 编译环境

```shell
$ sudo apt update
$ sudo apt install gcc g++ make
```

* 编译

```shell
$ cd auto
$ make
```

编译 `debug`

```shell
$ cd auto
$ make debug=1
```

#### Windows

* 编译环境

```shell
$ sudo apt update
$ sudo apt install gcc g++ make mingw-w64
```

* For 64 Bit

```shell
$ cd auto
$ export CROSS_TOOLCHAINS=x86_64-w64-mingw32-
$ make
```

* For 32 Bit

```shell
$ cd auto
$ export CROSS_TOOLCHAINS=i686-w64-mingw32-
$ make
```

**备注**

在 `Windows` 中使用时需加载 `mingw` 对应的动态库。
