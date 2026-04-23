## 说明

适用于 `Java` 和 `Android` 的底层日志库, 通过宏定义将底层日志导出到 `Java` 并接入 `SLF4J`。

## 构建

### 依赖库

* slf4j-api-xxx.jar

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
$ sudo apt install gcc g++ make cmake
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

#### `Windows`

请安装 `Visual Studio` 开发工具、`CMake` 工具包软件。

使用 `CMake` 生成 `Visual Studio` 工程, 使用 `Visual Studio` 打开工程编译即可。

### 目录结构

针对 `Linux`、`macOS`、`Windows` 环境, 编译完成后, 请调整库目录结构如下(供依赖库链接使用):

```
main
 - libs
   - linux
     - x86_64
       - liblogger.so
   - macos
     - arm64
       - liblogger.dylib
     - x86_64
       - liblogger.dylib
   - windows
     - x64
       - logger.dll
       - logger.lib
     - x86
       - logger.dll
       - logger.lib
```

各系统环境架构如下:

name    | abi
--------|--------------
Linux   | arm64、x86_64、x86
macOS   | arm64、x86_64
Windows | arm64、x64、x86

### 编译优化

在链接静态库时, 编译器为减小体积, 可能丢弃未被内部引用的符号, 导致相关函数无法正确导出。

不同系统下编译器的优化策略如下:

name    | strategy
--------|----------------
Android | 丢弃
Linux   | 全部导出
macOS   | 丢弃
Windows | 按需导出

如需保留符号, 相关配置如下:

`Android`

* `Android.mk` 文件添加 `LOCAL_WHOLE_STATIC_LIBRARIES := $(STATIC_LIBRARIES)` 声明
* `CMakeLists.txt` 文件添加如下配置:

```
if (APP_SYSTEM STREQUAL "Android")
    list(APPEND STATIC_LIBRARIES "-Wl,--whole-archive" slogger "-Wl,--no-whole-archive")
endif ()
```

`macOS`

* `CMakeLists.txt` 文件添加如下配置:

```
if (APP_SYSTEM STREQUAL "macOS")
    list(APPEND STATIC_LIBRARIES "-Wl,-force_load,$<TARGET_FILE:slogger>")
endif ()
```

`Windows`

使用 `__declspec(dllexport)` 显式声明。

**备注**

* `slogger` 为需要保留符号的静态库名称

### 使用

```java
import org.hibate.logger.natives.Logger;
import org.hibate.logger.natives.LoggerFactory;
import org.hibate.logger.natives.LoggerOverSlf4j;
import org.hibate.logger.natives.Priority;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Logger logger = LoggerFactory.getLogger();
        logger.setPriority(Priority.DEBUG);  // 设置日志输出级别
        logger.setLineEnabled(true);         // 是否输出文件/行号信息
        logger.addLoggerListener(new LoggerOverSlf4j()); // 将底层日志链接到 Slf4j 中输出
    }
}
```

**备注**

* `Windows` 系统环境下使用, 请将依赖库拷贝至系统环境变量 `PATH` 下
* `Linux` 系统环境下使用, 请将依赖库拷贝至系统库搜索路径下, 如: `/usr/local/lib`
* `macOS` 系统环境下使用, 请将依赖库拷贝至 `~/Library/Java/Extensions` 目录下
* 非开发环境下使用, 请导出依赖库路径至系统库搜索路径下即可
