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

import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;

import com.google.zxing.client.result.AddressBookParsedResult;
import com.google.zxing.client.result.ParsedResult;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Handles address book entries.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 */
public final class AddressBookResultHandler extends ResultHandler {

    private static final DateFormat[] DATE_FORMATS = {
            new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH),
            new SimpleDateFormat("yyyyMMdd'T'HHmmss", Locale.ENGLISH),
            new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH),
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH),
    };

    static {
        for (DateFormat format : DATE_FORMATS) {
            format.setLenient(false);
        }
    }

    public AddressBookResultHandler(ParsedResult result) {
        super(result);
    }

    private static long parseDate(String s) {
        for (DateFormat currentFormat : DATE_FORMATS) {
            try {
                Date date = currentFormat.parse(s);
                if (date != null) {
                    return date.getTime();
                }
            } catch (ParseException e) {
                // continue
            }
        }
        return -1L;
    }

    // Overriden so we can hyphenate phone numbers, format birthdays, and bold the name.
    @Override
    public CharSequence getDisplayContents() {
        AddressBookParsedResult result = (AddressBookParsedResult) getResult();
        StringBuilder contents = new StringBuilder(100);
        ParsedResult.maybeAppend(result.getNames(), contents);
        int namesLength = contents.length();

        String pronunciation = result.getPronunciation();
        if (pronunciation != null && !pronunciation.isEmpty()) {
            contents.append("\n(");
            contents.append(pronunciation);
            contents.append(')');
        }

        ParsedResult.maybeAppend(result.getTitle(), contents);
        ParsedResult.maybeAppend(result.getOrg(), contents);
        ParsedResult.maybeAppend(result.getAddresses(), contents);
        String[] numbers = result.getPhoneNumbers();
        if (numbers != null) {
            for (String number : numbers) {
                if (number != null) {
                    ParsedResult.maybeAppend(formatPhone(number), contents);
                }
            }
        }
        ParsedResult.maybeAppend(result.getEmails(), contents);
        ParsedResult.maybeAppend(result.getURLs(), contents);

        String birthday = result.getBirthday();
        if (birthday != null && !birthday.isEmpty()) {
            long date = parseDate(birthday);
            if (date >= 0L) {
                ParsedResult.maybeAppend(DateFormat.getDateInstance(DateFormat.MEDIUM).format(date), contents);
            }
        }
        ParsedResult.maybeAppend(result.getNote(), contents);

        if (namesLength > 0) {
            // Bold the full name to make it stand out a bit.
            Spannable styled = new SpannableString(contents.toString());
            styled.setSpan(new StyleSpan(Typeface.BOLD), 0, namesLength, 0);
            return styled;
        } else {
            return contents.toString();
        }
    }

}
