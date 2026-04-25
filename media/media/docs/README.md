## 说明

适用于 `Java` 和 `Android` 的音视频库。

## 示例

示例              | 描述
-----------------|-------------------
`ReMuxTest`      | 转封装示例
`ThumbnailTest`  | 截图示例
`TranscodeTest`  | 转码示例

## Media library

已在以下 `FFmpeg` 版本完成验证:

版本     | 结果
--------|-------
`n8.1`  | ✅️
`n8.0`  | ✅️
`n7.1`  | ✅️
`n7.0`  | ✅️
`n6.1`  | ✅️

## 构建

### 预加载库

参考源码路径下 `prebuilt/README.md` 文件, 编译完成 `FFMPEG` 和 `logger` 预加载库。

### 编译

请配置 `Java` 环境, 并导出 `JAVA_HOME` 环境变量。

#### `Android`

配置 [`Android NDK`](https://developer.android.com/ndk) 环境, 将其路径添加到系统环境变量 `PATH` 中。

`Windows` 系统下双击源码路径下 `build.bat` 脚本执行编译，双击 `clean.bat` 脚本执行清理。

其他系统下进入源码路径下执行 `ndk-build` 命令编译。

或编辑 `gradle.properties` 文件, 修改变量 `environment.native` 值为 `dev`, 使用 `Android Studio` 自动编译均可。

#### `Linux`

以下以 `Ubuntu` 系统环境编译为例, 请执行以下命令配置编译环境:

```shell
$ sudo apt update
$ sudo apt install gcc g++ make
```

执行以下命令编译, 默认编译 `Release` 版本:

```shell
$ ./build.sh
```

编译 `Debug` 版本:

```shell
$ export APP_OPTIM=Debug
$ ./build.sh
```

#### `macOS`

请安装命令行开发工具以及 `CMake` 环境。

执行以下命令编译, 默认编译 `Release` 版本:

```shell
$ ./build.sh
```

编译 `Debug` 版本:

```shell
$ export APP_OPTIM=Debug
$ ./build.sh
```

#### Windows

请安装 `Visual Studio` 开发工具、`CMake` 工具包软件。

使用 `CMake` 生成 `Visual Studio` 工程, 使用 `Visual Studio` 打开工程编译即可。

### 使用

* `Windows` 系统环境下使用, 请将依赖库拷贝至系统环境变量 `PATH` 下
* `Linux` 系统环境下使用, 请将依赖库拷贝至系统库搜索路径下, 如: `/usr/local/lib`
* `macOS` 系统环境下使用, 请将依赖库拷贝至 `~/Library/Java/Extensions` 目录下
* 非开发环境下使用, 请导出依赖库路径至系统库搜索路径下即可
