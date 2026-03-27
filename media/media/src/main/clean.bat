@echo off

@echo =============================================================
@echo Author: Hibate
@echo Description: Jni project clean script
@echo Current Date: %date% %time:~0,8%
@echo =============================================================

setlocal

rem ##############################################################
set "current_dir=%cd%"
set "libs_dir=%current_dir%\libs"
set "objs_dir=%current_dir%\obj"
rem ##############################################################

:mainEntry
rem 检查是否是 jni 编译目录: 当前目录下必须存在且名字为 jni 的文件夹
set "jni_dir=%current_dir%\jni"
if exist "%jni_dir%" goto okJNI
echo Is this jni project folder?
goto end

:okJNI
rem 检查 ndk 环境，确保已添加至系统环境变量 PATH 中
set "_RUN_EXECUTOR=ndk-build"
where %_RUN_EXECUTOR% > nul 2>&1
if "%errorlevel%" == "0" goto okNdk
echo The NDK environment is not defined correctly.
echo This environment is needed to run this program.
echo Please add the android ndk home to PATH environment variable.
goto end

:okNdk
set "_EXECNDK=%_RUN_EXECUTOR%"
goto jniClean

:jniClean
rem 执行清理
call %_EXECNDK% clean
goto deleteFiles

:deleteFiles
if exist "%libs_dir%" (
    rd /s /q %libs_dir%
)
if exist "%objs_dir%" (
    rd /s /q %objs_dir%
)

goto exit

:end
pause

:exit