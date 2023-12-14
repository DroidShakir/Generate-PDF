package com.protocol.generatepdf

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.os.Environment
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.protocol.generatepdf.databinding.ActivityMainBinding
import java.io.File
import java.io.FileOutputStream

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var pageHeight = 1120
    private var pageWidth = 792

    private lateinit var bmp: Bitmap
    private lateinit var scaledBmp: Bitmap
    private var permissionCode = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bmp = BitmapFactory.decodeResource(resources, R.drawable.android)
        scaledBmp = Bitmap.createScaledBitmap(bmp, 140, 140, false)

        if (this.checkPermissions()) {
            Toast.makeText(this, "Permissions Granted..", Toast.LENGTH_SHORT).show()
        } else {
            this.requestPermission()
        }

        binding.idBtnGeneratePdf.setOnClickListener {
            this.generatePDF()
        }
    }

    private fun generatePDF() {
        val pdfDocument: PdfDocument = PdfDocument()
        val paint: Paint = Paint()
        val title: Paint = Paint()

        val myPageInfo: PdfDocument.PageInfo? =
            PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create()
        val myPage: PdfDocument.Page = pdfDocument.startPage(myPageInfo)
        val canvas: Canvas = myPage.canvas

        canvas.drawBitmap(scaledBmp, 56F, 40F, paint)
        title.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        title.textSize = 15F
        title.color = ContextCompat.getColor(this, R.color.purple)

        canvas.drawText("A portal for IT professionals.", 209F, 100F, title)
        canvas.drawText("Geeks for Geeks", 209F, 80F, title)
        title.typeface = Typeface.defaultFromStyle(Typeface.NORMAL)
        title.color = ContextCompat.getColor(this, R.color.purple)
        title.textSize = 15F

        title.textAlign = Paint.Align.CENTER
        canvas.drawText("This is sample document which we have created.", 396F, 560F, title)
        pdfDocument.finishPage(myPage)
        val file: File = File(Environment.getExternalStorageDirectory(), "GFG.pdf")

        try {
            pdfDocument.writeTo(FileOutputStream(file))
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(applicationContext, "Fail to generate PDF file..", Toast.LENGTH_SHORT)
                .show()
        }
        pdfDocument.close()
    }

    private fun checkPermissions(): Boolean {
        val writeStoragePermission = ContextCompat.checkSelfPermission(
            applicationContext,
            WRITE_EXTERNAL_STORAGE
        )

        val readStoragePermission = ContextCompat.checkSelfPermission(
            applicationContext,
            READ_EXTERNAL_STORAGE
        )

        return writeStoragePermission == PackageManager.PERMISSION_GRANTED
                && readStoragePermission == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE), permissionCode
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == permissionCode) {
            if (grantResults.isNotEmpty()) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1]
                    == PackageManager.PERMISSION_GRANTED
                ) {
                    Toast.makeText(this, "Permission Granted..", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Permission Denied..", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }
}