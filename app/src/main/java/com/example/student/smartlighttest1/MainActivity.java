package com.example.student.smartlighttest1;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

//import android.support.v7.app.AppCompatActivity;


public class MainActivity extends FragmentActivity implements SeekBar.OnSeekBarChangeListener,Button.OnClickListener{
    static ArrayList<String> selected = new ArrayList<String>();
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
    static int last_grup_n=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.context_g = getApplicationContext();

        brighness =(TextView) findViewById(R.id.text);
        new_group=(Button) findViewById(R.id.NEW_GROUP);
        new_group.setOnClickListener(this);
        new_scenario=(Button)findViewById(R.id.NEW_SCENARIO);
        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        activity = this;
        bar =(SeekBar)findViewById(R.id.BRIGNESS) ;
        bar.setOnSeekBarChangeListener(this);
        try {
            savedInstanceState.getBoolean("null");
        }
        catch(Exception e){
            udp.setup();
        //new multithread().execute("send", "refresh");
        new multithread().execute("send", "status");
        new multithread().execute("start");
            }
        Button scenario = (Button) findViewById(R.id.SCENARIO);
        int timer=0;
        while (!multithread.isFinished()) {

            /*timer++;
            delay(500);
            if (timer>=30)
            {
               udp.colvo=0;
               break;
            }*/
        }
        builui();
        if (timer>=30)Toast.makeText(this,"ОЩИБКА!!!\n Проверьте соединение с сервером",Toast.LENGTH_LONG).show();
        new Thread(new getter_from_app()).start();
        try {read("buttons.txt",udp.colvo); }
        catch (Exception e){Log.e("files","dont exist");}
       try {
           get_number_of_groups();
       }
       catch (Exception e){Log.e("file","doesnt exist");}

