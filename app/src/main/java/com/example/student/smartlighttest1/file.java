package com.example.student.smartlighttest1;


import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class file  {
    @SuppressLint("StaticFieldLeak")
    static Context context;

    public static final String LOG_PATH = "logs.txt", PAIRS_PATH = "pairs.txt";

    static void writeToFile(String[] data, String file_name, int mode) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(file_name, mode));
            for (String ms : data) {
                if (ms != null) outputStreamWriter.write(ms);
            }
            outputStreamWriter.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }

    }

    static void writeToFile(String data, String file_name, int mode) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(file_name, mode));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }


    }

    static boolean isExist(String sx) {
        boolean result = false;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(context.openFileInput("scenarios.txt")));
        } catch (Exception e) {
            Log.e("Write", e.getMessage());
        }
        String str = "";

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("scenarios.txt"));
            while ((str = br.readLine()) != null) {
                if (str.contains(sx)) return true;
                writer.write(str + System.getProperty("line.separator"));
            }
        } catch (Exception e) {
            Log.e("Read", e.getMessage());
        }

        return result;
    }
    public static void writeToSDFile(String fileName,String data,boolean append){

        // Find the root of the external storage.
        // See http://developer.android.com/guide/topics/data/data-  storage.html#filesExternal

        File root = android.os.Environment.getExternalStorageDirectory();

        File dir = new File (root.getAbsolutePath());
        dir.mkdirs();
        File file = new File(dir, fileName);

        try {
            FileOutputStream f = new FileOutputStream(file,append);
            PrintWriter pw = new PrintWriter(f);
            pw.println(data);
            pw.flush();
            pw.close();
            f.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();

            Log.i("file", "******* File not found. Did you" +
                    " add a WRITE_EXTERNAL_STORAGE permission to the   manifest?");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeLog(String log){
        writeToSDFile(LOG_PATH, log,true);
    };
}