package com.example.student.smartlighttest1;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class lamp {

    private int id;
    static boolean selected=false;
    private String ID;
    int brigh;
    public Button button;
    boolean is_active;
    boolean in_mode_active=false;
    int in_new_scen_brigness=-2;
    private String MODE="DEFAULT";
    boolean isTurned=false;
    int ID_;
    void set_priv_img(){
        if (isTurned)this.button.setBackgroundResource(R.drawable.lamp_on);
        else this.button.setBackgroundResource(R.drawable.lamp_off);
    }
    lamp(Button bt,int brigh_,int id_){
        button =bt;
        id=id_;
        brigh=brigh_;
        ID=""+id_;
        if (brigh>0){bt.setBackgroundResource(R.drawable.lamp_on);is_active=true;isTurned=true;}
        else {bt.setBackgroundResource(R.drawable.lamp_off);is_active=false;isTurned=false;}
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (MODE)
                {
                    case "DEFAULT":
                        Log.d("Button_mode","DEFAULT");
                        if(is_active)
                        {
                            new multithread().execute("send",(getId()+"000"));
                            isTurned=false;
                            button.setBackgroundResource(R.drawable.lamp_off);
                        }
                        else
                        {
                            new multithread().execute("send",(getId()+"255"));
                            isTurned=true;
                            button.setBackgroundResource(R.drawable.lamp_on);
                        }
                        break;
                    case "GROUP":
                        in_mode_active=!in_mode_active;
                        Log.d("Button_mode","GROUP");
                        if(in_mode_active)
                        {
                            if (!MainActivity.selected.contains(""+getId()));
                            MainActivity.selected.add(""+getId());
                            button.setBackgroundResource(R.drawable.lamp_on_s);

                        }
                        else
                        {
                            MainActivity.selected.remove(""+getId());
                            set_priv_img();
                        }

                        break;
                    case "SCENARIO":
                        in_mode_active=!in_mode_active;
                        Log.d("Button_mode","SCENARIO");
                        if(in_mode_active)
                        {
                            button.setBackgroundResource(R.drawable.lamp_on_s);
                            in_new_scen_brigness=MainActivity.bar.getProgress();

                        }
                        else
                        {
                            button.setBackgroundResource(R.drawable.lamp_off);
                            in_new_scen_brigness=-2;
                        }
                        break;

                }
                is_active=!is_active;
            }
        });
        bt.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                MainActivity.vibrate(500);
                selected=!selected;
                if (selected) {
                    for (lamp l : MainActivity.lamps) {l.setMODE("GROUP");}
                    MainActivity.new_scenario.setVisibility(View.VISIBLE);
                    MainActivity.new_group.setVisibility(View.VISIBLE);
                }

                else{
                    for(lamp l:MainActivity.lamps){l.setMODE("DEFAULT");MainActivity.selected.remove(""+l.getId());}
                    MainActivity.new_group.setVisibility(View.INVISIBLE);
                    MainActivity.new_scenario.setVisibility(View.INVISIBLE);
                }
                return true;
            }
        });

    }

    public String getId(){
        return ID;
    }
    public void setMODE(String Mode){this.MODE=Mode;}
}

