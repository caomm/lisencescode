package cn.ourfuture;

import sun.misc.BASE64Encoder;

import java.net.InetAddress;
import java.net.SocketException;


public class Main {

    public Main()
    {

        String []osname = System.getProperties().getProperty("os.name").toLowerCase().split(" ");
        //String [] iparg = {"192.168.8.51"};
      //  String mac = SystemUtil.getMACAddress("192.168.8.51");
       // System.out.println(mac);
        InetAddress ia = SystemUtil.getIpAddress();
        String macAddress = null;
        try {
            if(osname[0].equals("windows")){
                macAddress = SystemUtil.getWinLocalMac(ia);
            }else{
                macAddress = SystemUtil.getLocalMac(ia);
            }

        } catch (SocketException e) {
            e.printStackTrace();
        }
        String ipAddress = ia.getHostAddress();
        String expireDate = "2016-08-10 23:59:59";
        System.out.println(macAddress);


        String str = macAddress + "|"+ipAddress + "|" + expireDate;
        String ret = null;
        ret = new BASE64Encoder().encode(str.getBytes());
        System.out.println(ret);
    }


    public static void main(String args[])
    {
        new Main();
    }
}
