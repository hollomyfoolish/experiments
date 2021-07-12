package cn.hollomyfoolish.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

public final class ServerUtils {

    public static String getHostName(){
        String hostName = System.getenv("COMPUTERNAME");;
        if(hostName == null){
            try {
                hostName = InetAddress.getLocalHost().getHostName();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }
        return hostName;
    }

}
