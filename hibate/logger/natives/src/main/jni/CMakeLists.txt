cmake_minimum_required(VERSION 3.25.0)
project(logger)

set(CMAKE_CXX_STANDARD 17)

# 工程目录
set(PROJECT_HOME ${PROJECT_SOURCE_DIR})

# 加载环境
include(CMakeLists.cmake)
# -fPIC
set(CMAKE_POSITION_INDEPENDENT_CODE ON)

add_subdirectory(utils)
add_subdirectory(logger)
add_subdirectory(libjni)
