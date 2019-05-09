package com.example.student.smartlighttest1;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;

public class Scenarios extends FragmentActivity {
    String Name;
    TableLayout layout1;
    static  BufferedReader br;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lamps);
        layout1= new TableLayout(this); layout1.findViewById(R.id.box);
        layout1.setOrientation(TableLayout.VERTICAL);

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
    }





}