package com.aravind.textdetection;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {

    private Button backButton;
    private TextView result;
    String resultText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        backButton = findViewById(R.id.back_button);
        result = findViewById(R.id.result_textview);

        resultText = getIntent().getStringExtra(MyTextDetection.RESULT_TEXT);
        result.setText(resultText);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
