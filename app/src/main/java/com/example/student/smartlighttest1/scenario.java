package com.example.student.smartlighttest1;

import android.util.Log;

import java.util.ArrayList;

public class scenario {
    private  String namel;
    int brignes[];

   static void set_new(String brg){
        String [] br=brg.split(",");
       ArrayList<String> mesage=new ArrayList<>();
        int counter=0;
        int a=0;
        for(int s=0;s<udp.colvo;s++)
        {
           try {
               a=Integer.parseInt(br[s]);
           }catch (NumberFormatException e){Log.e("scenario",e.getMessage());}
           if(a>=0){
            Log.d("udp",""+a);
                counter++;
                String brighness=""+(a*2.55);
                String n=""+s;
                mesage.add(n+","+brighness);}
        }
        new multithread().execute("send","new");
        new multithread().execute("send",(""+counter));
        for(int as=0;a<mesage.size();a++){
            new multithread().execute("send",mesage.get(as));
        }

    }
}
