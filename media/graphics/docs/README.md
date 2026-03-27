## 说明

适用于 `Android` 的图像预处理利器, 支持 `yuv` 数据格式转换、镜像、旋转处理。

## 构建

配置 `Android NDK` 环境, 将其路径添加到系统环境变量 `PATH` 中。

#### `libyuv`

克隆 `libyuv` 源码:

```shell
$ git clone https://chromium.googlesource.com/libyuv/libyuv
```

编译 `libyuv` 为静态链接库, 编译完成后, 请调整库目录结构如下:

```
- prebuilt
  - libyuv
    - include
    - libs
      - android
        - arm64-v8a
          - libyuv.a
        - armeabi-v7a
          - libyuv.a
        - ...
```

#### `Android`

`Windows` 系统下双击源码路径下 `build.bat` 脚本执行编译，双击 `clean.bat` 脚本执行清理。

其他系统下进入源码路径下执行 `ndk-build` 命令编译。

或编辑 `gradle.properties` 文件, 修改变量 `environment.native` 值为 `dev`, 使用 `Android Studio` 自动编译均可。
