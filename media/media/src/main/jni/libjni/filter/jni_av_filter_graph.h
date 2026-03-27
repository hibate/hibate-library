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
 * jni_av_filter_graph.h
 *
 * @author Hibate
 * Created on 2026/03/21.
 */

#ifndef MEDIA_JNI_AV_FILTER_GRAPH_H
#define MEDIA_JNI_AV_FILTER_GRAPH_H

#include <jni.h>

extern "C" {
#include <libavfilter/avfilter.h>
}

AVFilterGraph* avfilter_graph_getAVFilterGraph(JNIEnv* env, jobject thiz);

#endif //MEDIA_JNI_AV_FILTER_GRAPH_H