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

package org.hibate.media.util;

/**
 * @author Hibate
 * Created on 2026/03/22.
 */
public enum Error {

    EPERM(1),
    ENOENT(2),
    ENOFILE(2),
    ESRCH(3),
    EINTR(4),
    EIO(5),
    ENXIO(6),
    E2BIG(7),
    ENOEXEC(8),
    EBADF(9),
    ECHILD(10),
    EAGAIN(11),
    ENOMEM(12),
    EACCES(13),
    EFAULT(14),
    EBUSY(16),
    EEXIST(17),
    EXDEV(18),
    ENODEV(19),
    ENOTDIR(20),
    EISDIR(21),
    EINVAL(22),
    ENFILE(23),
    EMFILE(24),
    ENOTTY(25),
    EFBIG(27),
    ENOSPC(28),
    ESPIPE(29),
    EROFS(30),
    EMLINK(31),
    EPIPE(32),
    EDOM(33),
    ERANGE(34),
    EDEADLK(36),
    ENAMETOOLONG(38),
    ENOLCK(39),
    ENOSYS(40),
    ENOTEMPTY(41),
    EILSEQ(42),
    ;

    private final int statusCode;

    Error(int statusCode) {
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
