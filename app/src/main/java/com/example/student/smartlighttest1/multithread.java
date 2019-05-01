package com.example.student.smartlighttest1;
import android.os.AsyncTask;
import android.util.Log;

public class multithread extends AsyncTask<String, Void, Boolean> {
   final   udp server =new udp();
    static  boolean finished=false;
@Override
    protected Boolean doInBackground(String... params) {
    switch (params[0]){
        case "send":udp.sender(params[1]);
            Log.d("udp","called");break;
        case "start":server.start();break;
    }

 return true;
}
public static boolean isFinished(){return finished;}

}
