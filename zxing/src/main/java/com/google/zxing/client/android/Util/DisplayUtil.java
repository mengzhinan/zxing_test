package com.google.zxing.client.android.Util;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

/**
 * Author: duke
 * DateTime: 2021-09-25 23-32
 * Description: 功能说明
 */
public class DisplayUtil {

    public static Point getScreenSize(Context context) {
        if (context == null) {
            return null;
        }
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        return point;
    }

}
