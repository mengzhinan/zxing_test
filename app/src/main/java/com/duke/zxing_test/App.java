package com.duke.zxing_test;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;

/**
 * Author: duke
 * DateTime: 2021-09-20 22-38
 * Description: 功能说明
 */
public class App extends Application {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);


    }
}
