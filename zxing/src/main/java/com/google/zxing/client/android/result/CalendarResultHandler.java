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

import android.app.Activity;

import com.google.zxing.client.result.CalendarParsedResult;
import com.google.zxing.client.result.ParsedResult;

import java.text.DateFormat;

/**
 * Handles calendar entries encoded in QR Codes.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 * @author Sean Owen
 */
public final class CalendarResultHandler extends ResultHandler {

    public CalendarResultHandler(Activity activity, ParsedResult result) {
        super(activity, result);
    }

    @Override
    public CharSequence getDisplayContents() {

        CalendarParsedResult calResult = (CalendarParsedResult) getResult();
        StringBuilder result = new StringBuilder(100);

        ParsedResult.maybeAppend(calResult.getSummary(), result);

        long start = calResult.getStartTimestamp();
        ParsedResult.maybeAppend(format(calResult.isStartAllDay(), start), result);

        long end = calResult.getEndTimestamp();
        if (end >= 0L) {
            if (calResult.isEndAllDay() && start != end) {
                // Show only year/month/day
                // if it's all-day and this is the end date, it's exclusive, so show the user
                // that it ends on the day before to make more intuitive sense.
                // But don't do it if the event already (incorrectly?) specifies the same start/end
                end -= 24 * 60 * 60 * 1000;
            }
            ParsedResult.maybeAppend(format(calResult.isEndAllDay(), end), result);
        }

        ParsedResult.maybeAppend(calResult.getLocation(), result);
        ParsedResult.maybeAppend(calResult.getOrganizer(), result);
        ParsedResult.maybeAppend(calResult.getAttendees(), result);
        ParsedResult.maybeAppend(calResult.getDescription(), result);
        return result.toString();
    }

    private static String format(boolean allDay, long date) {
        if (date < 0L) {
            return null;
        }
        DateFormat format = allDay
                ? DateFormat.getDateInstance(DateFormat.MEDIUM)
                : DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM);
        return format.format(date);
    }


}
