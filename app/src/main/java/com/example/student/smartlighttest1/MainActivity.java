package com.example.student.smartlighttest1;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;

import static android.os.Environment.getExternalStorageDirectory;

//import android.support.v7.app.AppCompatActivity;


public class MainActivity extends FragmentActivity implements SeekBar.OnSeekBarChangeListener, Button.OnClickListener {
    static HashSet<selectable> selected = new HashSet<>();
    static Button[] buttons;
    static lamp[] lamps;
    static Vibrator v;
    TextView brighness;
    static SeekBar bar;
    int i;
    static Context context_g;
    static MainActivity activity;
    static Button new_scenario;
    static Button new_group;
    LinearLayout buttonPanel;
    static boolean inScenMode =false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context_g = getApplicationContext();
        brighness = (TextView) findViewById(R.id.text);
        new_group = findViewById(R.id.NEW_GROUP);
        new_group.setOnClickListener(this);
        new_scenario = findViewById(R.id.NEW_SCENARIO);
        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        activity = this;
        buttonPanel=(LinearLayout)findViewById(R.id.buttonPanel);
        bar = findViewById(R.id.BRIGNESS);
        Button group=findViewById(R.id.GROUP);
        group.setOnClickListener(this);
        bar.setOnSeekBarChangeListener(this);
        getter_from_app.writeToFile("1,2 100#200 1\n" +
                "3,4 300#400 0\n" +
                "5,6 500#600 1\n" +
                "7,8 700#800 0\n" +
                "9,10 900#1000 1","pairs.txt",MODE_PRIVATE);
        try {
            savedInstanceState.getBoolean("null");
        } catch (Exception e) {
            udp.setup();
            //new multithread().execute("send", "refresh");
            new multithread().execute("send", "status");
            new multithread().execute("start");
        }
        Button scenario = findViewById(R.id.SCENARIO);
        int timer = 0;
        while (!multithread.isFinished()) {

            /*timer++;
            delay(500);
            if (timer>=30)
            {
               udp.colvo=0;
               break;
            }*/
        }
        Log.d("Android",Environment.getExternalStorageDirectory()
                .getAbsolutePath());
        builui();
        if (timer >= 30)
            Toast.makeText(this, "ОЩИБКА!!!\n Проверьте соединение с сервером", Toast.LENGTH_LONG).show();
        new Thread(new getter_from_app()).start();

        scenario.setOnClickListener(this);
        Button settings = findViewById(R.id.SETTINGS);
        settings.setOnClickListener(this);
        new_group.setOnClickListener(this);
        new_scenario.setOnClickListener(this);



