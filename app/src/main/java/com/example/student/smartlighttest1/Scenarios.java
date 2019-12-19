package com.example.student.smartlighttest1;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

class scenar {
    boolean focused = false;
    scenar(final String s2, final int id, LinearLayout layout,final Context c) {
        Button button = new Button(c);
        String[] scen = s2.split("-");

        final String mes = "*" + id;
        TextView ids=new TextView(c),name=new TextView(c),des=new TextView(c);
        name.setText(scen[0]);
        name.setTextColor(Color.BLACK);
        des.setTextColor(Color.GRAY);
        ids.setTextColor(Color.LTGRAY);
        des.setText(scen[1]);
        ids.setText(scen[2]);
        name.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, (MainActivity.height-67)/3,1f));
        des.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, (MainActivity.height-67)/3, 1f));
        //button.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, Scenarios.height/3, 1f));
        ids.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, (MainActivity.height-67)/3, 1f));
        button.setId(id);
        final int id_ = button.getId();
        button.findViewById(id_);
        layout.addView(name);
        layout.addView(des);
        layout.addView(ids);
        button.setLayoutParams(new LinearLayout.LayoutParams(67,67));
        layout.addView(button);
        button.setBackgroundResource(R.drawable.scenario);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new multithread().execute("send", mes);
            }
        });
        button.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (Scenarios.from_setings)
                    //sc.confirmation_alert("Вы действительно собираетесь удалить данный сценарии?", c, view);
                if (Scenarios.confirm) {
                    BufferedReader br = null;
                    ArrayList<String> override_scan = new ArrayList<>();
                    try {
                        br = new BufferedReader(new InputStreamReader(MainActivity.activity.openFileInput("scenarios.txt")));
                    } catch (Exception e) {
                        file.writeToSDFile("logs.txt",e.getLocalizedMessage(),true);
                    }
                    String str = "";
                    try {
                        while ((str = br.readLine()) != null) {
                            if (str.equals(s2)) override_scan.add("DELETED");
                            else override_scan.add(str);

                        }
                    } catch (Exception e) {
                    }
                    file.writeToFile("", "scenarios.txt", Context.MODE_PRIVATE);
                    for (String s : override_scan)
                        file.writeToFile(s, "scenarios.txt", Context.MODE_APPEND);
                    new multithread().execute("send", "delete_s");
                    new multithread().execute("send", "" + id);
                    Scenarios.confirm = false;
                    Scenarios.from_setings = false;
                }

                return false;
            }
        });
    }
}


public class Scenarios extends AppCompatActivity {

    public static LinearLayout layout1;

    private static BufferedReader br;
    static public ArrayList<String> selected;
    static boolean from_setings;
    static boolean confirm;
    LinearLayout scroll;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scenarios);
        layout1 = findViewById(R.id.box);
        selected = new ArrayList<String>();
        scroll=findViewById(R.id.box);
        initDefault();

    }

    private void initDefault() {
        TextView ids=new TextView(this),name=new TextView(this),des=new TextView(this);
        name.setText("Включить все");
        Button button=new Button(this);
        name.setTextColor(Color.BLACK);
        des.setTextColor(Color.GRAY);
        ids.setTextColor(Color.LTGRAY);
        des.setText("Включает все светильники");
        ids.setText("Все");
        name.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, (MainActivity.height-67)/3,1f));
        des.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, (MainActivity.height-67)/3, 1f));
        //button.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, Scenarios.height/3, 1f));
        ids.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, (MainActivity.height-67)/3, 1f));
        button.setId(lamp.random.nextInt());
        final int id_ = button.getId();
        button.findViewById(id_);
        LinearLayout layout=new LinearLayout(this);
        //layout.setWeightSum(4);
        layout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        this.scroll.addView(layout);
        layout.addView(name);
        layout.addView(des);
        layout.addView(ids);
        button.setLayoutParams(new LinearLayout.LayoutParams(67,67));
        layout.addView(button);
        button.setBackgroundResource(R.drawable.scenario);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new multithread().execute("send", "on");
                new multithread().execute("send", "all");
            }
        });
        Button button1=new Button(this);
        TextView ids1=new TextView(this),name1=new TextView(this),des1=new TextView(this);
        name1.setText("Выключить все");
        name1.setTextColor(Color.BLACK);
        des1.setTextColor(Color.GRAY);
        ids1.setTextColor(Color.LTGRAY);
        des1.setText("Выключает все светильники");
        ids1.setText("Все");
        name1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, (MainActivity.height-67)/3,1f));
        des1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, (MainActivity.height-67)/3, 1f));
        //button.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, Scenarios.height/3, 1f));
        ids1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, (MainActivity.height-67)/3, 1f));
        button1.setId(lamp.random.nextInt());
        final int id = button1.getId();
        button1.findViewById(id);
        LinearLayout layout1=new LinearLayout(this);
        //layout.setWeightSum(4);
        layout1.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        this.scroll.addView(layout1);
        layout1.addView(name1);
        layout1.addView(des1);
        layout1.addView(ids1);
        button1.setLayoutParams(new LinearLayout.LayoutParams(67,67));
        layout1.addView(button1);
        button1.setBackgroundResource(R.drawable.scenario);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new multithread().execute("send", "off");
                new multithread().execute("send", "all");
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            br = new BufferedReader(new InputStreamReader(MainActivity.activity.openFileInput("scenarios.txt")));
        } catch (Exception e) {
            file.writeToSDFile("logs.txt",e.getLocalizedMessage(),true);
        }
        String str = "";

        // читаем содержимое
        int id = 0;
        try {
            while ((str = br.readLine()) != null) {
                if (!str.equals("DELETED")) {
                    LinearLayout layout=new LinearLayout(this);
                   //layout.setWeightSum(4);
                    layout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    this.scroll.addView(layout);
                    new scenar(str, id++,layout,this);
                } else id++;
            }
        } catch (Exception e) {
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void confirmation_alert(String measedge, Context context, final View v) {
        from_setings = false;
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle("ВНИМАНИЕ!!!");
        alertDialog.setMessage(measedge);
        alertDialog.setPositiveButton("ДА!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Scenarios.confirm = true;
                from_setings = true;
                v.performClick();
                return;
            }
        });
        alertDialog.setNegativeButton("НЕТ!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Scenarios.confirm = false;
                from_setings = false;
                return;
            }
        });
        alertDialog.show();
        return;
    }
    public static void sortByHeight(View...arc){
        View result=arc[0];
        for(View v:arc){
            if (v.getTranslationY()>result.getTranslationY())result=v;
        }
        for (View v:arc)v.setTranslationY(result.getTranslationY()+v.getMeasuredHeight());
    }


}