package com.duke.zxing_test

import android.Manifest
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.client.android.CaptureActivity
import com.google.zxing.client.android.Intents
import com.google.zxing.client.android.encode.EncodeActivity

class MainActivity : AppCompatActivity() {

    private var tvScan: TextView? = null
    private var tvProductCodeQRCode: TextView? = null
    private var tvProductCodePDF417: TextView? = null
    private var tvProductCodeDataMatrix: TextView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvScan = findViewById(R.id.tv_scan)
        tvProductCodeQRCode = findViewById(R.id.tv_product_code_qrcode)
        tvProductCodePDF417 = findViewById(R.id.tv_product_code_pdf_417)
        tvProductCodeDataMatrix = findViewById(R.id.tv_product_code_data_matrix)

        tvScan?.setOnClickListener {
            scan()
        }

        tvProductCodeQRCode?.setOnClickListener {
            val data = tvProductCodeQRCode?.text?.toString() ?: "无"
            val intent = Intent(this, EncodeActivity::class.java)

            val logo = BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher)
            EncodeActivity.setIntentQRCode(intent, data, logo)
            startActivity(intent)
        }

        tvProductCodePDF417?.setOnClickListener {
            val data = tvProductCodePDF417?.text?.toString() ?: "无"
            val intent = Intent(this, EncodeActivity::class.java)
            EncodeActivity.setIntentPDF417(intent, data)
            startActivity(intent)
        }

        tvProductCodeDataMatrix?.setOnClickListener {
            val data = tvProductCodeDataMatrix?.text?.toString() ?: "无"
            val intent = Intent(this, EncodeActivity::class.java)
            EncodeActivity.setIntentDataMatrix(intent, data)
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
