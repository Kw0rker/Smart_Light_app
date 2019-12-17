package com.example.student.smartlighttest1;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Iterator;

public class activity_group extends AppCompatActivity {
    private boolean enable = true;
    private Scenarios sc;
    private String ID;
    boolean from_setings;
    LinearLayout layout;
    Iterator<selectable> iterator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_activity_group);
        layout=findViewById(R.id.box2);
    }

    @Override
    protected void onStart() {
        super.onStart();
        BufferedReader br = null;
        iterator=MainActivity.selected.iterator();
        while (iterator.hasNext()){
            try {
                selectable selected=iterator.next();
                if (selected instanceof group)MainActivity.selected.remove(selected);
            }catch (ConcurrentModificationException e){iterator=MainActivity.selected.iterator();}


        }
        String str;
        try {
            br = new BufferedReader(new InputStreamReader(MainActivity.activity.openFileInput("groups.txt")));
        } catch (Exception e) {
            Log.e("Write", e.getMessage());
        }


        // читаем содержимое
        int ID = 0;
        try {
            while ((str = br.readLine()) != null) {
                if (!str.equals("DELETED")){
                    LinearLayout linearLayout=new LinearLayout(this);
                    linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    this.layout.addView(linearLayout);
                    new group(str, ID++, this, linearLayout,this);}
                else ID++;
            }
        } catch (Exception e) {
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
    class group implements selectable {
    String ID;
    Button button;
    group Group;

        @Override
        public int hashCode() {
            return ID.hashCode();
        }

        group(final String s2, final int id, final activity_group sc,LinearLayout layout, final Context c)  {
            button = new Button(c);
            button.setBackgroundResource(R.drawable.krestik);
            String[] scen = s2.split("-");
            Log.d("Read", Arrays.toString(scen));

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
            ID=""+id;
            if (id<10)ID="00"+id;
            else if (id<100)ID="0"+id;
            layout.addView(name);
            layout.addView(des);
            layout.addView(ids);
            button.setLayoutParams(new LinearLayout.LayoutParams(67,67));
            layout.addView(button);
            Group=this;
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!MainActivity.selected.contains(Group)){MainActivity.selected.add(Group);button.setBackgroundResource(R.drawable.galochka);}
                    else{ MainActivity.selected.remove(Group);button.setBackgroundResource(R.drawable.krestik);}
                }
            });
            button.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (Scenarios.from_setings)
                        sc.confirmation_alert("Вы действительно собираетесь удалить данную группу?", c, view);
                    if (Scenarios.confirm) {
                        BufferedReader br = null;
                        ArrayList<String> override_scan = new ArrayList<>();
                        try {
                            br = new BufferedReader(new InputStreamReader(MainActivity.activity.openFileInput("groups.txt")));
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
                        getter_from_app.writeToFile("", "groups.txt", Context.MODE_PRIVATE);
                        for (String s : override_scan)
                            getter_from_app.writeToFile(s, "groups.txt", Context.MODE_APPEND);
                        new multithread().execute("send", "delete_g");
                        new multithread().execute("send", "" + id);
                        Scenarios.confirm = false;
                        Scenarios.from_setings = false;
                    }

                    return false;
                }
            });
        }
        @Override
        public String getId() {
            return ID;
        }

    }

