## 预加载库

### libyuv

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
