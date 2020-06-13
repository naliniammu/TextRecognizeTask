package com.example.textrecognizetask

import android.annotation.SuppressLint
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.text.FirebaseVisionText

class DetailsActivity : AppCompatActivity() {
    lateinit var resultEditText: TextView
    lateinit var ocrImage: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text_recognizer)
        resultEditText = findViewById(R.id.ocrResultEt)
        ocrImage = findViewById(R.id.ocrImageView)
        val imageUri = intent.getParcelableExtra<Uri>(TestActivity.imageUri)

        if (imageUri != null) {
            ocrImage.setImageURI(imageUri)
            processImage()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun processImage() {
        if (ocrImage.drawable != null) {
            resultEditText.setText("")
            val bitmap = (ocrImage.drawable as BitmapDrawable).bitmap
            val image = FirebaseVisionImage.fromBitmap(bitmap)
            val detector = FirebaseVision.getInstance().onDeviceTextRecognizer

            detector.processImage(image)
                .addOnSuccessListener { firebaseVisionText ->
                    processResultText(firebaseVisionText)
                }
                .addOnFailureListener {
                    resultEditText.setText("Failed")

                }
        } else {
            resultEditText.setText("Select Image First")
        }
    }

    private fun processResultText(resultText: FirebaseVisionText) {
        if (resultText.textBlocks.size == 0) {
            resultEditText.setText("Not Found")
            return
        }
        for (block in resultText.textBlocks) {
            val blockText = block.text
            resultEditText.append(blockText + "\n")
        }
    }
}
