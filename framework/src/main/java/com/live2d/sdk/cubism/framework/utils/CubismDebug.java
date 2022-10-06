/*
 * Copyright(c) Live2D Inc. All rights reserved.
 *
 * Use of this source code is governed by the Live2D Open Software license
 * that can be found at http://live2d.com/eula/live2d-open-software-license-agreement_en.html.
 */

package com.live2d.sdk.cubism.framework.utils;

import com.live2d.sdk.cubism.framework.CubismFramework;

import static com.live2d.sdk.cubism.framework.CubismFramework.CSM_LOG_LEVEL;

/**
 * A utility class for debugging.
 * <p>
 * Log output, dump byte, and so on.
 */
public class CubismDebug {
    /**
     * Output log. Set log level to 1st argument.
     * At using {@link CubismFramework#initialize()} function, if the log level is lower than set log output level, log output is not executed.
     *
     * @param logLevel log level setting
     * @param message log string
     */
    public static void print(final CubismFramework.Option.LogLevel logLevel, final String message) {
        // If the log level is lower than set log output level in Option class, log outputting is not executed.
        if (logLevel.getId() < CubismFramework.getLoggingLevel().getId()) {
            return;
        }
        CubismFramework.coreLogFunction(message);
    }

    /**
     * Dump out a specified length of data.
     * <p>
     * If the log output level is below the level set in the option at {@link CubismFramework#initialize()}, it will not be logged.
     *
     * @param logLevel setting of log level
     * @param data data to dump
     * @param length length of dumping
     */
    public static void dumpBytes(final CubismFramework.Option.LogLevel logLevel, final byte[] data, int length) {
        for (int i = 0; i < length; i++) {
            byte b = data[i];
            String hex = Integer.toHexString(b < 0 ? 256 + b : b);

            print(logLevel, hex);
        }
    }

    /**
     * Display the normal message.
     *
     * @param message message
     */
    public static void cubismLogPrint(CubismFramework.Option.LogLevel logLevel, String message) {
        print(logLevel, "[CSM]" + message);
    }

    /**
     * Display a newline message.
     *
     * @param message message
     */
    public static void cubismLogPrintln(CubismFramework.Option.LogLevel logLevel, String message) {
        print(logLevel, "[CSM]" + message + "\n");
    }

    /**
     * Show detailed message.
     *
     * @param message message
     */
    public static void cubismLogVerbose(String message) {
        if (CSM_LOG_LEVEL.getId() <= CubismFramework.Option.LogLevel.VERBOSE.getId()) {
            cubismLogPrintln(CubismFramework.Option.LogLevel.VERBOSE, "[V]" + message);
        }
    }

    /**
     * Display the debug message.
     *
     * @param message message
     */
    public static void cubismLogDebug(String message) {
        if (CSM_LOG_LEVEL.getId() <= CubismFramework.Option.LogLevel.DEBUG.getId()) {
            cubismLogPrintln(CubismFramework.Option.LogLevel.DEBUG, "[D]" + message);
        }
    }

    /**
     * Display informational messages.
     *
     * @param message message
     */
    public static void cubismLogInfo(String message) {
        if (CSM_LOG_LEVEL.getId() <= CubismFramework.Option.LogLevel.INFO.getId()) {
            cubismLogPrintln(CubismFramework.Option.LogLevel.INFO, "[I]" + message);
        }
    }

    /**
     * Display a warning message.
     *
     * @param message message
     */
    public static void cubismLogWarning(String message) {
        if (CSM_LOG_LEVEL.getId() <= CubismFramework.Option.LogLevel.WARNING.getId()) {
            cubismLogPrintln(CubismFramework.Option.LogLevel.WARNING, "[W]" + message);
        }
    }

    /**
     * Display a error message.
     *
     * @param message message.
     */
    public static void cubismLogError(String message) {
        if (CSM_LOG_LEVEL.getId() <= CubismFramework.Option.LogLevel.ERROR.getId()) {
            cubismLogPrintln(CubismFramework.Option.LogLevel.ERROR, "[E]" + message);
        }
    }

    /**
     * Private constructor
     */
    private CubismDebug() {}
}
