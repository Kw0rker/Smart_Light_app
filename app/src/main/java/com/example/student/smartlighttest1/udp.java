package com.example.student.smartlighttest1;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.concurrent.TimeUnit;

public class udp

{
    public static int colvo;
    static int port = 12345;
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
        buffer = ByteBuffer.wrap(Data);
        try {
            serverAddr = InetAddress.getByName("localhost");
        } catch (UnknownHostException e) {
            System.out.println("host error");
            System.exit(-1);
        }
        try {
            skt = new DatagramSocket();
            servSock = new DatagramSocket(50001);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void sender(String sendmsg) {
        buf = sendmsg.getBytes();
        DatagramPacket pkt;
        pkt = new DatagramPacket(buf, buf.length, serverAddr, port);
        try {
            skt.send(pkt);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        int i = 0;
        do {
            if (i != 0) {
                packets[i] = new DatagramPacket(Data, Data.length);
                try {

                    servSock.receive(packets[i]);
                    Data = packets[i].getData();
                    brignes[i-1]=buffer.getInt();
                    i++;

                } catch (IOException e) {
                    System.out.println("socket error");
                }

                }
                else {
                    DatagramPacket pk =new DatagramPacket(Data, Data.length);
                try {

                    servSock.receive(pk);
                    Data = pk.getData();
                    colvo=buffer.getInt();
                    brignes =new int[colvo];
                    i++;
                } catch (IOException e) {
                    System.out.println("socket error");
                }
                    }

        }while (i < colvo) ;
    }
}