        scenario.setOnClickListener(this);
        Button settings = (Button)findViewById(R.id.SETTINGS);
        settings.setOnClickListener(this);
        new_group.setOnClickListener(this);
        new_scenario.setOnClickListener(this);


    }
    public static void delay(int milis)
    {
        try {
            Thread.sleep(milis);
        }
        catch (Exception e){Log.e("timer","error");}
    }


    public void builui() {
        lamps = new lamp[udp.colvo];
        buttons = new Button[udp.colvo];
        ConstraintLayout layout = findViewById(R.id.Main);
        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        for (i = 0; i < udp.colvo; i++) {
            buttons[i] = new Button(this);
            buttons[i].setId(i);
            int id_ = buttons[i].getId();
            layout.addView(buttons[i], params);
            buttons[i] = findViewById(id_);
            buttons[i].setLayoutParams(new ConstraintLayout.LayoutParams(67,67));

            lamps[i] = new lamp(buttons[i], udp.brignes[i], udp.id[i]);

        }
    }

    public static void read(String name,int int_max) {
        try {
            // открываем поток для чтения
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    activity.openFileInput(name)));
            String str = "";
            // читаем содержимое

            Log.e("Read", str);
            for (int x = 0; x <int_max && ((str = br.readLine()) != null); x++) {

                String[] x_y = str.split("/");
                buttons[x].setTranslationX(Integer.parseInt(x_y[0]));
                buttons[x].setTranslationY(Integer.parseInt(x_y[1]));

                Log.d("Buttons", "Setted");
            }


        } catch (IOException e) {
            Log.e("Read", e.getMessage());

        }

    }
    public static void get_number_of_groups() {
        BufferedReader br=null;
        String str;
        try{ br = new BufferedReader(new InputStreamReader(MainActivity.activity.openFileInput("groups.txt")));}
        catch (Exception e){Log.e("Write",e.getMessage());}
        try{
            while ((str = br.readLine()) != null) {
               last_grup_n++;
            }
        }catch (Exception e){}

    }
    static void vibrate(int miliis)
    {
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
        for (lamp l:lamps)l.setMODE("DEFAULT");
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, final int progress, boolean fromUser) {
        new Runnable() {
            public void run() {
                String brighnes=""+progress;
                if (progress<10)brighnes="00"+progress;
                else if (progress<100)brighnes="0"+progress;
                for (String s : selected) {
                    delay(75);
                    new multithread().execute("send", s+brighnes);
                }
                brighness.setText("Яркость :"+progress);
            }
        }.run();

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.SCENARIO:
                final Intent intent = new Intent(this, Scenarios.class);
                startActivity(intent);
                break;
            case R.id.NEW_GROUP:
                new_scenario.setVisibility(View.INVISIBLE);
                for(lamp l:lamps)l.setMODE("GROUP");
                new_group.setText("Сохранить");
                new_group.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int n=0;
                        new multithread().execute("send","setgroup");
                        new multithread().execute("send",""+(last_grup_n++));
                        for(lamp l:lamps)
                        {
                            if(l.in_mode_active){
                                n++;
                            }
                        }
                        new multithread().execute("send",""+n);
                        for(lamp l:lamps)
                        {
                            if(l.in_mode_active)new multithread().execute("send",""+l.getId());
                            l.in_mode_active=false;
                            l.setMODE("DEFAULT");
                            l.set_priv_img();
                        }
                        v.setOnClickListener(MainActivity.this);
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                        alertDialog.setTitle("Новая группа");
                        alertDialog.setMessage("Введите название группы ");

                        final EditText input = new EditText(MainActivity.this);
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.MATCH_PARENT);
                        input.setLayoutParams(lp);
                        alertDialog.setView(input).setIcon(R.drawable.lamp)
                                .setCancelable(false).setPositiveButton("Сохранить", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                getter_from_app.writeToFile(input.getText().toString()+"\n","groups.txt",Context.MODE_APPEND);
                                selected.clear();
                                new_group.setText("ДОБАВИТЬ НОВУЮ ГРУППУ");
                                new_group.setOnClickListener(MainActivity.this);
                                new_group.setVisibility(View.INVISIBLE);
                            }
                        });
                        alertDialog.show();
                    }
                });
                break;
            case R.id.NEW_SCENARIO:
                new_group.setVisibility(View.INVISIBLE);
                for(lamp l:lamps)l.setMODE("SCENARIO");
                new_scenario.setText("Сохранить");
                new_scenario.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int n=0;
                        for(lamp l:lamps)
                        {
                            if(l.in_new_scen_brigness>=0)n++;
                        }
                        new multithread().execute("send","new");
                        new multithread().execute("send",""+n);
                        for (lamp l:lamps)
                        {
                            if(l.in_new_scen_brigness>=0){
                                String  br=""+l.in_new_scen_brigness;
                                if (l.in_new_scen_brigness<10)br="00"+l.in_new_scen_brigness;
                                    else if (l.in_new_scen_brigness<100)br="0"+l.in_new_scen_brigness;


                                new multithread().execute("send",l.getId()+br);
                            }
                            l.in_new_scen_brigness=-2;
                            l.in_mode_active=false;
                            l.setMODE("DEFAULT");
                            l.set_priv_img();
                        }
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                        alertDialog.setTitle("Новый сценарий");
                        alertDialog.setMessage("Введите название сценария ");

                        final EditText input = new EditText(MainActivity.this);
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.MATCH_PARENT);
                        input.setLayoutParams(lp);
                        alertDialog.setView(input).setIcon(R.drawable.lamp)
                                .setCancelable(false).setPositiveButton("Сохранить", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                getter_from_app.writeToFile(input.getText().toString()+"\n","scenarios.txt",Context.MODE_APPEND);
                                new_scenario.setOnClickListener(MainActivity.this);
                            }
                        });
                        alertDialog.show();
                        new_scenario.setText("ДОБАВИТЬ НОВЫЙ СЦЕНАРИЙ");
                        new_scenario.setOnClickListener(MainActivity.this);
                        new_scenario.setVisibility(View.INVISIBLE);

                    }
                });
                break;
            case R.id.SETTINGS:
               final Intent intent_=new Intent(this,Settimgs.class);
                startActivity(intent_);
                break;
        }
    }

}