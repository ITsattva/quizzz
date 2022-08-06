package com.example.quizzsport;

import android.graphics.Color;
import android.os.Bundle;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private MyDB db;
    private RadioGroup radioGroup;
    private RadioButton button1;
    private RadioButton button2;
    private RadioButton button3;
    private RadioButton button4;
    //private Button answerButton;
    private TextView upsideText;
    private List<Question> questions = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        radioGroup = findViewById(R.id.rg_1);
        button1 = findViewById(R.id.b_1);
        button2 = findViewById(R.id.b_2);
        button3 = findViewById(R.id.b_3);
        button4 = findViewById(R.id.b_4);
        upsideText = findViewById(R.id.tv_upside);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int id = radioGroup.getCheckedRadioButtonId();
                RadioButton radio = findViewById(id);
                String currentAnswer = radio.getText().toString();

                if (checkAnswer(currentAnswer)) {
                    showRight();

                        restartButtons();
                        radioGroup.clearCheck();
                        createQuestion();
                } else {
                    showWrong();

                        restartButtons();
                        radioGroup.clearCheck();
                        createQuestion();
                }
            }
        });

        db = new MyDB(this);
        questions = db.getQuestionsFromDB();
        createQuestion();
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
        //answerButton.setText("ДАЛЕЕ");
    }

    private void showRight() {
        int id = radioGroup.getCheckedRadioButtonId();
        RadioButton radio = findViewById(id);
        radio.setBackgroundColor(Color.rgb(50, 180, 50));
        radio.getBackground().setAlpha(80);
        //answerButton.setText("ДАЛЕЕ");
    }

    private String[] mixAnswers(String[] array) {
        String[] copy = array.clone();
        List<String> list = Arrays.asList(copy);
        Collections.shuffle(list);
        String[] arr = (String[]) list.toArray();

        return arr;
    }

    private void restartButtons(){
//        button1.getBackground().setAlpha(0);
//        button2.getBackground().setAlpha(0);
//        button3.getBackground().setAlpha(0);
//        button4.getBackground().setAlpha(0);
        button1.setBackgroundColor(Color.TRANSPARENT);
        button2.setBackgroundColor(Color.TRANSPARENT);
        button3.setBackgroundColor(Color.TRANSPARENT);
        button4.setBackgroundColor(Color.TRANSPARENT);
        //answerButton.setText("ОТВЕТ!");
    }

    private void copyDataBase() throws IOException {
        System.out.println("COPYING DB");
        //Открываем локальную БД как входящий поток
        InputStream myInput = this.getAssets().open("questions.db");
        System.out.println("After searching copied db");
        //Путь ко вновь созданной БД
        String outFileName = Constants.DB_PATH+"quiz";

        //Открываем пустую базу данных как исходящий поток
        OutputStream myOutput = new FileOutputStream(outFileName);

        //перемещаем байты из входящего файла в исходящий
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

}