################################################################################

# 根目录
get_filename_component(ROOT_PROJECT_HOME "${PROJECT_HOME}/../../../../../" REALPATH)

# 依赖库
get_filename_component(LOGGER_HOME "${ROOT_PROJECT_HOME}/hibate/logger/natives/src/main" REALPATH)
get_filename_component(FFMPEG_HOME "${PROJECT_HOME}/prebuilt/ffmpeg" REALPATH)
