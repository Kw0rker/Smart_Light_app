package com.example.student.smartlighttest1;

import android.util.Log;

import java.util.ArrayList;

public class scenario {

   static void set_new(String brg){
        String [] brighness=brg.split(",");
       ArrayList<String> messadge=new ArrayList<>();
        int counter=0;
        int a=0;
        for(int lamp_n=0;lamp_n<udp.colvo;lamp_n++)
        {
           try {
               a=Integer.parseInt(brighness[lamp_n]);
           }catch (NumberFormatException e){Log.e("scenario",e.getMessage());}
           if(a>=0){
            Log.d("udp",""+a);
                counter++;
                messadge.add(lamp_n+","+a*2.55);}
        }
        new multithread().execute("send","new");
        new multithread().execute("send",(""+counter));
        for(String mes:messadge){
            new multithread().execute("send",mes);
        }

    }
}
