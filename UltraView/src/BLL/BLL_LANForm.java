package BLL;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Random;

import GUI.RemoteScreenForm;
import NET.LANServerThread;

public class BLL_LANForm {
	
	//Ham nay dung de doc port da duoc setting truoc
	public static String GetMyPort() {
		//Doc port da duoc setting
		
		//Neu khong co thi lay mac dinh la 1999
		return "1999";
	}
	
	public static String RandromPassword() {
		return RandomPassword(6);
	}
	
	public void OpenConnect(String ip, String port, String pass) {
		System.out.println("Connect opened at "+ip+":"+port+" pass:"+pass);
		int iPort;
		try {
			iPort= Integer.parseInt(port);
			if(iPort>=65535||iPort<10)
				throw new Exception("Port khong hop le!");
			
			//Neu port hop le thi mo server
			LANServerThread server=new LANServerThread(iPort,pass);
			server.start();
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Port khong hop le!");
			return;
		}
		
	}
	
	public void ConnectAndShowRemoteForm(String ip,String port, String pass) {
		System.out.println("Connecting to "+ip+":"+port+" pass:"+pass);
		boolean isSuccessConnect=true;//Viet 1 class/ham de try ket noi
		if(isSuccessConnect) {
			RemoteScreenForm.OpenForm(ip, port, pass);
		}
	}
	
	public static String GetMyIPv4() {
		String ip;
		ArrayList<String>arrIP=new ArrayList<>();
		ArrayList<String>arrIPName=new ArrayList<>();
		try {
		    Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
		    while (interfaces.hasMoreElements()) {
		        NetworkInterface iface = interfaces.nextElement();
		        // filters out 127.0.0.1 and inactive interfaces
		        if (iface.isLoopback() || !iface.isUp())
		            continue;

		        Enumeration<InetAddress> addresses = iface.getInetAddresses();
		        while(addresses.hasMoreElements()) {
		            InetAddress addr = addresses.nextElement();

		            // *EDIT*
		            if (addr instanceof Inet6Address) continue;

		            ip = addr.getHostAddress();
		            arrIP.add(ip);
		            arrIPName.add(iface.getDisplayName());
		        }
		    }
		} catch (SocketException e) {
		    throw new RuntimeException(e);
		}
		ip="";
		//return cai nao co wireless
		for(int i=0;i<arrIPName.size();i++) {
			if(arrIPName.get(i).toLowerCase().contains("wireless")||arrIPName.get(i).toLowerCase().contains("lan")) {
				System.out.println(arrIPName.get(i)+" ; "+arrIP.get(i));
				return arrIP.get(i);
			}
		}
		//Neu khong return cai ke cuoi
		if(arrIP.size()>=1)
			return arrIP.get(arrIP.size()-1);
		//khong thi return cai dau tien
		else 
			return "";
	}

	public static String RandomPassword(int length) {
		String kq="";
		Random r = new Random();
		for(int i=0;i<length;i++) {
			char c = (char)(r.nextInt(26) + 'a');
			kq+=c;
		}
		return kq;
	}
}
