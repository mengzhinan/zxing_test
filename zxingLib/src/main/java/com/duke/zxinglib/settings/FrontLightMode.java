/*
 * Copyright (C) 2012 ZXing authors
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

package com.duke.zxinglib.settings;

/**
 * Enumerates settings of the preference controlling the front light.
 */
public class FrontLightMode {

    /**
     * On only when ambient light is low.
     */
    public static final int MODE_AUTO = 0;

    /**
     * Always on.
     */
    public static final int MODE_ON = 1;

    /**
     * Always off.
     */
    public static final int MODE_OFF = 2;

    private static int mode = MODE_OFF;

    public static boolean isModeAuto() {
        return mode == MODE_AUTO;
    }

    public static boolean isModeOn() {
        return mode == MODE_ON;
    }

    public static boolean isModeOff() {
        return mode == MODE_OFF;
    }

}
