package com.example.textrecognizetask

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class TextRecognizerActivity : AppCompatActivity() {
    lateinit var resultEditText: TextView
    lateinit var ocrImage: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text_recognizer)
        resultEditText = findViewById(R.id.ocrResultEt)
        ocrImage = findViewById(R.id.ocrImageView)
        //get the selected image path from the intent
        val imagePath = intent.getStringExtra("IMAGE_PATH")
        val imageText = intent.getStringExtra("IMAGE_TEXT")
       // ocrImage.setImageBitmap(imagePath)
         resultEditText.append(imageText + "\n")

    }
}
