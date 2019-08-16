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
    static MainActivity main=new MainActivity();
    boolean in_mode_active=false;
    int in_new_scen_brigness=-2;
    private String MODE="DEFAULT";
    lamp(Button bt,int brigh_,int id_){
        button =bt;
       // button.setText(String.valueOf(brigh_));
        id=id_;
        id_++;
        brigh=brigh_;
        if (id_<10)ID="000"+id_;
        else if (id_>=10&&id<100)ID="00"+id_;
        else if(id_>=100&&id<1000)ID="0"+id_;
        else ID=""+id_;
        brigh_=brigh_;
        if (brigh>0){bt.setBackgroundResource(R.drawable.lamp_on);is_active=true;}
        else {bt.setBackgroundResource(R.drawable.lamp_off);is_active=false;}
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                is_active=!is_active;
                switch (MODE)
                {
                    case "DEFAULT":
                        Log.d("Button_mode","DEFAULT");
                        if(is_active)
                        {
                            new multithread().execute("send",(getId()+","+ 0));
                            udp.brignes[id]=0;
                            button.setBackgroundResource(R.drawable.lamp_off);
                        }
                        else
                        {
                            new multithread().execute("send",(getId() +"," + 255));
                            button.setBackgroundResource(R.drawable.lamp_on);
                            udp.brignes[id]=255;
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
                            button.setBackgroundResource(R.drawable.lamp_off);
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

