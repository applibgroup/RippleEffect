/*
 * Copyright (C) 2020-21 Application Library Engineering Group
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

package com.andexert.rippleeffect;

import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

/**
 * Log Utils to display HiLog.
 */
public class LogUtil {
    /**
     * LOG_TAG variable to use in HiLog.
     */
    public static final String LOG_TAG = "RippleEffect";

    /**
     * LOG_LABEL variable to use in HiLog.
     */
    public static final HiLogLabel LOG_LABEL = new HiLogLabel(HiLog.DEBUG, 0x0, LOG_TAG);

    /**
     * LOG_FORMAT variable to use the specified string format in HiLog.
     */
    public static final String LOG_FORMAT = "%{public}s: %{public}s";

    private LogUtil() {
    }

    /**
     * debug method for HiLog.
     *
     * @param tag log tag
     * @param message debug log message
     */
    public static void debug(String tag, String message) {

        HiLog.debug(LOG_LABEL, LOG_FORMAT, tag, message);
    }

    /**
     * info method for HiLog.
     *
     * @param tag log tag
     * @param message info log message
     */
    public static void info(String tag, String message) {
        HiLog.info(LOG_LABEL, LOG_FORMAT, tag, message);
    }

    /**
     * warn method for HiLog.
     *
     * @param tag log tag
     * @param message warning log message
     */
    public static void warn(String tag, String message) {

        HiLog.warn(LOG_LABEL, LOG_FORMAT, tag, message);
    }

    /**
     * error method for HiLog.
     *
     * @param tag log tag
     * @param message error log message
     */
    public static void error(String tag, String message) {

        HiLog.error(LOG_LABEL, LOG_FORMAT, tag, message);
    }
}
