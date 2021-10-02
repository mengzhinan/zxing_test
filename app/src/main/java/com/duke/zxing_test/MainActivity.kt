package com.duke.zxing_test

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.client.android.CaptureActivity

class MainActivity : AppCompatActivity() {

    private var tvScan: TextView? = null
    private var tvProductCode: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvScan = findViewById(R.id.tv_scan)
        tvProductCode = findViewById(R.id.tv_product_code)

        tvScan?.setOnClickListener {
            scan()
        }

        tvProductCode?.setOnClickListener {

        }

        scan()
    }

    private fun scan() {
        DPermission.newInstance(this)
            .setCallback {
                startActivity(Intent(this, CaptureActivity::class.java))
            }.startRequest(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }
}
