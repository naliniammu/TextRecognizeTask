package com.example.textrecognizetask

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.text.FirebaseVisionText
import kotlinx.android.synthetic.main.activity_test.*

class TestActivity : AppCompatActivity() {
    lateinit var ocrImage: ImageView
    lateinit var resultEditText: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        ocrImage = findViewById(R.id.ocrImageView)
        //set an onclick listener on the button to trigger the @pickImage() method
        selectImageBtn.setOnClickListener {
            pickImage()

        }

        //set an onclick listener on the button to trigger the @processImage method
        processImageBtn.setOnClickListener {
            processImage(processImageBtn)
            /* val intent = Intent(this, TextRecognizerActivity::class.java)
             //  intent.putExtra("IMAGE_PATH", imagePath)
             intent.putExtra("IMAGE_TEXT", finalText)
             startActivity(intent)*/
        }
    }

    fun pickImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            ocrImage.setImageURI(data!!.data)

        }
    }

    fun processImage(v: View) {
        if (ocrImage.drawable != null) {
            resultEditText.setText("")

            v.isEnabled = false
            val bitmap = (ocrImage.drawable as BitmapDrawable).bitmap
            val image = FirebaseVisionImage.fromBitmap(bitmap)
            val detector = FirebaseVision.getInstance().onDeviceTextRecognizer

            detector.processImage(image)
                .addOnSuccessListener { firebaseVisionText ->
                    v.isEnabled = true
                    processResultText(firebaseVisionText)
                }
                .addOnFailureListener {
                    v.isEnabled = true
                    //  resultEditText.setText("Failed")
                    Toast.makeText(this, "Failed", Toast.LENGTH_LONG).show()
                }
        } else {
            Toast.makeText(this, "Select an Image First", Toast.LENGTH_LONG).show()
        }

    }

    private fun processResultText(resultText: FirebaseVisionText) {
        if (resultText.textBlocks.size == 0) {
            Toast.makeText(this, "No Text Found", Toast.LENGTH_LONG).show()
            return
        }
        for (block in resultText.textBlocks) {
            val blockText = block.text
            resultEditText.append(blockText + "\n")
        }

    }
}
