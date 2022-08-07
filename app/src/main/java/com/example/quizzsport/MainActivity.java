package com.example.quizzsport;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quizzsport.db.Constants;
import com.example.quizzsport.db.MyDB;
import com.example.quizzsport.model.Question;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private String rightAnswer;
    private MyDB db;
    private RadioGroup radioGroup;
    private RadioButton button1;
    private RadioButton button2;
    private RadioButton button3;
    private RadioButton button4;
    private TextView upsideText;
    private List<Question> questions = new ArrayList<>();
    private List<RadioButton> buttons = new ArrayList<>();
    private UnlockReceiver unlockReceiver = new UnlockReceiver();


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch(id) {
            case R.id.artro:
                db.setTable("sport");
                questions = db.getQuestionsFromDB();
                createQuestion();

                return true;
            case R.id.muscle:
                db.setTable("muscles");
                questions = db.getQuestionsFromDB();
                createQuestion();

                return true;
            case R.id.sql:
                db.setTable("sql");
                questions = db.getQuestionsFromDB();
                createQuestion();

                return true;
            case R.id.spring_eng:
                db.setTable("spring_eng");
                questions = db.getQuestionsFromDB();
                createQuestion();

                return true;
        }

        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        registerBroadcastReceiver();


        radioGroup = findViewById(R.id.rg_1);
        button1 = findViewById(R.id.b_1);
        button2 = findViewById(R.id.b_2);
        button3 = findViewById(R.id.b_3);
        button4 = findViewById(R.id.b_4);
        upsideText = findViewById(R.id.tv_upside);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
//                if (i==-1) {
//                    return;
//                }
                int id = radioGroup.getCheckedRadioButtonId();
                RadioButton radio = findViewById(id);
                String currentAnswer = "";
                if (radio != null) {
                    currentAnswer = radio.getText().toString();
                    if (checkAnswer(currentAnswer)) {
                        showRight();
                    } else {
                        showWrong();
                        findRightAnswer();
                    }
                    radioGroup.clearCheck();
                    restart(1000L);
                }
            }
        });

        db = new MyDB(this);
        questions = db.getQuestionsFromDB();
        createQuestion();
        buttons = Arrays.asList(button1, button2, button3, button4);
    }

    @Override
    protected void onPause() {
        super.onPause();
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                int id = radioGroup.getCheckedRadioButtonId();
                RadioButton radio = findViewById(id);
                String currentAnswer = "";
                if (radio != null) {
                    currentAnswer = radio.getText().toString();
                    if (checkAnswer(currentAnswer)) {
                        showRight();
                    } else {
                        showWrong();
                        findRightAnswer();
                    }
                    radioGroup.clearCheck();
                    restartOnResume(1000L);
                }
            }
        });
    }

    private void restart(Long millis) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                restartButtons();
                createQuestion();
            }
        }, millis);
    }

    private void restartOnResume(Long millis) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                restartButtons();
                createQuestion();
                rollUp();
            }
        }, millis);
    }

    public void rollUp() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }

    private boolean checkAnswer(String answer) {
        for (Question question : questions) {
            System.out.println(question.getDescription());
            if (question.getAnswer1().equals(answer)) {
                return true;
            }
        }
        return false;
    }


    private void createQuestion() {
        Random random = new Random();
        int rand = random.nextInt(questions.size());

        Question question = questions.get(rand);
        rightAnswer = question.getAnswer1();

        upsideText.setText(question.getDescription());
        String[] array = mixAnswers(new String[]{question.getAnswer1(), question.getAnswer2(),
                                                question.getAnswer3(), question.getAnswer4()});
        button1.setText(array[0]);
        button2.setText(array[1]);
        button3.setText(array[2]);
        button4.setText(array[3]);
    }

    private void showWrong() {
        int id = radioGroup.getCheckedRadioButtonId();
        RadioButton radio = findViewById(id);
        radio.setBackgroundColor(Color.rgb(180, 50, 50));
        radio.getBackground().setAlpha(80);
    }

    private void showRight() {
        int id = radioGroup.getCheckedRadioButtonId();
        RadioButton radio = findViewById(id);
        radio.setBackgroundColor(Color.rgb(50, 180, 50));
        radio.getBackground().setAlpha(80);
    }

    private String[] mixAnswers(String[] array) {
        String[] copy = array.clone();
        List<String> list = Arrays.asList(copy);
        Collections.shuffle(list);
        String[] arr = (String[]) list.toArray();

        return arr;
    }

    private void restartButtons(){
        button1.setBackgroundColor(Color.TRANSPARENT);
        button2.setBackgroundColor(Color.TRANSPARENT);
        button3.setBackgroundColor(Color.TRANSPARENT);
        button4.setBackgroundColor(Color.TRANSPARENT);
    }

    private void findRightAnswer(){
        for (RadioButton button : buttons) {
            if (button.getText().toString().equals(rightAnswer)) {
                button.setBackgroundColor(Color.rgb(50, 180, 50));
                button.getBackground().setAlpha(80);
            }
        }
    }

    private void copyDataBase() throws IOException {
        InputStream myInput = this.getAssets().open("questions.db");
        String outFileName = Constants.DB_PATH+"quiz";
        OutputStream myOutput = new FileOutputStream(outFileName);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer))>0){
            System.out.println("PROCESS");
            myOutput.write(buffer, 0, length);
        }
        //закрываем потоки
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    public void registerBroadcastReceiver() {
        this.registerReceiver(unlockReceiver, new IntentFilter(
                "android.intent.action.SCREEN_ON"));
    }

    // Отменяем регистрацию
    public void unregisterBroadcastReceiver(View view) {
        this.unregisterReceiver(unlockReceiver);

        Toast.makeText(getApplicationContext(), "Приёмник выключён", Toast.LENGTH_SHORT)
                .show();
    }

}