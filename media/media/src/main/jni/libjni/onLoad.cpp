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
 *
 *
 * @author Hibate
 * Created on 2017/06/19.
 */

#include "onload.h"

extern MethodLoader avCodecLoader;
extern MethodLoader avCodecFactoryLoader;
extern MethodLoader avCodecContextLoader;
extern MethodLoader avCodecParametersLoader;
extern MethodLoader avCodecDecoderLoader;
extern MethodLoader avCodecEncoderLoader;
extern MethodLoader avPacketLoader;
extern MethodLoader avFilterLoader;
extern MethodLoader avFilterContextLoader;
extern MethodLoader avFilterFactoryLoader;
extern MethodLoader avFilterGraphLoader;
extern MethodLoader avFilterInOutLoader;
extern MethodLoader avFormatLoader;
extern MethodLoader avFormatContextLoader;
extern MethodLoader avFormatInputFormatLoader;
extern MethodLoader avFormatInputStreamLoader;
extern MethodLoader avFormatOutputFormatLoader;
extern MethodLoader avFormatOutputStreamLoader;
extern MethodLoader avStreamLoader;
extern MethodLoader avUtilChannelLayoutLoader;
extern MethodLoader avUtilDictionaryLoader;
extern MethodLoader avUtilFrameLoader;
extern MethodLoader avUtilLogLoader;
extern MethodLoader avUtilMathLoader;
extern MethodLoader avUtilOptionsLoader;
extern MethodLoader avUtilRationalLoader;
extern MethodLoader avUtilLoader;

static MethodLoader s_loaders[] = {
    avCodecLoader,
    avCodecFactoryLoader,
    avCodecContextLoader,
    avCodecParametersLoader,
    avCodecDecoderLoader,
    avCodecEncoderLoader,
    avPacketLoader,
    avFilterLoader,
    avFilterContextLoader,
    avFilterFactoryLoader,
    avFilterGraphLoader,
    avFilterInOutLoader,
    avFormatLoader,
    avFormatContextLoader,
    avFormatInputFormatLoader,
    avFormatInputStreamLoader,
    avFormatOutputFormatLoader,
    avFormatOutputStreamLoader,
    avStreamLoader,
    avUtilChannelLayoutLoader,
    avUtilDictionaryLoader,
    avUtilFrameLoader,
    avUtilLogLoader,
    avUtilMathLoader,
    avUtilOptionsLoader,
    avUtilRationalLoader,
    avUtilLoader,
};

int getMethodLoaders(MethodLoader** loaders, int* size) {
    *loaders = s_loaders;
    *size = NELEM(s_loaders);
    return 0;
}
