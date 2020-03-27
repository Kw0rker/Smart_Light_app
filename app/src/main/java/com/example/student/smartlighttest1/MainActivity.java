package com.example.student.smartlighttest1;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;
import java.util.TreeSet;


public class MainActivity extends FragmentActivity implements SeekBar.OnSeekBarChangeListener, Button.OnClickListener {
    //Const
    Timer statusUpdater = new Timer();
    static TreeSet<Pair<selectable, Integer>> selected = new TreeSet<>((selectableIntegerPair, t1) -> selectableIntegerPair.first.compareTo(t1.first));
    static lamp[] lamps;
    static Vibrator v;
    TextView brighness;
    SeekBar bar;
    int i;
    Context context_g;
    MainActivity activity;
    Button new_scenario;
    Button new_group;
    public static int numberOfLamps;
    LinearLayout buttonPanel;
    static int width, height;
    static ConstraintLayout layout;
    static boolean inScenMode = false;
    public static volatile TreeMap<String, Integer> brightnessMap;
    public static volatile HashMap<String, lamp> lampMap = new HashMap<>();

    //@RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        file.context = this;
        setContentView(R.layout.activity_main);
        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, MODE_APPEND);
            Date currentTime = Calendar.getInstance().getTime();
            file.writeToSDFile(com.example.student.smartlighttest1.file.LOG_PATH, currentTime.toString(), false);
        }
        context_g = getApplicationContext();
        ExceptionCacher cacher = new ExceptionCacher();
        Thread.setDefaultUncaughtExceptionHandler(cacher);
        brighness = findViewById(R.id.text);
        new_group = findViewById(R.id.NEW_GROUP);
        new_group.setOnClickListener(this);
        new_scenario = findViewById(R.id.NEW_SCENARIO);
        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        activity = this;
        buttonPanel = findViewById(R.id.buttonPanel);
        bar = findViewById(R.id.BRIGNESS);
        Button group = findViewById(R.id.GROUP);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;
        group.setOnClickListener(this);
        bar.setOnSeekBarChangeListener(this);
        final Switch swich = findViewById(R.id.switch1);
        swich.setOnCheckedChangeListener((compoundButton, b) -> {
            String br;
            if (b) br = "254";
            else br = "000";
            for (Pair<selectable, Integer> selectedPair : selected) {
                selectable s = selectedPair.first;
                if (s instanceof lamp && ((lamp) s).iterator < 2) {
                    new multithread().execute("send", s.getId() + br);
                } else if (s instanceof group) {
                    new multithread().execute("send", s.getId() + br);
                    group g = (group) s;
                    int brightness = Integer.parseInt(br);
                    for (Pair<String, Integer> p : g.id) {
                        lamp l = MainActivity.lampMap.get(p.first);
                        if (l == null) return;
                        switch (p.second) {
                            case 0:
                                l.setBright1(brightness);
                                break;
                            case 1:
                                l.setBright2(brightness);
                                break;

                            case 2:
                                l.setBright2(brightness);
                                l.setBright1(brightness);
                                break;
                        }
                        l.changeBackground();
                    }
                } else {
                    String[] ss = s.getId().split(",");
                    new multithread().execute("send", ss[0] + br);
                    new multithread().execute("send", ss[1] + br);

                }
                if (s instanceof lamp) {
                    lamp l = (lamp) s;
                    switch (l.iterator) {
                        case 0:
                            l.setBright1(Integer.parseInt(br));
                            break;

                        case 1:
                            l.setBright2(Integer.parseInt(br));
                            break;
                        case 2:
                            l.setBright1(Integer.parseInt(br));
                            l.setBright2(Integer.parseInt(br));
                            break;
                    }
                    l.changeBackground();
                }
            }
        });
        file.writeToFile("1,2 100#200 1\n" +
                "3,4 300#400 0\n" +
                "5,6 500#600 1\n" +
                "7,8 700#800 0\n" +
                "9,10 900#1000 1", file.PAIRS_PATH, MODE_PRIVATE);
        try {
            savedInstanceState.getBoolean("null");
        } catch (Exception e) {
            final Context context = this;
            new multithread().execute("send", "status");
            new Thread(() -> brightnessMap = udp.status(context)).start();
            statusUpdater.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    if (brightnessMap == null) new multithread().execute("send", "status");

                }
            }, 0, 15000);

        }
        Button scenario = findViewById(R.id.SCENARIO);
        int timer = 0;
        while (brightnessMap == null) {
            Toast.makeText(this, "Подключение к серверу,\n Пожалуйста подождите", Toast.LENGTH_SHORT).show();
            timer++;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        builui();
        if (timer >= 60)
            Toast.makeText(this, "Ошибка!\n Проверьте соединение с сервером", Toast.LENGTH_LONG).show();
        scenario.setOnClickListener(this);
        Button settings = findViewById(R.id.SETTINGS);
        settings.setOnClickListener(this);
        new_group.setOnClickListener(this);
        new_scenario.setOnClickListener(this);
        file.writeLog("OnCreate completed successfully ");
    }

    public static void delay(int milis) {
        try {
            Thread.sleep(milis);
        } catch (Exception e) {
            Log.e("timer", "error");
        }
    }


    @SuppressLint("ClickableViewAccessibility")
    private void builui() {
        Log.d("BuildUI", "started");
        lamps = new lamp[numberOfLamps / 2];
        layout = findViewById(R.id.Main);
        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT);
        BufferedReader reader = null;
        layout.setOnTouchListener(new OnSwipeTouchListener() {
            public void onSwipeTop() {
                buttonPanel.setVisibility(View.VISIBLE);
            }

            public void onSwipeBottom() {
                buttonPanel.setVisibility(View.INVISIBLE);
            }

            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });
        final File file = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath(), com.example.student.smartlighttest1.file.PAIRS_PATH);
        com.example.student.smartlighttest1.file.writeLog(file.getAbsolutePath() + "  opened");

        try {
            reader = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            com.example.student.smartlighttest1.file.writeLog(e.toString());
            Toast.makeText(this, "Отсутствует файл расположения ламп (" + com.example.student.smartlighttest1.file.PAIRS_PATH + ")", Toast.LENGTH_LONG).show();
        }
        for (i = 0; i < numberOfLamps / 2; i++) {
            String str = null;
            Button button = new Button(this);
            button.setId(i);
            try {
                str = reader != null ? reader.readLine() : null;
                com.example.student.smartlighttest1.file.writeLog("Lamp created with params: " + str);
            } catch (NullPointerException e) {
                com.example.student.smartlighttest1.file.writeLog(e.toString());
            } catch (IOException e) {
                com.example.student.smartlighttest1.file.writeLog(e.toString());
            }
            int id_ = button.getId();
            layout.addView(button, params);
            button = findViewById(id_);
            button.setLayoutParams(new ConstraintLayout.LayoutParams(37, 17));
            lamps[i] = new lamp(button, str);
            Log.d("lamp", "Created");

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
        file.writeLog("activity main opened");
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onProgressChanged(SeekBar seekBar, final int progress, boolean fromUser) {

        brighness.setText("Яркость :" + progress);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    //@RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    synchronized public void onStopTrackingTouch(final SeekBar seekBar) {
        final int progress = seekBar.getProgress();
        String brighnes = "" + progress;
        if (progress < 10) brighnes = "00" + progress;
        else if (progress < 100) brighnes = "0" + progress;
        if (selected.size() <= 0) return;
        for (Pair<selectable, Integer> selected : selected) {
            delay(75);
            selectable Selected = selected.first;
            lamp Lamp;
            if (Selected instanceof group) {
                new multithread().execute("send", Selected.getId() + brighnes);
                group g = (group) Selected;
                for (Pair<String, Integer> p : g.id) {
                    lamp lamp = lampMap.get(p.first);
                    if (lamp == null) continue;
                    switch (p.second) {
                        case 0:
                            lamp.setBright1(progress);
                            break;
                        case 1:
                            lamp.setBright2(progress);
                            break;

                        case 2:
                            lamp.setBright1(progress);
                            lamp.setBright2(progress);
                            break;
                    }
                    lamp.changeBackground();
                }

            } else if (Selected instanceof lamp) {
                Lamp = (lamp) Selected;
                if (Lamp.iterator < 2) {
                    if (!inScenMode) {
                        new multithread().execute("send", Selected.getId() + brighnes);
                    }
                    switch (Lamp.iterator) {
                        case 1:
                            Lamp.setBright2(seekBar.getProgress());
                            break;
                        case 0:
                            Lamp.setBright1(seekBar.getProgress());
                            break;
                    }
                } else {
                    if (!inScenMode) {
                        String[] s = Selected.getId().split(",");
                        new multithread().execute("send", s[0] + brighnes);
                        new multithread().execute("send", s[1] + brighnes);
                    }
                    Lamp.setBright1(seekBar.getProgress());
                    Lamp.setBright2(seekBar.getProgress());
                }
                Lamp.changeBackground();
            }
        }


    }

    //@RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.SCENARIO:
                final Intent intent = new Intent(this, Scenarios.class);
                startActivity(intent);
                break;
            case R.id.NEW_GROUP:
                int n = 0;
                ArrayList<String> selectedLamps = new ArrayList<>();
                LinkedList<Pair<String, Integer>> list = new LinkedList<>();
                for (Pair<selectable, Integer> selectedPair : selected) {
                    selectable Selected = selectedPair.first;
                    lamp l;
                    if (Selected instanceof group) continue;
                    else l = (lamp) Selected;
                    boolean[] b = l.getBooleans();
                    if (b[0] || b[1] || b[2]) {
                        if (l.iterator < 2) {
                            if (!l.getId().contains(",")) selectedLamps.add(l.getId());
                            list.add(new Pair<>(l.getId(), l.iterator));
                            n++;
                        } else {
                            String[] s = l.getId().split(",");
                            selectedLamps.add(s[0]);
                            selectedLamps.add(s[1]);
                            list.add(new Pair<>(s[0], l.iterator));
                            Log.d("z", s[0]);
                            n += 2;
                        }
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
                final LinearLayout layout = new LinearLayout(this);
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
                Gson gson = new Gson();
                alertDialog.setView(layout).setIcon(R.drawable.lamp_on)
                        .setCancelable(false).setPositiveButton("Сохранить", (dialog, which) ->
                        file.writeToFile(gson.toJson(new group(list, inputName.getText().toString(), input_description.getText().toString()), group.class) + "\n", "groups.txt", Context.MODE_APPEND));
                alertDialog.show();
                file.writeLog("New group created");

                break;
            case R.id.NEW_SCENARIO:
                inScenMode = true;
                new_scenario.setText("Сохранить");
                Context context = this;
                new_scenario.setOnClickListener(v1 -> {
                    inScenMode = false;
                    LinkedList<Triple<String, Short, Integer>> lampParams = new LinkedList<>();
                    int n1 = 0, id = 0;
                    final LinkedList<Pair<String, String>> lampInScenario = new LinkedList<>();
                    for (Pair<selectable, Integer> selectedPair : selected) {
                        selectable Selected = selectedPair.first;
                        lamp l;
                        if (Selected instanceof group) continue; //we dont use groups in script
                        else l = (lamp) Selected;
                        switch (l.iterator) {
                            case 1:
                                String br = "" + l.getBright1();
                                if (l.getBright1() < 10) br = "00" + l.getBright1();
                                else if (l.getBright1() < 100)
                                    br = "0" + l.getBright1();
                                lampInScenario.add(new Pair<>(l.getId(), br));
                                lampParams.add(new Triple<>(l.getId(), Short.parseShort(br), l.iterator));
                                n1++;
                                break;
                            case 0:
                                String s = "" + l.getBright1();
                                if (l.getBright1() < 10) s = "00" + l.getBright1();
                                else if (l.getBright1() < 100)
                                    s = "0" + l.getBright1();
                                lampInScenario.add((new Pair<>(l.getId(), s)));
                                lampParams.add(new Triple<>(l.getId(), Short.parseShort(s), l.iterator));
                                n1++;

                                break;
                            case 2:
                                String[] ids = l.getId().split(",");
                                String BR = "" + l.getBright1();
                                if (l.getBright1() < 10) BR = "00" + l.getBright1();
                                else if (l.getBright1() < 100)
                                    BR = "0" + l.getBright1();
                                String S = "" + l.getBright1();
                                if (l.getBright1() < 10) S = "00" + l.getBright1();
                                else if (l.getBright1() < 100)
                                    S = "0" + l.getBright1();
                                lampInScenario.add(new Pair<>(ids[1], S));
                                lampInScenario.add(new Pair<>(ids[0], BR));
                                lampParams.add(new Triple<>(ids[1], Short.parseShort(S), l.iterator));
                                lampParams.add(new Triple<>(ids[0], Short.parseShort(BR), l.iterator));
                                n1 += 2;
                                break;

                        }
                    }
                    if (n1 <= 0) return;
                    Log.d("Lamp", lampInScenario.toString());
                    new multithread().execute("send", "new");
                    new multithread().execute("send", String.valueOf(n1));
                    for (int i = 0; i < lampInScenario.size(); i++) {
                        new multithread().execute("send", lampInScenario.get(i).first + lampInScenario.get(i).second);
                    }

                    AlertDialog.Builder alertDialog1 = new AlertDialog.Builder(MainActivity.this);
                    alertDialog1.setTitle("Новый сценарий");
                    alertDialog1.setMessage("Добавление нового сценария сценария ");
                    final LinearLayout layout1 = new LinearLayout(context);
                    layout1.setOrientation(LinearLayout.VERTICAL);
                    final EditText inputName1 = new EditText(MainActivity.this);
                    LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT);
                    inputName1.setLayoutParams(lp1);
                    final EditText input_description1 = new EditText(MainActivity.this);
                    input_description1.setLayoutParams(lp1);
                    layout1.addView(inputName1);
                    layout1.addView(input_description1);
                    Gson gson1 = new Gson();
                    /*
                    int id=0;
                    for (Pair<String,String> pair:lampInScenario){
                        idAndBrightness[id++]=new Triple<String, Short, Integer>(pair.first,Short.parseShort(pair.second),MainActivity.lampMap.get(pair.first).iterator);
                    }*/

                    alertDialog1.setView(layout1).setIcon(R.drawable.lamp_on)
                            .setCancelable(false).setPositiveButton("Сохранить", (dialog, which) -> {
                        file.writeToFile(gson1.toJson(new scenar(inputName1.getText().toString(), input_description1.getText().toString(), lampParams), scenar.class), "scenarios.txt", Context.MODE_APPEND);
                        new_scenario.setOnClickListener(MainActivity.this);
                    });
                    alertDialog1.show();
                    new_scenario.setText("ДОБАВИТЬ НОВЫЙ СЦЕНАРИЙ");
                    new_scenario.setOnClickListener(MainActivity.this);
                    file.writeLog("new script created");

                });
                break;
            case R.id.SETTINGS:
                final Intent intent_ = new Intent(this, Settimgs.class);
                startActivity(intent_);
                break;
            case R.id.GROUP:
                final Intent INTENT = new Intent(this, activity_group.class);
                startActivity(INTENT);
                break;

        }
    }

}