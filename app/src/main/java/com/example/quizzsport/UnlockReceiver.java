package com.example.quizzsport;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UnlockReceiver extends BroadcastReceiver {
    public UnlockReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
//        StringBuilder msgStr = new StringBuilder("Текущее время: ");
//        Format formatter = new SimpleDateFormat("hh:mm:ss a");
//        msgStr.append(formatter.format(new Date()));
//        Toast.makeText(context, msgStr, Toast.LENGTH_SHORT).show();
        Intent intent1 = new Intent(context, MainActivity.class);
        context.startActivity(intent1);
        System.out.println("There is!");
    }
}