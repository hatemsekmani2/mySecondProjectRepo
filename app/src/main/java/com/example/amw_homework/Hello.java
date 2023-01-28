package com.example.amw_homework;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.widget.Toast;

import java.time.Duration;
import java.util.Timer;
import java.util.TimerTask;

public class Hello extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello);

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            public void uncaughtException(Thread paramThread, Throwable paramThrowable) {
                System.out.println("hello");
                Toast.makeText(getApplicationContext(),"Error"+Thread.currentThread().getStackTrace()[2].toString(),Toast.LENGTH_LONG).show();
            }
        });

        Context context = this;

        ActionBar actionBar;
        actionBar = getSupportActionBar();
        actionBar.setElevation(0);
        Spannable spannablerTitle = new SpannableString("");
        spannablerTitle.setSpan(new ForegroundColorSpan(Color.parseColor("#ffffff")), 0, spannablerTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        actionBar.setTitle(spannablerTitle);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        findViewById(R.id.hello).getLayoutParams().width =2*width/3;
        Timer timer = new Timer();


        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                SharedPreferences p = getSharedPreferences("edit",Context.MODE_PRIVATE);
                if(!p.contains("type"))
                startActivity(new Intent(context,Login.class));
                else{
                    if(p.getString("type","").equals("admin"))
                    startActivity(new Intent(context,AdminMainActivity.class));
                    else
                    startActivity(new Intent(context,MainActivity.class));

                }
                finish();
            }
        } ,3000);



    }
}
