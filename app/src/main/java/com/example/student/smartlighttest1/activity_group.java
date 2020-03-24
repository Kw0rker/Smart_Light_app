package com.example.student.smartlighttest1;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.LinkedList;

public class activity_group extends AppCompatActivity {
    private boolean enable = true;
    private Scenarios sc;
    private String ID;
    boolean from_setings;
    LinearLayout layout;
    Iterator<Pair<selectable, Integer>> iterator;
    private LinkedList<group> groups = new LinkedList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_activity_group);
        layout=findViewById(R.id.box2);
    }

    public int getGroupSize() {
        return groups.size();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onStart() {
        super.onStart();
        BufferedReader br = null;
        groups.clear();
        iterator=MainActivity.selected.iterator();
        while (iterator.hasNext()){
            try {
                Pair<selectable, Integer> selectedPair = iterator.next();
                selectable selected = selectedPair.first;
                if (selected instanceof group) MainActivity.selected.remove(selectedPair);
            }catch (ConcurrentModificationException e){iterator=MainActivity.selected.iterator();}


        }
        String str;
        try {
            br = new BufferedReader(new InputStreamReader(this.openFileInput("groups.txt")));
        } catch (Exception e) {
            Log.e("Write", e.getMessage());
        }


        // читаем содержимое
        int ID = 0;
        Gson gson = new Gson();
        try {
            while ((str = br.readLine()) != null) {
                if (!str.equals("DELETED")){
                    LinearLayout linearLayout=new LinearLayout(this);
                    linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    this.layout.addView(linearLayout);
                    group Group = gson.fromJson(str, group.class);
                    Group.add(this, linearLayout, this);
                    groups.add(Group);
                } else ID++;
            }
        } catch (Exception e) {
        }
    }

    public void confirmation_alert(String measedge, Context context, final View v) {
        from_setings = false;
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle("ВНИМАНИЕ!!!");
        alertDialog.setMessage(measedge);
        alertDialog.setPositiveButton("ДА!", (dialogInterface, i) -> {
            Scenarios.confirm = true;
            from_setings = true;
            v.performClick();
            return;
        });
        alertDialog.setNegativeButton("НЕТ!", (dialogInterface, i) -> {
            Scenarios.confirm = false;
            from_setings = false;
            return;
        });
        alertDialog.show();
        return;
    }
}

class group extends selectable {
    private String ID;
    LinkedList<Pair<String, Integer>> id;
    String name;
    String description;

    public group(LinkedList<Pair<String, Integer>> id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

        @Override
        public int hashCode() {
            return id.hashCode();
        }


    @RequiresApi(api = Build.VERSION_CODES.N)
    void add(activity_group sc, LinearLayout layout, Context c) {
        Button button;
            button = new Button(c);
        int groupId = sc.getGroupSize();
            button.setBackgroundResource(R.drawable.krestik);
        TextView ids = new TextView(c), textViewName = new TextView(c), des = new TextView(c);
        textViewName.setText(name);
        textViewName.setTextColor(Color.BLACK);
            des.setTextColor(Color.GRAY);
            ids.setTextColor(Color.LTGRAY);
        des.setText(description);
        StringBuffer buffer = new StringBuffer();
        id.forEach(p -> buffer.append(p.first).append(","));
        ids.setText(buffer.toString());
        textViewName.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, (MainActivity.height - 67) / 3, 1f));
            des.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, (MainActivity.height-67)/3, 1f));
            //button.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, Scenarios.height/3, 1f));
            ids.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, (MainActivity.height-67)/3, 1f));
        button.setId(groupId);
            final int id_ = button.getId();
            button.findViewById(id_);
        ID = "" + groupId;
        if (groupId < 10) ID = "00" + groupId;
        else if (groupId < 100) ID = "0" + groupId;
        layout.addView(textViewName);
            layout.addView(des);
            layout.addView(ids);
            button.setLayoutParams(new LinearLayout.LayoutParams(67,67));
            layout.addView(button);
        Pair<selectable, Integer> pair = new Pair<>(this, 0);
        button.setOnClickListener(v -> {
            if (!MainActivity.selected.contains(pair)) {
                MainActivity.selected.add(pair);
                button.setBackgroundResource(R.drawable.galochka);
            } else {
                MainActivity.selected.remove(pair);
                button.setBackgroundResource(R.drawable.krestik);
            }
            });
        button.setOnLongClickListener(view -> {
              /*  if (Scenarios.from_setings)
                    sc.confirmation_alert("Вы действительно собираетесь удалить данную группу?", c, view);
                if (Scenarios.confirm) {
                    BufferedReader br = null;
                    ArrayList<String> override_scan = new ArrayList<>();
                    try {
                        br = new BufferedReader(new InputStreamReader(c.openFileInput("groups.txt")));
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
                    file.writeToFile("", "groups.txt", Context.MODE_PRIVATE);
                    for (String s : override_scan)
                        file.writeToFile(s, "groups.txt", Context.MODE_APPEND);
                    new multithread().execute("send", "delete_g");
                    new multithread().execute("send", "" + groupId);
                    Scenarios.confirm = false;
                    Scenarios.from_setings = false;
                }*/

            return false;
            });
        }

        @Override
        public String getId() {
            return ID;
        }

    @Override
    public int compareTo(selectable selectable) {
        try {
            if (selectable instanceof lamp) {
                lamp l = (lamp) selectable;
                return Integer.parseInt(getId()) - Integer.parseInt(l.IDS[0]);

            } else {
                return Integer.parseInt(getId()) - Integer.parseInt(selectable.getId());
            }
        } catch (NumberFormatException e) {
            file.writeLog(e.toString());
            return 0;
        }
    }
    }

