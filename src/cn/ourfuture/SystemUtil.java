package cn.ourfuture;

import java.io.*;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by wangyanqiong on 2016/7/4.
 */
public class SystemUtil {

    public static String getLocalMac(InetAddress ia) {
        String mac = null;
        List<String> devices = new ArrayList<>();
        Pattern pattern = Pattern.compile("^ *(.*):");
        try (FileReader reader = new FileReader("/proc/net/dev")) {
            BufferedReader in = new BufferedReader(reader);
            String line = null;
            while( (line = in.readLine()) != null) {
                Matcher m = pattern.matcher(line);
                if (m.find()) {
                    devices.add(m.group(1));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // read the hardware address for each device
        for (String device : devices) {
            try (FileReader reader = new FileReader("/sys/class/net/" + device + "/address")) {
                BufferedReader in = new BufferedReader(reader);
                String addr = in.readLine();

                mac = String.format("%5s: %s", device, addr);
                mac = mac.substring(mac.indexOf(":")+1).trim();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    return mac;
    }

    public static String getWinLocalMac(InetAddress ia) throws SocketException {
        // TODO Auto-generated method stub
        //获取网卡，获取地址
        byte[] mac = NetworkInterface.getByInetAddress(ia).getHardwareAddress();
        StringBuffer sb = new StringBuffer("");
        for(int i=0; i<mac.length; i++) {
            if(i!=0) {
                sb.append("-");
            }
            //字节转换为整数
            int temp = mac[i]&0xff;
            String str = Integer.toHexString(temp);
            if(str.length()==1) {
                sb.append("0"+str);
            }else {
                sb.append(str);
            }
        }
        return sb.toString().toUpperCase();
    }


    public static InetAddress getIpAddress() {

        try {
            InetAddress ia = InetAddress.getLocalHost();
            return ia;
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return null;
    }

    //@Test
   /* public static String getMACAddress(String ipAddress) {
        String str = "", strMAC = "", macAddress = "";
        try {
            Process pp = Runtime.getRuntime().exec("nbtstat -a " + ipAddress);
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);
            for (int i = 1; i < 100; i++) {
                str = input.readLine();
                if (str != null) {
                    if (str.indexOf("MAC Address") > 1) {
                        strMAC = str.substring(str.indexOf("MAC Address") + 14, str.length());
                        break;
                    }
                }
            }
        } catch (IOException ex) {
            return "Can't Get MAC Address!";
        }
        if (strMAC.length() < 17) {
            return "Error!";
        }
        macAddress = strMAC.substring(0, 2) + ":" + strMAC.substring(3, 5) + ":" + strMAC.substring(6, 8) + ":" + strMAC.substring(9, 11) + ":"
                + strMAC.substring(12, 14) + ":" + strMAC.substring(15, 17);
        return macAddress;
    }*/

    /**
     * 执行单条指令
     * @param cmd 命令
     * @return 执行结果
     * @throws Exception
     */
    public static String command(String cmd) throws Exception{
        Process process = Runtime.getRuntime().exec(cmd);
        process.waitFor();
        InputStream in = process.getInputStream();
        StringBuilder result = new StringBuilder();
        byte[] data = new byte[256];
        while(in.read(data) != -1){
            String encoding = System.getProperty("sun.jnu.encoding");
            result.append(new String(data,encoding));
        }
        return result.toString();
    }


    /**
     * 获取mac地址
     * @param ip
     * @return
     * @throws Exception
     */
    public static String getMacAddress(String ip) throws Exception{
        String result = command("ping "+ip+" -n 2");
        if(result.contains("TTL")){
            result = command("arp -a "+ip);
        }
        String regExp = "([0-9A-Fa-f]{2})([-:][0-9A-Fa-f]{2}){5}";
        Pattern pattern = Pattern.compile(regExp);
        Matcher matcher = pattern.matcher(result);
        StringBuilder mac = new StringBuilder();
        while (matcher.find()) {
            String temp = matcher.group();
            mac.append(temp);
        }
        return mac.toString();
    }

    public static void main(String []args){
        try {
            System.out.println(getMacAddress("192.168.9.6"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
