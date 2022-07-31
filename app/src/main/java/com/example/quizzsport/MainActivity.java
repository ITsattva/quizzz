package com.example.quizzsport;

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
    private static short answersCount = 2;
    private MyDB db;
    private RadioGroup radioGroup;
    private RadioButton button1;
    private RadioButton button2;
    private RadioButton button3;
    private RadioButton button4;
    private Button answerButton;
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
        answerButton = findViewById(R.id.b_answer);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = radioGroup.getCheckedRadioButtonId();
                RadioButton radio = findViewById(id);
                String currentAnswer = radio.getText().toString();

                if (checkAnswer(currentAnswer)) {
                    showRight();
                    radioGroup.clearCheck();
                } else {
                    showWrong();
                }
            }
        };
        answerButton.setOnClickListener(listener);

        db = new MyDB(this);
        //dbManager.insertIntoDB("первый вопрос", new String[]{"1", "2", "3", "4"});
        questions = db.getQuestionsFromDB();
        createQuestion();
    }

    private boolean checkAnswer(String answer) {
        answersCount--;

        for (Question question : questions) {
            System.out.println(question.getDescription());
            if (question.getAnswer1().equals(answer)) {
                return true;
            }
        }

        return false;
    }


    private void createQuestion() {
        answersCount = 2;
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
        if (answersCount <= 0) {
            Toast toast = Toast.makeText(MainActivity.this, "Ответ неверный!", Toast.LENGTH_SHORT);
            toast.show();
            createQuestion();
        } else {
            answersCount--;
            Toast toast = Toast.makeText(MainActivity.this, "Ответ неверный! У тебя есть еще одна попытка!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private void showRight() {
        answersCount = 2;
        Toast toast = Toast.makeText(MainActivity.this, "Молодец!", Toast.LENGTH_SHORT);
        toast.show();
    }

    private String[] mixAnswers(String[] array) {
        String[] copy = array.clone();
        List<String> list = Arrays.asList(copy);
        Collections.shuffle(list);
        String[] arr = (String[]) list.toArray();

        return arr;
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