package com.example.student.smartlighttest1;

import android.view.View;
import android.widget.Button;

public class lamp {

    int button;
    int on_off;
    protected int id;
    static boolean selected=false;
    int brigh;
    Button bot;
    boolean x=true;
    lamp(Button bt,int brigh_,int id_){
        bot=bt;
        bot.setText(String.valueOf(brigh_));
        id=id_;
        brigh=brigh_;
        if (brigh>0){bt.setBackgroundResource(R.drawable.lamp_on);on_off=1;}
        else {bt.setBackgroundResource(R.drawable.lamp_off);on_off=0;}
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selected)
                {
                    if(x){
                    bot.setBackgroundResource(R.drawable.lamp_on_s);
                    MainActivity.selected.add(""+id);
                    x=false;}
                    else
                        {
                            bot.setBackgroundResource(R.drawable.lamp_on);
                            MainActivity.selected.remove(""+id);
                            x=true;
                        }
                }
              else{  tuped(bot);}
            }
        });
        bt.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                selected=!selected;
                return true;
            }
        });

    }

    public void tuped(Button bt) {
        if (on_off ==  1) {
            new multithread().execute("send",(getId()+","+ 0));
            udp.brignes[id]=0;
            on_off =  0;
            bt.setBackgroundResource(R.drawable.lamp_off);

        }
        else {on_off= 1;  new multithread().execute("send",(getId() +"," + 255));bt.setBackgroundResource(R.drawable.lamp_on);udp.brignes[id]=255;}
    }
    public String getId(){
        String result=""+id;
        return result;
    }
}

