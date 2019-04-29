package com.example.student.smartlighttest1;
import android.os.AsyncTask;

public class multithread extends AsyncTask<String, Void, Boolean> {
   final   udp server =new udp();
    static  boolean finished=false;
@Override
    protected Boolean doInBackground(String... params) {
    switch (params[0]){
        case "send":udp.sender(params[1]);break;
        case "start":try{server.start();}catch (Exception e){}break;
    }

 return true;
}
public static boolean isFinished(){return finished;}

}