        Log.d("onCreate","complite");
    }

    public static void delay(int milis) {
        try {
            Thread.sleep(milis);
        } catch (Exception e) {
            Log.e("timer", "error");
        }
    }


    private void builui() {
        lamps = new lamp[udp.colvo];
        buttons = new Button[udp.colvo];
        ConstraintLayout layout = findViewById(R.id.Main);
        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT);
        BufferedReader reader = null;
        layout.setOnTouchListener(new OnSwipeTouchListener() {
            public void onSwipeTop() {
                buttonPanel.setVisibility(View.VISIBLE);
            }
            public void onSwipeRight() {
            }
            public void onSwipeLeft() {
            }
            public void onSwipeBottom() {
                buttonPanel.setVisibility(View.INVISIBLE);
            }

            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });

        try {
            final File file = new File(Environment.getExternalStorageDirectory()
                    .getAbsolutePath(), "pairs.txt");

            //reader = new BufferedReader(new FileReader(file));
            reader = new BufferedReader(new InputStreamReader(activity.openFileInput("pairs.txt")));
            for (i = 0; i < udp.colvo/2; i++) {
                buttons[i] = new Button(this);
                buttons[i].setId(i);
                int id_ = buttons[i].getId();
                layout.addView(buttons[i], params);
                buttons[i] = findViewById(id_);
                buttons[i].setLayoutParams(new ConstraintLayout.LayoutParams(67, 67));

                try {
                    lamps[i] = new lamp(buttons[i], reader.readLine());
                } catch (IOException E) {
                    Log.e("pairs",E.getLocalizedMessage());
                }

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Log.d("build","complite");
    }

    public static void read(String name, int int_max) {
        BufferedReader br = null;
        try {
            // открываем поток для чтения
            final File file = new File(Environment.getExternalStorageDirectory()
                    .getAbsolutePath(), "pairs.txt");
            br = new BufferedReader(new FileReader(file));
            String str = "";
            // читаем содержимое

            Log.e("Read", str);
            for (int x = 0; x < int_max && ((str = br.readLine()) != null); x++) {

                try {
                    String[] x_y = str.split("/");
                    buttons[x].setTranslationX(Integer.parseInt(x_y[0]));
                    buttons[x].setTranslationY(Integer.parseInt(x_y[1]));

                    Log.d("Buttons", "Setted");
                } catch (Exception e) {
                    Log.e("read", e.getLocalizedMessage());
                }

            }


        } catch (IOException e) {
            Log.e("Read", e.getMessage());

        } finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    static void vibrate(int miliis) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(miliis, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(miliis);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        new_scenario.setText("Добавить новый сценарий");
        new_scenario.setOnClickListener(this);
        new_group.setText("Добавить новую группу");
        new_group.setOnClickListener(this);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, final int progress, boolean fromUser) {

        brighness.setText("Яркость :" + progress);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(final SeekBar seekBar) {
        final int progress = seekBar.getProgress();
        String brighnes = "" + progress;
        if (progress < 10) brighnes = "00" + progress;
        else if (progress < 100) brighnes = "0" + progress;
        if (selected.size()<=0)return;
        for (selectable Selected : selected) {
            delay(75);
            lamp Lamp=null;
            if (Selected instanceof group){new multithread().execute("send", Selected.getId() + brighnes);continue;}
            else Lamp=(lamp)Selected;

            if (Lamp.iterator < 2) {
               if (!inScenMode)new multithread().execute("send", Selected.getId() + brighnes);
                switch (Lamp.iterator) {
                    case 1:
                        Lamp.setBright2(seekBar.getProgress());
                        break;
                    case 0:
                        Lamp.setBright(seekBar.getProgress());
                        break;
                }
            } else {
                if (!inScenMode){String[] s = Selected.getId().split(",");
                new multithread().execute("send", s[0] + brighnes);
                new multithread().execute("send", s[1] + brighnes);}
                Lamp.setBright(seekBar.getProgress());
                Lamp.setBright2(seekBar.getProgress());
            }
            Lamp.changeBackground();
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.SCENARIO:
                final Intent intent = new Intent(this, Scenarios.class);
                startActivity(intent);
                break;
            case R.id.NEW_GROUP:
                int n = 0;
                final ArrayList<String> selectedLamps = new ArrayList<>();
                for (selectable Selected :selected) {
                    lamp l=null;
                    if (Selected instanceof group)continue;
                    else l=(lamp)Selected;
                    boolean[] b = l.getBooleans();
                    if (b[0] || b[1] || b[2]) {
                        if (l.iterator<2) {
                            selectedLamps.add(l.getId());
                            n++;
                        } else  {
                            String[] s = l.getId().split(",");
                            selectedLamps.add(s[0]);
                            selectedLamps.add(s[1]);
                            Log.d("z",s[0]);
                            n+=2;
                        }
                        Log.d("lamp",l.iterator+"");
                    }
                }
                if (n == 0) break;
                new multithread().execute("send", "setgroup");
                new multithread().execute("send", String.valueOf(n));
                for (int i = 0; i < selectedLamps.size(); i++) {
                    new multithread().execute("send", "" + selectedLamps.get(i));

                }
                v.setOnClickListener(MainActivity.this);
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                alertDialog.setTitle("Новая группа");
                alertDialog.setMessage("Добавление новой группы ");
                final LinearLayout layout = new LinearLayout(MainActivity.context_g);
                layout.setOrientation(LinearLayout.VERTICAL);
                final EditText inputName = new EditText(MainActivity.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                inputName.setLayoutParams(lp);
                final EditText input_description = new EditText(MainActivity.this);
                input_description.setLayoutParams(lp);
                layout.addView(inputName);
                layout.addView(input_description);
                alertDialog.setView(layout).setIcon(R.drawable.lamp_on)
                        .setCancelable(false).setPositiveButton("Сохранить", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getter_from_app.writeToFile(inputName.getText().toString() + "-" + input_description.getText() + "-" + selectedLamps + "\n", "groups.txt", Context.MODE_APPEND);
                    }
                });
                alertDialog.show();

                break;
            case R.id.NEW_SCENARIO:
                inScenMode=true;
                new_scenario.setText("Сохранить");
                new_scenario.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        inScenMode=false;
                        int n = 0;
                        final ArrayList<String> lampInScenario = new ArrayList<>();
                        for (selectable Selected : selected) {
                            lamp l=null;
                            if (Selected instanceof group)continue;
                            else l=(lamp)Selected;
                            switch (l.iterator){
                                case 1:
                                    String br=""+l.getBright1();
                                    if (l.getBright1() < 10) br = "00" + l.getBright1();
                                    else if (l.getBright1() < 100)
                                        br = "0" + l.getBright1();
                                    lampInScenario.add(l.getId() + br);
                                    n++;
                                    break;
                                case 0:
                                    String s=""+l.getBright1();
                                    if (l.getBright1() < 10) s = "00" + l.getBright1();
                                    else if (l.getBright1() < 100)
                                        s = "0" + l.getBright1();
                                    lampInScenario.add(l.getId() + s);
                                    n++;

                                    break;
                                case 2:
                                    String ids[]=l.getId().split(",");
                                    String BR=""+l.getBright1();
                                    if (l.getBright1() < 10) BR = "00" + l.getBright1();
                                    else if (l.getBright1() < 100)
                                        BR = "0" + l.getBright1();
                                    lampInScenario.add(ids[0] + BR);
                                    String S=""+l.getBright1();
                                    if (l.getBright1() < 10) S = "00" + l.getBright1();
                                    else if (l.getBright1() < 100)
                                        S = "0" + l.getBright1();
                                    lampInScenario.add(ids[1] + S);
                                    n+=2;
                                    break;

                            }
                        }
                        if (n <= 0) return;
                        Log.d("Lamp",lampInScenario.toString());
                        new multithread().execute("send", "new");
                        new multithread().execute("send", String.valueOf(n));
                        for (int i = 0; i < lampInScenario.size(); i++) {
                            new multithread().execute("send",lampInScenario.get(i));
                        }

                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                        alertDialog.setTitle("Новый сценарий");
                        alertDialog.setMessage("Добавление нового сценария сценария ");
                        final LinearLayout layout = new LinearLayout(MainActivity.context_g);
                        layout.setOrientation(LinearLayout.VERTICAL);
                        final EditText inputName = new EditText(MainActivity.this);
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.MATCH_PARENT);
                        inputName.setLayoutParams(lp);
                        final EditText input_description = new EditText(MainActivity.this);
                        input_description.setLayoutParams(lp);
                        layout.addView(inputName);
                        layout.addView(input_description);
                        alertDialog.setView(layout).setIcon(R.drawable.lamp_on)
                                .setCancelable(false).setPositiveButton("Сохранить", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                getter_from_app.writeToFile(inputName.getText().toString() + "-" + input_description.getText() + "-" + lampInScenario + "\n", "scenarios.txt", Context.MODE_APPEND);
                                new_scenario.setOnClickListener(MainActivity.this);
                            }
                        });
                        alertDialog.show();
                        new_scenario.setText("ДОБАВИТЬ НОВЫЙ СЦЕНАРИЙ");
                        new_scenario.setOnClickListener(MainActivity.this);

                    }
                });
                break;
            case R.id.SETTINGS:
                final Intent intent_ = new Intent(this, Settimgs.class);
                startActivity(intent_);
                break;
            case R.id. GROUP:
                final Intent INTENT = new Intent(this, activity_group.class);
                startActivity(INTENT);
                break;

        }
    }
    public static void initDefaultG_S(){

    }

}