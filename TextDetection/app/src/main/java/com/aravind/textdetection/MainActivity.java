package com.aravind.textdetection;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;

public class MainActivity extends AppCompatActivity {

    private Button openCamera;
    private static final int REQUEST_CAMERA_CAPTURE = 123;
    private FirebaseVisionTextRecognizer textDetector;
    private FirebaseVisionImage image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseApp.initializeApp(this);
        openCamera = findViewById(R.id.camera_button);

        openCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent captureText = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (captureText.resolveActivity(getPackageManager())!=null){
                    startActivityForResult(captureText,REQUEST_CAMERA_CAPTURE);
                }

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == REQUEST_CAMERA_CAPTURE && resultCode == RESULT_OK && data != null){

            Bundle extras = data.getExtras();
            Bitmap bitmap = (Bitmap) extras.get("data");

            textDetector(bitmap);

        }
    }

    private void textDetector(Bitmap bitmap) {

        try {
            image = FirebaseVisionImage.fromBitmap(bitmap);
            textDetector = FirebaseVision.getInstance()
                    .getOnDeviceTextRecognizer();
        } catch (Exception e) {
            e.printStackTrace();
        }

        textDetector.processImage(image).addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
            @Override
            public void onSuccess(FirebaseVisionText firebaseVisionText) {
                String recognizedText = firebaseVisionText.getText();
                if (!recognizedText.equals("")){
                    Intent intent = new Intent(MainActivity.this,ResultActivity.class);
                    intent.putExtra(MyTextDetection.RESULT_TEXT,recognizedText);
                    startActivity(intent);
                }else {
                    Toast.makeText(MainActivity.this, "No Text is There.", Toast.LENGTH_SHORT).show();
                }
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Sorry TextNotFound"+e.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
