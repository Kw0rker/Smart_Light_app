package com.example.student.smartlighttest1;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Space;
import android.widget.TableLayout;
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
        Log.d("Read", Arrays.toString(scen));
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
        button.setBackgroundResource(R.drawable.lamp_on);
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
                        Log.e("Write", e.getMessage());
                    }
                    String str = "";
                    try {
                        while ((str = br.readLine()) != null) {
                            if (str.equals(s2)) override_scan.add("DELETED");
                            else override_scan.add(str);

                        }
                    } catch (Exception e) {
                    }
                    getter_from_app.writeToFile("", "scenarios.txt", Context.MODE_PRIVATE);
                    for (String s : override_scan)
                        getter_from_app.writeToFile(s, "scenarios.txt", Context.MODE_APPEND);
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

    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            br = new BufferedReader(new InputStreamReader(MainActivity.activity.openFileInput("scenarios.txt")));
        } catch (Exception e) {
            Log.e("Write", e.getMessage());
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
            Log.d("view", "sortByHeight: "+v.getHeight());
            if (v.getTranslationY()>result.getTranslationY())result=v;
        }
        //Log.d("View",result.getMeasuredHeight()+"");
        for (View v:arc)v.setTranslationY(result.getTranslationY()+v.getMeasuredHeight());
    }


}