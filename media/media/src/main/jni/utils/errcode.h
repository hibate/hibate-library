/*
 * Copyright (C) 2018 Hibate <ycaia86@126.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * errcode.h
 *
 * @author Hibate
 * Created on 2018/03/28.
 */

#ifndef JNI_UTILS_ERRCODE_H_
#define JNI_UTILS_ERRCODE_H_

enum StatusCode {
    RET_SUCCESS               = 0,   // 成功
    RET_NO_ERROR              = 0,   // 成功
    RET_ERROR                 = -1,  // 未知错误
    // 系统
    RET_INACTIVE              = -10, // 未激活
    RET_NOT_IMPLEMENTED       = -11, // 功能未实现
    // 资源
    RET_RES_NOT_FOUND         = -20, // 资源不存在
    RET_RES_EXISTS            = -21, // 资源已存在
    RET_OUT_OF_MEMORY         = -22, // 内存溢出
    RET_BUFFER_OVERFLOW       = -23, // 缓冲区溢出
    // 权限
    RET_FORBIDDEN             = -40, // 禁止访问
    RET_PERMISSION_DENIED     = -41, // 权限不足
    // 参数/逻辑
    RET_ILLEGAL_ARGUMENT      = -50, // 非法参数
    RET_ILLEGAL_STATE         = -51, // 非法状态
    RET_NULL_POINTER          = -52, // 空指针
    RET_TYPE_MISMATCH         = -54, // 类型不匹配
    RET_INDEX_OUT_OF_BOUNDS   = -55, // 下标越界
    RET_UNSUPPORTED_OPERATION = -56, // 操作不支持
    // 读写/IO
    RET_IO_ERROR              = -70, // IO 异常
    RET_TIMEOUT               = -71, // 访问超时
    RET_INTERRUPTED           = -72, // 操作中断
    RET_BUSY                  = -73, // 系统繁忙
    // 登录/身份验证
    RET_CREDENTIALS_BAD       = -106, // 凭据错误
    RET_CREDENTIALS_EXPIRED   = -107, // 凭据过期
};

#endif //JNI_UTILS_ERRCODE_H_
