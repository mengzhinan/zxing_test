/*
 * Copyright (C) 2008 ZXing authors
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

package com.google.zxing.client.android.result;

import android.telephony.PhoneNumberUtils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.result.ParsedResult;
import com.google.zxing.client.result.ParsedResultType;

/**
 * A base class for the Android-specific barcode handlers. These allow the app to polymorphically
 * suggest the appropriate actions for each data type.
 * <p>
 * This class also contains a bunch of utility methods to take common actions like opening a URL.
 * They could easily be moved into a helper object, but it can't be static because the Activity
 * instance is needed to launch an intent.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 * @author Sean Owen
 */
public abstract class ResultHandler {

    // 二维码类型。如：条形码、二维码、PDF417、DataMatrix 等。
    protected final BarcodeFormat barcodeFormat;

    private final ParsedResult result;

    ResultHandler(ParsedResult result, BarcodeFormat pBarcodeFormat) {
        this.result = result;
        barcodeFormat = pBarcodeFormat;
    }

    public final ParsedResult getResult() {
        return result;
    }

    /**
     * Create a possibly styled string for the contents of the current barcode.
     *
     * @return The text to be displayed.
     */
    public CharSequence getDisplayContents() {
        String contents = result.getDisplayResult();
        return contents.replace("\r", "");
    }

    /**
     * A convenience method to get the parsed type. Should not be overridden.
     *
     * @return The parsed type, e.g. URI or ISBN
     */
    public final ParsedResultType getType() {
        return result.getType();
    }


    @SuppressWarnings("deprecation")
    static String formatPhone(String phoneData) {
        // Just collect the call to a deprecated method in one place
        return PhoneNumberUtils.formatNumber(phoneData);
    }

}
