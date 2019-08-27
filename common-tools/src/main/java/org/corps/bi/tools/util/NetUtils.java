package org.corps.bi.tools.util;

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
	
	public static void main(String []args){
		for(String ip:localMachineIp()){
			System.out.println(ip+" "+IpUtils.isValidIp(ip));
		}
	}
	
}
