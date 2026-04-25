#!/bin/bash
#
# Copyright (C) 2017 Hibate <ycaia86@126.com>
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

#--------------------
# Standard options:
COMMON_OPTIONS=

# Licensing options:
COMMON_OPTIONS="$COMMON_OPTIONS --disable-gpl"
COMMON_OPTIONS="$COMMON_OPTIONS --disable-nonfree"

# Configuration options:
COMMON_OPTIONS="$COMMON_OPTIONS --enable-runtime-cpudetect"
COMMON_OPTIONS="$COMMON_OPTIONS --disable-gray"

# Program options:
COMMON_OPTIONS="$COMMON_OPTIONS --disable-programs"
COMMON_OPTIONS="$COMMON_OPTIONS --disable-ffmpeg"
COMMON_OPTIONS="$COMMON_OPTIONS --disable-ffplay"
COMMON_OPTIONS="$COMMON_OPTIONS --disable-ffprobe"

# Documentation options:
COMMON_OPTIONS="$COMMON_OPTIONS --disable-doc"
COMMON_OPTIONS="$COMMON_OPTIONS --disable-htmlpages"
COMMON_OPTIONS="$COMMON_OPTIONS --disable-manpages"
COMMON_OPTIONS="$COMMON_OPTIONS --disable-podpages"
COMMON_OPTIONS="$COMMON_OPTIONS --disable-txtpages"

# Component options:
COMMON_OPTIONS="$COMMON_OPTIONS --disable-avdevice"
COMMON_OPTIONS="$COMMON_OPTIONS --enable-avcodec"
COMMON_OPTIONS="$COMMON_OPTIONS --enable-avformat"
COMMON_OPTIONS="$COMMON_OPTIONS --enable-swresample"
COMMON_OPTIONS="$COMMON_OPTIONS --enable-swscale"
if [ ! -z "${VERSION}" ] && awk "BEGIN {exit !(${VERSION} < 8.0)}"; then
    COMMON_OPTIONS="$COMMON_OPTIONS --disable-postproc"
fi
COMMON_OPTIONS="$COMMON_OPTIONS --enable-avfilter"
COMMON_OPTIONS="$COMMON_OPTIONS --enable-network"

# Hardware accelerators:
COMMON_OPTIONS="$COMMON_OPTIONS --disable-d3d11va"
COMMON_OPTIONS="$COMMON_OPTIONS --disable-dxva2"
COMMON_OPTIONS="$COMMON_OPTIONS --disable-vaapi"
COMMON_OPTIONS="$COMMON_OPTIONS --disable-vdpau"
COMMON_OPTIONS="$COMMON_OPTIONS --disable-videotoolbox"

# Individual component options:
# ./configure --list-encoders
COMMON_OPTIONS="$COMMON_OPTIONS --enable-encoders"

# ./configure --list-decoders
COMMON_OPTIONS="$COMMON_OPTIONS --enable-decoders"

# ./configure --list-muxers
COMMON_OPTIONS="$COMMON_OPTIONS --enable-muxers"

# ./configure --list-demuxers
COMMON_OPTIONS="$COMMON_OPTIONS --enable-demuxers"

# ./configure --list-parsers
COMMON_OPTIONS="$COMMON_OPTIONS --enable-parsers"

# ./configure --list-bsf
COMMON_OPTIONS="$COMMON_OPTIONS --enable-bsfs"

# ./configure --list-protocols
COMMON_OPTIONS="$COMMON_OPTIONS --enable-protocols"
