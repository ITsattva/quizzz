package com.example.quizzsport;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.KeyguardManager;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Button button1;
    private Button button2;
    private Button button3;
    private Button button4;
    private TextView upsideText;
    private TextView bellowText;

//    @RequiresApi(api = Build.VERSION_CODES.O_MR1)
    @RequiresApi(api = Build.VERSION_CODES.O_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        setTurnScreenOn(true);
        setShowWhenLocked(true);

        button1 = findViewById(R.id.b_1);
        button2 = findViewById(R.id.b_2);
        button3 = findViewById(R.id.b_3);
        button4 = findViewById(R.id.b_4);
        upsideText = findViewById(R.id.tv_upside);
        bellowText = findViewById(R.id.tv_bellow);

//        bellowText.setText("");
//        upsideText.setText("--");
//        button1.setText("-");
//        button2.setText("-");
//        button3.setText("-");
//        button4.setText("-");

        String buffer = "";

        View.OnClickListener onClickListener1 = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(button1.getText());
                bellowText.setText(button1.getText());
            }
        };
        View.OnClickListener onClickListener2 = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(button2.getText());
                bellowText.setText("Ну яка ж ти кобилка? Ти моя любімовка!");
            }
        };
        View.OnClickListener onClickListener3 = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(button3.getText());
                bellowText.setText("Ну яка ж ти котя? Ти моя любімовка!");
            }
        };
        View.OnClickListener onClickListener4 = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(button4.getText());
                bellowText.setText("Ну яка ж ти бабаска? Ти моя любімовка!");
            }
        };

        button1.setOnClickListener(onClickListener1);
        button2.setOnClickListener(onClickListener2);
        button3.setOnClickListener(onClickListener3);
        button4.setOnClickListener(onClickListener4);
    }
}