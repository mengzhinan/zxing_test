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

package com.google.zxing.client.android.encode;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.android.Contents;
import com.google.zxing.client.android.FinishListener;
import com.google.zxing.client.android.Intents;
import com.google.zxing.client.android.R;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

/**
 * This class encodes data from an Intent into a QR code, and then displays it full screen so that
 * another person can scan it with their device.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 */
public final class EncodeActivity extends Activity {

    private static final String TAG = EncodeActivity.class.getSimpleName();

    private static final String USE_VCARD_KEY = "USE_VCARD";

    private QRCodeEncoder qrCodeEncoder;

    /**
     * 构造 二维码 intent
     */
    public static void setIntentQRCode(Intent intent, String data) {
        if (intent == null) {
            return;
        }
        intent.setAction(Intents.Encode.ACTION);
        intent.putExtra(Intents.Encode.DATA, data);
        intent.putExtra(Intents.Encode.FORMAT, BarcodeFormat.QR_CODE.name());
        // 二维码需要设置 type
        intent.putExtra(Intents.Encode.TYPE, Contents.Type.TEXT);
    }

    /**
     * PDF_417
     */
    public static void setIntentPDF417(Intent intent, String data) {
        if (intent == null) {
            return;
        }
        intent.setAction(Intents.Encode.ACTION);
        intent.putExtra(Intents.Encode.DATA, data);
        intent.putExtra(Intents.Encode.FORMAT, BarcodeFormat.PDF_417.name());
    }

    /**
     * Data_Matrix 二维码
     */
    public static void setIntentDataMatrix(Intent intent, String data) {
        if (intent == null) {
            return;
        }
        String data_iso_8859_1 = null;
        try {
            // 此格式的二维码，必须是 ISO-8859-1
            data_iso_8859_1 = new String(data.getBytes("UTF-8"), "ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        intent.setAction(Intents.Encode.ACTION);
        intent.putExtra(Intents.Encode.DATA, data_iso_8859_1);
        intent.putExtra(Intents.Encode.FORMAT, BarcodeFormat.DATA_MATRIX.name());
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        Intent intent = getIntent();
        if (intent == null) {
            finish();
        } else {
            setContentView(R.layout.encode);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.encode, menu);
        boolean useVcard = qrCodeEncoder != null && qrCodeEncoder.isUseVCard();
        int encodeNameResource = useVcard ? R.string.menu_encode_mecard : R.string.menu_encode_vcard;
        MenuItem encodeItem = menu.findItem(R.id.menu_encode);
        encodeItem.setTitle(encodeNameResource);
        Intent intent = getIntent();
        if (intent != null) {
            String type = intent.getStringExtra(Intents.Encode.TYPE);
            encodeItem.setVisible(Contents.Type.CONTACT.equals(type));
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.menu_encode) {
            Intent intent = getIntent();
            if (intent == null) {
                return false;
            }
            intent.putExtra(USE_VCARD_KEY, !qrCodeEncoder.isUseVCard());
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            return true;
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        // This assumes the view is full screen, which is a good assumption
        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point displaySize = new Point();
        display.getSize(displaySize);
        int width = displaySize.x;
        int height = displaySize.y;
        int smallerDimension = Math.min(width, height);
        smallerDimension = smallerDimension * 7 / 8;

        Intent intent = getIntent();
        if (intent == null) {
            return;
        }

        try {
            boolean useVCard = intent.getBooleanExtra(USE_VCARD_KEY, false);
            qrCodeEncoder = new QRCodeEncoder(this, intent, smallerDimension, useVCard);
            Bitmap bitmap = qrCodeEncoder.encodeAsBitmap();
            if (bitmap == null) {
                Log.w(TAG, "Could not encode barcode");
                showErrorMessage(R.string.msg_encode_contents_failed);
                qrCodeEncoder = null;
                return;
            }

            ImageView view = findViewById(R.id.image_view);
            view.setImageBitmap(bitmap);

            TextView contents = findViewById(R.id.contents_text_view);
            if (intent.getBooleanExtra(Intents.Encode.SHOW_CONTENTS, true)) {
                contents.setText(qrCodeEncoder.getDisplayContents());
                setTitle(qrCodeEncoder.getTitle());
            } else {
                contents.setText("");
                setTitle("");
            }
        } catch (WriterException e) {
            Log.w(TAG, "Could not encode barcode", e);
            showErrorMessage(R.string.msg_encode_contents_failed);
            qrCodeEncoder = null;
        }
    }

    private void showErrorMessage(int message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setPositiveButton(R.string.button_ok, new FinishListener(this));
        builder.setOnCancelListener(new FinishListener(this));
        builder.show();
    }
}
