/*
 * Copyright (C) 2026 Hibate <ycaia86@126.com>
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
 * text_utils.h
 *
 * @author Hibate
 * Created on 2026/03/21.
 */

#ifndef MEDIA_LANG_TEXT_UTILS_H
#define MEDIA_LANG_TEXT_UTILS_H

#include <jni.h>

class TextUtils {
public:
    TextUtils();
    ~TextUtils();

    static int OnLoad(JNIEnv* env);

    static void OnUnload(JNIEnv* env);

    /**
     * 字符串转 UTF8 编码字符串
     * @param env 上下文
     * @param text 字符串
     * @return 字符串
     */
    static jstring ToUTF8(JNIEnv* env, const char *text);
};

#endif //MEDIA_LANG_TEXT_UTILS_H