## 说明

Android Logger

## Android logger with logback

### 依赖库

* slf4j-api-xxx.jar
* logback-classic-xxx.jar
* logback-core-xxx.jar

### 使用

在 `assets` 目录下创建 `logback.xml` 文件, 并使用 `org.hibate.logger.logback.Configurator` 进行初始化和去初始化。

应用程序需配置以下权限:

```xml
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
```

#### 控制台输出

```xml
<configuration scan="true" scanPeriod="60 seconds" debug="false">
    <appender name="STDOUT" class="org.hibate.logger.logback.core.ConsoleAppender">
        <debuggable>true</debuggable>
    </appender>
</configuration>
```

#### 配置日志路径

```xml
<configuration scan="true" scanPeriod="60 seconds" debug="false">
    <!-- 日志目录 -->
    <property name="LOG_HOME" value="${context.logs.home}" />
    <appender name="FILE_INFO" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <fileNamePattern>${LOG_HOME}/log_%d{yyyy-MM-dd}_%i.txt</fileNamePattern>
        </rollingPolicy>
    </appender>
</configuration>
```

其中默认已注册两个属性变量:

属性名                      | 说明
---------------------------|---------------------------
`context.files.home`       | 应用程序目录
`context.logs.home`        | 即 `${context.files.home}/logs` 目录

备注:

* 若 `SD` 卡存在, 应用程序目录为 `/sdcard/Android/data/package-name/files`, 否则为 `/data/data/package-name/files`
* `package-name` 为应用程序包名

#### 自定义属性

可通过自定义属性实现 `context.files.home` 等环境变量的效果。

* 使用 `org.hibate.logger.logback.Configurator#getRegistry()` 获取 `org.hibate.logger.logback.Registry` 对象;
* 使用 `org.hibate.logger.logback.Registry#putProperty(String name, String value)` 方法添加自定义属性。

备注: 自定义属性必须在初始化之前配置方可生效
