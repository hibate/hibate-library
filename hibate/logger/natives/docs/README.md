## 说明

适用于 `Java` 和 `Android` 的底层日志库, 通过宏定义将底层日志导出到 `Java` 并接入 `SLF4J`。

## 构建

### 依赖库

* slf4j-api-xxx.jar

### 编译

以下以 `Ubuntu` 系统环境编译为例。可执行源码路径下的 `build.sh` 脚本一键编译 `Linux` 和 `Windows` 版本。

#### `Android`

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

* 在 `Windows` 中使用时需加载 `mingw` 对应的动态库。

### 目录结构

针对 `Linux` 和 `Windows` 环境, 编译完成后, 请调整库目录结构如下(供依赖库链接使用):

```
main
 - libs
   - linux
     - x64
       - liblogger.so
   - windows
     - x64
       - liblogger.dll
     - x86
       - liblogger.dll
```

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

* `Linux` 和 `Windows` 系统环境下使用, 请将依赖库拷贝至系统环境变量 `PATH` 下。
