package com.google.zxing.client.android.util;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;

/**
 * Author: duke
 * DateTime: 2021-09-30 17-12
 * Description: 功能说明
 */
public class ScanLineHelper {

    private final RelativeLayout root;
    private final ImageView scanLine;
    private int yLimit = 0;
    private boolean isLoop = false;
    private final MyHandler handler;

    public void setLoop(boolean loop) {
        isLoop = loop;
        if (!isLoop) {
            handler.removeCallbacksAndMessages(null);
            return;
        }
        root.post(() -> {
            yLimit = root.getMeasuredHeight() - scanLine.getMeasuredHeight();
            scanLine.setY(0);
            handler.sendEmptyMessage(0);
        });
    }

    public ScanLineHelper(RelativeLayout pRoot, ImageView pScanLine) {
        this.root = pRoot;
        this.scanLine = pScanLine;
        handler = new MyHandler();
    }

    @SuppressLint("HandlerLeak")
    private class MyHandler extends Handler {
        private MyHandler() {
            super(Looper.getMainLooper());
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            scanLine.setY(scanLine.getY() + 20);
            if (scanLine.getY() > yLimit) {
                scanLine.setY(0);
            }
            sendMessageDelayed(Message.obtain(), 30);
        }
    }
}
