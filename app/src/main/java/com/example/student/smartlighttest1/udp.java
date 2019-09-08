package com.example.student.smartlighttest1;

import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;

public class udp

{
    public static int colvo =2 ;
    static int port =13013 ;
    private static byte[] buf = new byte[1024];
    // public static byte[] reciveData = new byte[1024];
    public static byte[] Data = new byte[1024];
    static DatagramSocket servSock;
    static DatagramSocket skt;
    static InetAddress serverAddr;
    static DatagramPacket[] packets;
    static public int brignes[];
    static int i=0;
    static String s;

    public static void setup() {
        try {
            servSock = new DatagramSocket(port);
            servSock.setReuseAddress(true);
            //servSock.bind(address);
        }
        catch (IOException e){Log.e("udp",e.getMessage());}

        try {
            serverAddr = InetAddress.getByName("10.17.0.1");
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
            Log.e("udp",e.getMessage());
        }
    }

    static   public void start() {
        int brighness=0;
        int number=0;
        do {
            if (i != 0) {
                DatagramPacket pk = new DatagramPacket(Data, Data.length);
                try {

                    servSock.receive(pk);
                    s=new String(pk.getData());
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String [] es=s.split("#")[0].split(" ");
                            int brighness=Integer.parseInt(es[1]);
                            int number=Integer.parseInt(es[0]);
                            brignes[number-1]=brighness;
                            Log.e("udp",number+" "+brighness );
                            i++;
                        }
                    }).start();
                    if (i==colvo)break;

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

        }while (i <= colvo) ;
        //brignes[number-1]=brighness;
        Log.d("lamp", Arrays.toString(brignes));
        multithread.finished=true;
    }
}