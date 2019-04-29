package com.example.student.smartlighttest1;


import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentActivity;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.content.Intent;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class MainActivity extends FragmentActivity {
    static Intent intent;
    static int id;
    static ArrayList<String> selected = new ArrayList<String>();
    static byte[] info = new byte[1024];
    static Button[] buttons;
    static int value;
    static lamp[] lamps;
    static SeekBar bar;
    static arduino[] ard;
    TextView brighness;
    String text;
    int i;
    static Context context_g;
    static MainActivity activity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context_g = getApplicationContext();
        brighness = new TextView(this);

        brighness = findViewById(R.id.text);
        try {
            File.createTempFile("scenarios", "txt");
        } catch (IOException e) {
            Log.e("Write", e.getMessage());
        }
        activity = this;
        bar = new SeekBar(this);
        bar.findViewById(R.id.seekBar3);
        int max = 255;
        bar.setMax(max);
        bar.setProgress(max);
        bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, final int progress, boolean fromUser) {
                new Runnable() {
                    public void run() {
                        for (String s : selected) {
                            new multithread().execute("send", s + "," + progress);
                        }

                    }
                }.run();

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        Log.d("context", context_g.toString());//@string/_100

        setContentView(R.layout.activity_main);
        new multithread().execute("send", "refresh");
        new multithread().execute("send", "status");
        new multithread().execute("start");
        Button scenario = (Button) findViewById(R.id.button3);
        //builui();
        while (!multithread.isFinished()) {
        }
        ///checked////
        builui();
        new Thread(new getter_from_app()).start();
        read("buttons.txt");
        scenario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent);
            }
        });


    }

    public void builui() {
        lamps = new lamp[udp.colvo];
        buttons = new Button[udp.colvo];
        ard = new arduino[udp.colvo];// /x
        ConstraintLayout layout = findViewById(R.id.main);
        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        intent = new Intent(this, Scenarios.class);
        for (i = 0; i < udp.colvo; i++) {
            buttons[i] = new Button(this);
            buttons[i].setId(i);
            int id_ = buttons[i].getId();
            layout.addView(buttons[i], params);
            buttons[i] = findViewById(id_);
            id = i;
            lamps[i] = new lamp(buttons[i], udp.brignes[i], i);
        }
    }

    public void start() {
        startActivity(intent);
    }/*Calls to open lamps group*/

    public static void read(String name) {
        try {
            // открываем поток для чтения
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    activity.openFileInput(name)));
            String str = "";
            // читаем содержимое

            Log.e("Read", str);
            for (int x = 0; x < udp.colvo && ((str = br.readLine()) != null); x++) {

                String[] x_y = str.split("/");
                buttons[x].setTranslationX(Integer.parseInt(x_y[0]));
                buttons[x].setTranslationY(Integer.parseInt(x_y[1]));

                Log.d("Buttons", "Setted");
            }

        } catch (IOException e) {
            Log.e("Read", e.getMessage());

        }

    }


}














