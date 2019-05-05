package com.example.student.smartlighttest1;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Arrays;

public class getter_from_app  implements Runnable {
@Override
    public void run(){
        boolean working = true;
        DatagramSocket serverSocket = null;
        byte[] receiveData = new byte[1024];
        while (working) {
            try {
                if (serverSocket == null) serverSocket = new DatagramSocket(50002);
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                serverSocket.receive(receivePacket);
                String mesg = new String(receivePacket.getData());
                //Log.e("udp", mesg + "\n");
                if (mesg.contains("buttons")) {
                    String[] str = mesg.split("#");
                    Log.d("Buttons", Arrays.toString(str));
                    String[] data = new String[str.length - 2];
                    for (int a = 1; a <= str.length- 1; a++) {
                        final String x_y[] = str[a].split("/");
                        Log.d("udp", str[a]);
                       try {
                           data[a - 1] =str[a]+"\n";
                       }catch (Exception e){}
                        final int id = a;
                        MainActivity.activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    MainActivity.buttons[id - 1].setTranslationX(Float.parseFloat(x_y[0]));
                                    MainActivity.buttons[id - 1].setTranslationY(Float.parseFloat(x_y[1]));
                                } catch (Exception e) {
                                }

                            }
                        });
                        Log.d("Write", "sucssess");
                    }
                    writeToFile(data, "buttons.txt", Context.MODE_PRIVATE);
                } else if (mesg.contains("scenario")) {
                    String[] str = mesg.split("#");
                    Log.d("udp", str[1]);
                    if(!isExist(str[1]))
                    {
                        String[] fs = str[1].split("-");
                        scenario.set_new(fs[1]);
                    }
                    writeToFile((str[1] + "\n"), "scenarios.txt",Context.MODE_APPEND);

                } else if (mesg.contains("end")) {
                    working = false;
                }

            } catch (IOException e) {
               // Log.e("udp", e.getMessage());
            }
        }
        MainActivity.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                MainActivity.read("buttons.txt",udp.colvo);
            }
        });
    }

    static void writeToFile(String data[], String file_name, int mode) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(MainActivity.context_g.openFileOutput(file_name, mode));
            for (String ms : data) {if (ms!=null)outputStreamWriter.write(ms);}
            outputStreamWriter.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }

    }

    static void writeToFile(String data, String file_name, int mode) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(MainActivity.context_g.openFileOutput(file_name, mode));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }


    }
    static boolean isExist(String sx)
    {
        boolean result=false;
          BufferedReader br=null;
        try{ br = new BufferedReader(new InputStreamReader(MainActivity.activity.openFileInput("scenarios.txt")));}
        catch (Exception e){Log.e("Write",e.getMessage());}
        String str="";

       try{
           BufferedWriter writer = new BufferedWriter(new FileWriter("scenarios.txt"));
           while ((str = br.readLine())!= null)
           {
               if (str.contains(sx))return true;
               writer.write(str + System.getProperty("line.separator"));
           }
       }
       catch (Exception e){Log.e("Read",e.getMessage());}

        return result;
    }
}
