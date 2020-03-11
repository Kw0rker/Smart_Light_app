package com.example.student.smartlighttest1;


import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends FragmentActivity implements SeekBar.OnSeekBarChangeListener, Button.OnClickListener {
    //Const
    Timer statusUpdater;
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
    public static int numberOfLamps;
    LinearLayout buttonPanel;
    static int width,height;
    static boolean inScenMode = false;
    public static HashMap<String, Integer> lampList = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions,MODE_APPEND);
            Date currentTime = Calendar.getInstance().getTime();
            file.writeToSDFile(com.example.student.smartlighttest1.file.LOG_PATH, currentTime.toString(),false);
        }
        context_g = getApplicationContext();
        ExceptionCacher cacher=new ExceptionCacher();
        brighness = (TextView) findViewById(R.id.text);
        new_group = findViewById(R.id.NEW_GROUP);
        new_group.setOnClickListener(this);
        new_scenario = findViewById(R.id.NEW_SCENARIO);
        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        activity = this;
        buttonPanel=(LinearLayout)findViewById(R.id.buttonPanel);
        bar = findViewById(R.id.BRIGNESS);
        Button group=findViewById(R.id.GROUP);
        Display display = getWindowManager(). getDefaultDisplay();
        Point size = new Point();
        display. getSize(size);
        width = size. x;
        height = size. y;
        group.setOnClickListener(this);
        bar.setOnSeekBarChangeListener(this);
        final Switch swich=findViewById(R.id.switch1);
        swich.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                String br;
                if (b) br="254";

                else br="000";
                for (selectable s:selected){
                    if (s instanceof  lamp&&((lamp)s).iterator<2)
                        new multithread().execute("send",s.getId()+br);
                    else if (s instanceof group)
                        new multithread().execute("send",s.getId()+br);
                    else {
                        String []ss=s.getId().split(",");
                        new multithread().execute("send",ss[0]+br);
                        new multithread().execute("send",ss[1]+br);
                        lamp l=(lamp)s;

                    }
                    if (s instanceof lamp){
                        lamp l=(lamp)s;
                        switch (l.iterator){
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
            }
        });
        file.writeToFile("1,2 100#200 1\n" +
                "3,4 300#400 0\n" +
                "5,6 500#600 1\n" +
                "7,8 700#800 0\n" +
                "9,10 900#1000 1", file.PAIRS_PATH,MODE_PRIVATE);
        try {
            savedInstanceState.getBoolean("null");
        } catch (Exception e) {
            statusUpdater.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    synchronized (lampList){
                        lampList = udp.status();
                    }
                }
            },0, 15000);

        }
        Button scenario = findViewById(R.id.SCENARIO);
        int timer = 0;
        while (!multithread.isFinished()) {

        }
        builui();
        if (timer >= 30)
            Toast.makeText(this, "Ошибка!\n Проверьте соединение с сервером", Toast.LENGTH_LONG).show();
        scenario.setOnClickListener(this);
        Button settings = findViewById(R.id.SETTINGS);
        settings.setOnClickListener(this);
        new_group.setOnClickListener(this);
        new_scenario.setOnClickListener(this);;
        file.writeLog("OnCreate completed successfully ");
    }

    public static void delay(int milis) {
        try {
            Thread.sleep(milis);
        } catch (Exception e) {
            Log.e("timer", "error");
        }
    }


    private void builui() {
        Log.d("BuildUI","started");
        lamps = new lamp[lamp.numberOfLamps/2];
        buttons = new Button[lamp.numberOfLamps/2];
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
            final File file = new File(Environment.getExternalStorageDirectory()
                    .getAbsolutePath(), com.example.student.smartlighttest1.file.PAIRS_PATH);
            com.example.student.smartlighttest1.file.writeLog(file.getAbsolutePath()+"  opened");

            try {
                reader = new BufferedReader(new FileReader(file));
            }catch (FileNotFoundException e){
                com.example.student.smartlighttest1.file.writeLog(e.toString());
                Toast.makeText(this,"Отсутствует файл расположения ламп (" + com.example.student.smartlighttest1.file.PAIRS_PATH + ")",Toast.LENGTH_LONG).show();}
            for (i = 0; i <lamp.numberOfLamps/2; i++) {
                String str=null;
                buttons[i] = new Button(this);
                buttons[i].setId(i);
                try {
                    str=reader.readLine();
                    com.example.student.smartlighttest1.file.writeLog("Lamp created with params: "+str);
                } catch (NullPointerException e){
                    com.example.student.smartlighttest1.file.writeLog(e.toString());
                } catch (IOException e) {
                    com.example.student.smartlighttest1.file.writeLog(e.toString());
                }
                int id_ = buttons[i].getId();
                layout.addView(buttons[i], params);
                buttons[i] = findViewById(id_);
                buttons[i].setLayoutParams(new ConstraintLayout.LayoutParams(37, 17));
                lamps[i] = new lamp(buttons[i], str);

            }
    }

    public static void read(String name, int int_max) {
        BufferedReader br = null;
        try {
            // открываем поток для чтения
            final File file = new File(Environment.getExternalStorageDirectory()
                    .getAbsolutePath(), com.example.student.smartlighttest1.file.PAIRS_PATH);
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
        file.writeLog("activity main opened");
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
                        Lamp.setBright1(seekBar.getProgress());
                        break;
                }
            } else {
                if (!inScenMode){String[] s = Selected.getId().split(",");
                new multithread().execute("send", s[0] + brighnes);
                new multithread().execute("send", s[1] + brighnes);}
                Lamp.setBright1(seekBar.getProgress());
                Lamp.setBright1(seekBar.getProgress());
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
                        file.writeToFile(inputName.getText().toString() + "-" + input_description.getText() + "-" + selectedLamps + "\n", "groups.txt", Context.MODE_APPEND);
                    }
                });
                alertDialog.show();
                file.writeLog("New group created");

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
                                file.writeToFile(inputName.getText().toString() + "-" + input_description.getText() + "-" + lampInScenario + "\n", "scenarios.txt", Context.MODE_APPEND);
                                new_scenario.setOnClickListener(MainActivity.this);
                            }
                        });
                        alertDialog.show();
                        new_scenario.setText("ДОБАВИТЬ НОВЫЙ СЦЕНАРИЙ");
                        new_scenario.setOnClickListener(MainActivity.this);
                        file.writeLog("new script created");

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
        try {
            BufferedReader vr=new BufferedReader(new InputStreamReader(activity.openFileInput("groups.txt")));
            int line=0;
            String br;
            while ((br=vr.readLine())!=null)line++;
            if (line<=1){
                new multithread().execute("send","setgroup");
                new multithread().execute("send",String.valueOf(lamp.numberOfLamps));
                for (lamp l:lamps){
                    String []ids =l.getId().split(",");
                    new multithread().execute("send",ids[1]+254);
                    new multithread().execute("send",ids[0]+254);
                }
                file.writeToFile("Все светильники" + "-" + "" +
                        "Регулирует все светильники" + "-" + "Все" + "\n", "groups.txt", Context.MODE_APPEND);

                new multithread().execute("send","setgroup");
                new multithread().execute("send",String.valueOf(lamp.numberOfLamps/2));
                for (int i = 0; i < lamps.length/2; i++) {
                    String []ids =lamps[i].getId().split(",");
                    new multithread().execute("send",ids[1]);
                    new multithread().execute("send",ids[0]);

                }
                file.writeToFile("Половина свитиликов" + "-" + "" +
                        "Регулирует половину" + "-" +"..."+lamp.numberOfLamps/2 + "\n", "groups.txt", Context.MODE_APPEND);

                new multithread().execute("send","setgroup");
                new multithread().execute("send",String.valueOf(lamp.numberOfLamps/2));

                for (int i = lamps.length/2; i < lamps.length; i++) {
                    String []ids =lamps[i].getId().split(",");
                    new multithread().execute("send",ids[1]);
                    new multithread().execute("send",ids[0]);

                }
                file.writeToFile("Половина свитиликов" + "-" + "" +
                        "Регулирует половину" + "-" +lamp.numberOfLamps/2+"..."+lamp.numberOfLamps + "\n", "groups.txt", Context.MODE_APPEND);


            }
        } catch (FileNotFoundException e) {
            Log.d("file", "initDefaultG_S: groups.txr doesnt exists");
        } catch (IOException e) {
        }
    }
    public void udateById(String ...keys){
        for (int j = 0; j < keys.length; j++) {

        }
    }

}