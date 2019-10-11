package com.example.student.smartlighttest1;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;

public class Scenarios extends FragmentActivity {
    String Name;
    LinearLayout layout1;
    LinearLayout layout;
    static  BufferedReader br;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lamps);
        layout1=(LinearLayout) findViewById(R.id.box);
        layout1.setOrientation(TableLayout.VERTICAL);
        layout=(LinearLayout) findViewById(R.id.groups);
        layout.setOrientation(TableLayout.VERTICAL);
    }


    @Override
    protected void onStart() {
        super.onStart();

        try{ br = new BufferedReader(new InputStreamReader(MainActivity.activity.openFileInput("scenarios.txt")));}
        catch (Exception e){Log.e("Write",e.getMessage());}
        String str = "";

        // читаем содержимое
        int id=0;
        try{
            while ((str = br.readLine()) != null) {
                Button button =new Button(this);
                String[] scen=str.split("-");
                Log.d("Read", Arrays.toString(scen));
                TableLayout.LayoutParams params=new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.MATCH_PARENT);
                final String mes=""+id;
                button.setId(id++);
                final int id_=button.getId();
                button.findViewById(id_);
                layout1.addView(button);
                button.setText(scen[0]);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new multithread().execute("send",mes);
                    }
                });
            }
        }catch (Exception e){}
        try{ br = new BufferedReader(new InputStreamReader(MainActivity.activity.openFileInput("groups.txt")));}
        catch (Exception e){Log.e("Write",e.getMessage());}


        // читаем содержимое
        int ID=0;
        try{
            while ((str = br.readLine()) != null) {
                Button button =new Button(this);
                TableLayout.LayoutParams params=new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.MATCH_PARENT);
                final String mes;
                if (ID<=9)mes="*00"+ID;
                    else if (ID<=99)mes="*0"+ID;
                        else mes="*"+ID;
                button.setId(ID++);
                final int id_=button.getId();
                button.findViewById(id_);
                MainActivity.selected.remove(mes);
                layout.addView(button);
                button.setText(str);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       if(!MainActivity.selected.contains(mes))MainActivity.selected.add(mes);
                       v.setBackgroundColor(Color.BLUE);
                    }
                });
                button.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        MainActivity.vibrate(500);
                        v.setBackgroundColor(Color.TRANSPARENT);
                        MainActivity.selected.remove(mes);
                        return false;
                    }
                });
            }
        }catch (Exception e){}
    }





}