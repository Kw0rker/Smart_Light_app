package com.example.student.smartlighttest1;

import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;

public class udp {
    static int port = 13013;
    private static byte[] buf = new byte[1024];
    public static byte[] Data = new byte[1024];
    private static DatagramSocket servSock;
    private static InetAddress serverAddr;
    static public String[] id;


    public static void setup() {
        try {
            servSock = new DatagramSocket(port);
            servSock.setReuseAddress(true);
            //servSock.bind(address);
        } catch (IOException e) {
            file.writeToSDFile("logs.txt",e.getLocalizedMessage(),true);
        }

        try {
            serverAddr = InetAddress.getByName("10.17.0.1");
        } catch (IOException e) {
            file.writeToSDFile("logs.txt",e.getLocalizedMessage(),true);
        }
    }


    public static void sender(String sendmsg) {
        buf = sendmsg.getBytes();
        DatagramPacket pkt;

        pkt = new DatagramPacket(buf, buf.length, serverAddr, port);
        try {
            servSock.send(pkt);
        } catch (IOException e) {
            file.writeToSDFile("logs.txt",e.getLocalizedMessage(),true);
            Log.e("udp",e.getMessage());
        }
    }

    public static void start() {
        ArrayList<String> mes = new ArrayList<>();
        DatagramPacket packet = new DatagramPacket(Data, Data.length);
        try {
            servSock.receive(packet);
        } catch (IOException e) {
            Toast.makeText(MainActivity.context_g, e.getMessage() + "\n Ошибка подключения", Toast.LENGTH_LONG).show();
        }
        try {
            lamp.numberOfLamps = Integer.parseInt(new String(packet.getData()).split("#")[0]);
        }
        catch (NumberFormatException e){file.writeToSDFile("logs.txt",e.getLocalizedMessage(),true);return;}
        id = new String[lamp.numberOfLamps];
        while (mes.size() < lamp.numberOfLamps) {
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
                catch (NumberFormatException e){file.writeToSDFile("logs.txt",e.getLocalizedMessage(),true);}

            } catch (Exception e) {
                file.writeToSDFile("logs.txt",e.getLocalizedMessage(),true);
            }
        }

        multithread.finished = true;
    }

    /*static   public void start() {
        int brighness=0;
        int number=0;
        do {
            if (i != 0) {
                if (i==colvo)break;
                DatagramPacket pk = new DatagramPacket(Data, Data.length);
                try {

                    servSock.receive(pk);
                    s=new String(pk.getData());
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                          try {
                              String [] es=s.split("#")[0].split(" ");
                              try {
                                  int brighness=Integer.parseInt(es[1]);
                                  int number=Integer.parseInt(es[0]);
                                  brignes[number-1]=brighness;
                                  Log.e("udp",number+" "+brighness );
                                  i++;
                              }
                              catch (Exception e){}
                          }
                          catch (NumberFormatException e){}

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
                    try{colvo=Integer.parseInt(s.split("#")[0]);}
                    catch (NumberFormatException e){colvo=1;continue;}
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
    }*/
}