package com.duke.zxing_test

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.client.android.CaptureActivity
import com.google.zxing.client.android.Intents
import com.google.zxing.client.android.encode.EncodeActivity

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
            val intent = Intent(this, EncodeActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onResume() {
        super.onResume()
        DPermission.newInstance(this)
            .setCallback {
                // to do
            }.startRequest(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }

    private fun scan() {
        val intent = Intent(this, CaptureActivity::class.java)

        val minLength =
            Math.min(DisplayUtil.getHeightPixels(this), DisplayUtil.getWidthPixels(this))

        intent.putExtra(Intents.Scan.WIDTH, minLength - 50)
        intent.putExtra(Intents.Scan.HEIGHT, minLength - 50)

        startActivity(intent)

    }
}
