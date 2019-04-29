package com.example.student.smartlighttest1;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
public class arduino {
    public int id;
    public boolean pressed =false;
    arduino(Button button, int id_){
        id=id_;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tupped())send(255);
                else send(0);
            }
        });
    }
    public boolean tupped(){
        pressed = !pressed;
        return pressed;
    }
    public void send(int brigh){
        new multithread().execute(id + "," + brigh);
    }
}
