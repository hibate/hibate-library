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
 * onLoad.cpp
 *
 * @author Hibate
 * Created on 2017/10/31.
 */

#include "onload.h"

extern MethodLoader codec_capabilities_loader;

static MethodLoader s_loaders[] = {
        codec_capabilities_loader,
};

int getMethodLoaders(MethodLoader** loaders, int* size) {
    *loaders = s_loaders;
    *size = NELEM(s_loaders);
    return 0;
}
