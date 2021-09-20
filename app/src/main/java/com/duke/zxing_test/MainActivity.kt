package com.duke.zxing_test

import android.Manifest
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.client.android.CaptureActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        DPermission.newInstance(this)
            .setCallback {
                startActivity(Intent(this, CaptureActivity::class.java))
            }.startRequest(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)

    }
}
