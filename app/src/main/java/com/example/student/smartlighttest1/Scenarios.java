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
import android.widget.SeekBar;
import android.widget.TableLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

class scenar {
    boolean focused = false;

    scenar(final String s2, final int id, final Scenarios sc, LinearLayout names, LinearLayout descr, LinearLayout ids_l, LinearLayout button_l,final Context c) {
        Button button = new Button(c);
        String[] scen = s2.split("-");
        Log.d("Read", Arrays.toString(scen));
        final String mes = "*" + id;
        TextView ids=new TextView(c),name=new TextView(c),des=new TextView(c);
        name.setText(scen[0]);
        des.setText(scen[1]);
        ids.setText(scen[2]);
        button.setId(id);
        final int id_ = button.getId();
        button.findViewById(id_);
        names.addView(name);
        descr.addView(des);
        ids_l.addView(ids);
        button_l.addView(button);
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
                    sc.confirmation_alert("Вы действительно собираетесь удалить данный сценарии?", c, view);
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
    LinearLayout names,descr,ids,button_l,gNames,gDescrp,gIds,gButtonL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scenarios);
        layout1 = findViewById(R.id.box);
        selected = new ArrayList<String>();
        names=findViewById(R.id.sName);
        descr=findViewById(R.id.sDescription);
        ids=findViewById(R.id.sIds);
        button_l=findViewById(R.id.sButton);
        gNames=findViewById(R.id.groupName);
        gDescrp=findViewById(R.id.groupDescription);
        gIds=findViewById(R.id.groupIds);
        gButtonL=findViewById(R.id.groupButton);
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
                    new scenar(str, id++, this,names,descr,ids,button_l,this);
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


}