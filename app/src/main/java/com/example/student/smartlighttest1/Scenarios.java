package com.example.student.smartlighttest1;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
class scenar{
    boolean focused=false;
    scenar(final String s2, final int id,final Context context){
        Button button =new Button(MainActivity.context_g);
        String[] scen=s2.split("-");
        Log.d("Read", Arrays.toString(scen));
        TableLayout.LayoutParams params=new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.MATCH_PARENT);
        final String mes="*"+id;
        button.setId(id);
        final int id_=button.getId();
        button.findViewById(id_);
        Scenarios.layout1.addView(button);
        button.setText(scen[0]);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new multithread().execute("send",mes);
            }
        });
        button.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
               if (Settimgs.from_setings)Settimgs.confirmation_alert("Вы действительно собираетесь удалить данный сценарии?",context,view);
                if(Settimgs.confirm){
                    BufferedReader br=null;
                    ArrayList<String> overrided_scen=new ArrayList<>();
                    try{ br = new BufferedReader(new InputStreamReader(MainActivity.activity.openFileInput("scenarios.txt")));}
                    catch (Exception e){Log.e("Write",e.getMessage());}
                    String str = "";
                    try{
                        while ((str = br.readLine()) != null) {
                            if (str.equals(s2))overrided_scen.add("DELETED");
                            else overrided_scen.add(str);

                        }
                    }catch (Exception e){}
                    getter_from_app.writeToFile("","scenarios.txt", Context.MODE_PRIVATE);
                    for (String s:overrided_scen)getter_from_app.writeToFile(s,"scenarios.txt",Context.MODE_APPEND);
                    new multithread().execute("send","delete_s");
                    new multithread().execute("send",""+id);
                    Settimgs.confirm =false;
                    Settimgs.from_setings=false;
                }

                return false;
            }
        });
    }
}
class group{
    boolean enable =false;
 group(final String s,int ID,final Context context)
 {
     Button button =new Button(MainActivity.context_g);
     TableLayout.LayoutParams params=new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.MATCH_PARENT);
     final String mes;
     if (ID<=9)mes="00"+ID;
     else if (ID<=99)mes="0"+ID;
     else mes=""+ID;
     button.setId(ID);
     final int id_=button.getId();
     button.findViewById(id_);
     MainActivity.selected.remove(mes);
     Scenarios.layout.addView(button);
     button.setText(s);
     button.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             if (!enable){ if(!MainActivity.selected.contains(mes))MainActivity.selected.add(mes);v.setBackgroundColor(Color.LTGRAY);}
             else { if(MainActivity.selected.contains(mes))MainActivity.selected.remove(mes);v.setBackgroundColor(Color.WHITE);}

             enable=!enable;
         }
     });
     button.setOnLongClickListener(new View.OnLongClickListener() {
         @Override
         public boolean onLongClick(View v) {
             Settimgs.confirmation_alert("Вы действительно собираетесь удалить данную группу?",context,v);
                 if(Settimgs.confirm){
                     BufferedReader br=null;
                     ArrayList<String> overrided_scen=new ArrayList<>();
                     try{ br = new BufferedReader(new InputStreamReader(MainActivity.activity.openFileInput("groups.txt")));}
                     catch (Exception e){Log.e("Write",e.getMessage());}
                     String str = "";
                     try{
                         while ((str = br.readLine()) != null) {
                             if (str.equals(s))overrided_scen.add("DELETED");
                             else overrided_scen.add(str);

                         }
                     }catch (Exception e){}
                     getter_from_app.writeToFile("","groups.txt", Context.MODE_PRIVATE);
                     for (String s:overrided_scen)getter_from_app.writeToFile(s,"groups.txt",Context.MODE_APPEND);
                     new multithread().execute("send","delete_g");
                     new multithread().execute("send",""+mes);Settimgs.from_setings=false;
                     Settimgs.confirm =false;

                 }


             return false;

         }
     });
 }
}

public class Scenarios extends AppCompatActivity {

   static LinearLayout layout1;
    static LinearLayout layout;
    static  BufferedReader br;
    private  Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lamps);
        layout1=(LinearLayout) findViewById(R.id.box);
        layout1.setOrientation(TableLayout.VERTICAL);
        layout=(LinearLayout) findViewById(R.id.groups);
        layout.setOrientation(TableLayout.VERTICAL);
        context=this.getApplicationContext();
    }

    @Override
    protected void onStart() {
        super.onStart();
        context=this.getApplicationContext();
        try{ br = new BufferedReader(new InputStreamReader(MainActivity.activity.openFileInput("scenarios.txt")));}
        catch (Exception e){Log.e("Write",e.getMessage());}
        String str = "";

        // читаем содержимое
        int id=0;
        try{
            while ((str = br.readLine()) != null) {
                if (!str.equals("DELETED"))new scenar(str,id++,getApplicationContext());
                else id++;
            }
        }catch (Exception e){}
        try{ br = new BufferedReader(new InputStreamReader(MainActivity.activity.openFileInput("groups.txt")));}
        catch (Exception e){Log.e("Write",e.getMessage());}


        // читаем содержимое
        int ID=0;
        try{
            while ((str = br.readLine()) != null) {
              if (!str.equals("DELETED"))new group(str,ID++,getApplicationContext());
              else ID++;
            }
        }catch (Exception e){}
    }





}