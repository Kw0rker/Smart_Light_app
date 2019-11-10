package com.example.student.smartlighttest1;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TableLayout;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
class scenar{
    boolean focused=false;
    scenar(final String s2, final int id, final Scenarios sc){
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
               if (Scenarios.from_setings)sc.confirmation_alert("Вы действительно собираетесь удалить данный сценарии?",sc.getApplicationContext(),view);
                if(Scenarios.confirm){
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
                    Scenarios.confirm =false;
                    Scenarios.from_setings=false;
                }

                return false;
            }
        });
    }
}
class group{
    boolean enable =false;
    static Scenarios sc;
 group(final String s,int ID,final Scenarios sc_)
 {
     Button button =new Button(MainActivity.context_g);
     this.sc=sc_;

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
             if (!enable){ if(!Scenarios.selected.contains(mes))Scenarios.selected.add(mes);v.setBackgroundColor(Color.LTGRAY);}
             else { if(Scenarios.selected.contains(mes))Scenarios.selected.remove(mes);v.setBackgroundColor(Color.WHITE);}

             enable=!enable;
         }
     });
     button.setOnLongClickListener(new View.OnLongClickListener() {
         @Override
         public boolean onLongClick(View v) {
             sc.confirmation_alert("Вы действительно собираетесь удалить данную группу?",sc.getApplicationContext(),v);
                 if(Scenarios.confirm){
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
                     finally {
                         try {
                             br.close();
                         } catch (IOException e) {
                             e.printStackTrace();
                         }
                     }
                     getter_from_app.writeToFile("","groups.txt", Context.MODE_PRIVATE);
                     for (String s:overrided_scen)getter_from_app.writeToFile(s,"groups.txt",Context.MODE_APPEND);
                     new multithread().execute("send","delete_g");
                     new multithread().execute("send",""+mes);Scenarios.from_setings=false;
                     Scenarios.confirm =false;

                 }


             return false;

         }
     });
 }
}

public class Scenarios extends AppCompatActivity {

   protected static LinearLayout layout1;
    protected static LinearLayout layout;
  private   static  BufferedReader br;
    static public ArrayList<String> selected;
    static boolean from_setings;
    static boolean confirm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lamps);
        layout1=(LinearLayout) findViewById(R.id.box);
        layout1.setOrientation(TableLayout.VERTICAL);
        layout=(LinearLayout) findViewById(R.id.groups);
        layout.setOrientation(TableLayout.VERTICAL);
        selected=new ArrayList<String>();
       SeekBar bar= new SeekBar(this);
       bar=(SeekBar)findViewById(R.id.group_brig);
       bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
           @Override
           public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

           }

           @Override
           public void onStartTrackingTouch(SeekBar seekBar) {

           }

           @Override
           public void onStopTrackingTouch(SeekBar seekBar) {
               String progress=String.valueOf(seekBar.getProgress());
               if (seekBar.getProgress()<10)progress="00"+seekBar.getProgress();
               else if (seekBar.getProgress()<100)progress="0"+seekBar.getProgress();
                for (String s:selected){new multithread().execute("send",s+progress);}
           }
       });
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
                if (!str.equals("DELETED"))new scenar(str,id++,this);
                else id++;
            }
        }catch (Exception e){}
        try{ br = new BufferedReader(new InputStreamReader(MainActivity.activity.openFileInput("groups.txt")));}
        catch (Exception e){Log.e("Write",e.getMessage());}


        // читаем содержимое
        int ID=0;
        try{
            while ((str = br.readLine()) != null) {
              if (!str.equals("DELETED"))new group(str,ID++,this);
              else ID++;
            }
        }catch (Exception e){}
    }
    public  void confirmation_alert(String measedge, Context context, final View v){
        from_setings=false;
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle("ВНИМАНИЕ!!!");
        alertDialog.setMessage(measedge);
        alertDialog.setPositiveButton("ДА!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Scenarios.confirm=true;
                from_setings=true;
                v.performClick();
                return;
            }
        });
        alertDialog.setNegativeButton("НЕТ!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Scenarios.confirm=false;
                from_setings=false;
                return;
            }
        });
        alertDialog.show();
        return;
    }





}