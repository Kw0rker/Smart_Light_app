package com.example.student.smartlighttest1;

import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class udp

{
    public static int colvo;
    static int port = 13013;
    private static byte[] buf = new byte[1024];
    // public static byte[] reciveData = new byte[1024];
    public static byte[] Data = new byte[1024];
    static DatagramSocket servSock;
    static DatagramSocket skt;
    static InetAddress serverAddr;
    DatagramPacket[] packets;
    static public int brignes[];
    private static ByteBuffer buffer;

    public static void setup() {

        try {
            serverAddr = InetAddress.getByName("172.16.1.133");
            servSock = new DatagramSocket(13013);
        } catch (IOException e) {
            Log.e("udp",e.getMessage());
        }
    }


    public static void sender(String sendmsg) {
        buf = sendmsg.getBytes();
        DatagramPacket pkt;
        pkt = new DatagramPacket(buf, buf.length, serverAddr, port);
        try {
            servSock.send(pkt);
            Log.e("udp",sendmsg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        int b=0;
        int i = 0;
        do {
            if (i != 0) {
                DatagramPacket pk = new DatagramPacket(Data, Data.length);
                try {

                    servSock.receive(pk);
                    int a=Integer.parseInt(new String(pk.getData()).split("#")[0]);
                    brignes[i-1]=a;
                    i++;
                    Log.d("udp",""+a);
                    b=a;

                } catch (IOException e) {
                   Log.e("udp",e.getMessage());
                }

                }
                else {
                    DatagramPacket pk =new DatagramPacket(Data, Data.length);
                try {

                    servSock.receive(pk);
                    Log.d("udp","recived");
                    Data = pk.getData();
                    String s=new String(pk.getData());
                    colvo=Integer.parseInt(s.split("#")[0]);
                    brignes =new int[colvo];
                    packets=new DatagramPacket[colvo];
                    i++;
                    Log.d("udp",""+colvo);
                } catch (IOException e) {
                    Log.e("udp",e.getMessage());
                }
                    }

        }while (i < colvo) ;
        brignes[brignes.length-1]=b;
        Log.d("lamp", Arrays.toString(brignes));
        multithread.finished=true;
    }
}
