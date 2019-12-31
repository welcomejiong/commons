package org.corps.bi.tools.util;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

public class NetUtils {

	public static Set<String> localMachineIp(){
		Set<String> ips=new HashSet<String>();
		try {
			Enumeration<NetworkInterface> localInterfaces = NetworkInterface.getNetworkInterfaces();
	        while (localInterfaces.hasMoreElements()) {   
	            NetworkInterface ni = localInterfaces.nextElement();   
	            Enumeration<InetAddress> ipAddresses = ni.getInetAddresses();   
	            while (ipAddresses.hasMoreElements()) {   
	                InetAddress adderss = ipAddresses.nextElement();
	                if(!IpUtils.isValidIp(adderss.getHostAddress())){
	                	continue;
	                }
	                ips.add(adderss.getHostAddress());
	            }
	        }
		} catch (SocketException e) {
			e.printStackTrace();
		}   
		return ips;
	}
	
	public static String getLocalMachineIp() {
		try {
		      Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
		      InetAddress ip = null;
		      while (allNetInterfaces.hasMoreElements()) {
		        NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
		        //用于排除回送接口,非虚拟网卡,未在使用中的网络接口.
		        if (netInterface.isLoopback() || netInterface.isVirtual() || !netInterface.isUp()) {
		          continue;
		        } else {
		          Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
		          while (addresses.hasMoreElements()) {
		            ip = addresses.nextElement();
		            if (ip != null && ip instanceof Inet4Address) {
		              return ip.getHostAddress();
		            }
		          }
		        }
		      }
		    } catch (Exception e) {
		    	System.err.println("IP地址获取失败" + e.toString());
		    }
		    return "";
	}
	
	public static void main(String []args){
		for(String ip:localMachineIp()){
			System.out.println(ip+" "+IpUtils.isValidIp(ip));
		}
		System.out.println(getLocalMachineIp());
	}
	
}
