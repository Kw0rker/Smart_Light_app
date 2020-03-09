package com.example.student.smartlighttest1;

import android.util.Log;
import android.widget.Toast;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;

public class udp {
    static final int port = 13013;
    static final String addr = "10.17.0.1";
    private static byte[] buf = new byte[1024];
    public static byte[] Data = new byte[1024];
    private static DatagramSocket servSock = null;
    private static InetAddress serverAddr;
    static public String[] id;


    private static void setup() {
        try {
            servSock = new DatagramSocket(port);
            servSock.setReuseAddress(true);
        } catch (IOException e) {
            file.writeLog(e.getLocalizedMessage());
        }

        try {
            serverAddr = InetAddress.getByName(addr);
        } catch (IOException e) {
            file.writeLog(e.getLocalizedMessage());
        }
    }


    public static void sender(String sendmsg) {
        if(servSock == null) setup();

        buf = sendmsg.getBytes();
        DatagramPacket pkt;

        pkt = new DatagramPacket(buf, buf.length, serverAddr, port);
        try {
            servSock.send(pkt);
        } catch (IOException e) {
            file.writeLog(e.getLocalizedMessage());
            Log.e("udp",e.getMessage());
        }
    }

    public static HashMap<String,Integer> status(){
        HashMap<String,Integer> lampList = new HashMap<>();
        sender("status");
        DatagramPacket packet = new DatagramPacket(Data, Data.length);
        int numberOfLamps = 0;
        try {
            servSock.receive(packet);
        } catch (IOException e) {
            Toast.makeText(MainActivity.context_g, e.getMessage() + "\n Ошибка подключения", Toast.LENGTH_LONG).show();
        }
        try {
            numberOfLamps = Integer.parseInt(new String(packet.getData()).split("#")[0]);
            MainActivity.numberOfLamps = numberOfLamps;
        } catch (NumberFormatException e){
            file.writeLog(e.getLocalizedMessage());
        }

        for(int i = 0; i<numberOfLamps; ++i){
            packet = new DatagramPacket(Data, Data.length);
            try {
                servSock.receive(packet);
                Log.d("upd",new String(packet.getData()));
            } catch (IOException e) {
                Toast.makeText(MainActivity.context_g, e.getMessage() + "\n Ошибка подключения", Toast.LENGTH_LONG).show();
            }
            String[] msg = new String(packet.getData()).split("#")[0].split(" ");
            try {
                lampList.put(msg[0], Integer.parseInt(msg[1]));
            } catch(NumberFormatException e){
                file.writeLog(e.getLocalizedMessage());
            }
        }

    return lampList;
    }

  /*  public static void start() {
        if(servSock == null) setup();
        ArrayList<String> mes = new ArrayList<>();
        DatagramPacket packet = new DatagramPacket(Data, Data.length);
        try {
            servSock.receive(packet);
        } catch (IOException e) {
            Toast.makeText(MainActivity.context_g, e.getMessage() + "\n Ошибка подключения", Toast.LENGTH_LONG).show();
        }
        try {
            MainActivity.numberOfLamps = Integer.parseInt(new String(packet.getData()).split("#")[0]);
        }
        catch (NumberFormatException e){
            file.writeLog(e.getLocalizedMessage());
            return;
        }
        id = new String[MainActivity.numberOfLamps];
        while (mes.size() < MainActivity.numberOfLamps) {
            packet = new DatagramPacket(Data, Data.length);
            try {
                servSock.receive(packet);
                Log.d("upd",new String(packet.getData()));
            } catch (IOException e) {
                Toast.makeText(MainActivity.context_g, e.getMessage() + "\n Ошибка подключения", Toast.LENGTH_LONG).show();
            }
            mes.add(new String(packet.getData()));
        }
        int xxx = 0;
        for (String Mess : mes) {
            String[] es = Mess.split("#")[0].split(" ");
            try {
                id[xxx++]=es[0];
                try{
                    int bigness= Integer.parseInt(es[1]);
                    lamp.brigness.put(es[0],bigness);
                }
                catch (NumberFormatException e){file.writeLog(e.getLocalizedMessage());}

            } catch (Exception e) {
                file.writeLog(e.getLocalizedMessage());
            }
        }

        multithread.finished = true;
    } */

}