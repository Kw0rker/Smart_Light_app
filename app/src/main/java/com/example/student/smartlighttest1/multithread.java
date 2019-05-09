package com.example.student.smartlighttest1;
import android.os.AsyncTask;
import android.util.Log;

public class multithread extends AsyncTask<String, Void, Boolean> {
    static  boolean finished=false;
    @Override
    protected Boolean doInBackground(String... params) {
        switch (params[0]){
            case "send":udp.sender(params[1]);
                Log.d("udp","called");break;
            case "start":udp.start();break;
        }

        return true;
    }
    public static boolean isFinished(){return finished;}

}
