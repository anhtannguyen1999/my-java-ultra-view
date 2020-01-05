package BLL;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Random;

import javax.swing.JOptionPane;

import GUI.LANForm;
import GUI.RemoteScreenForm;
import GUI.ServerChatForm;
import NET.LANServerThread;

public class BLL_LANForm {
	private static BLL_LANForm instance=null;
	public static BLL_LANForm GetInstance() {
		if(instance==null) {
			instance=new BLL_LANForm();
		}
		return instance;
	}
	
	//Ham nay dung de doc port da duoc setting truoc
	public static String GetMyPort() {
		//Doc port da duoc setting
		
		//Neu khong co thi lay mac dinh la 1999
		return "1999";
	}
	
	public static String RandromPassword() {
		return RandomPassword(6);
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
	
	
	private boolean isOpenConnection=false;
	
	public boolean  GetIsOpenConnection() {
		return isOpenConnection;
	}
	
	public void SetIsOpenConnection(boolean state) {
		isOpenConnection=state;
	}
	private LANServerThread server;
	public void OpenConnect(String ip, String port, String pass) throws Exception {
		System.out.println("Connect opened at "+ip+":"+port+" pass:"+pass);

		int iPort=0;
		try {
			iPort= Integer.parseInt(port);
		} catch (Exception e) {
			throw new Exception("Port khong hop le!");
		}
		
		if(iPort>=65530||iPort<10) {
			throw new Exception("Port khong hop le!");
		}
		
		//Neu port hop le thi mo server
		//LANServerThread 
		server=new LANServerThread(iPort,pass);
		server.start();
		isOpenConnection=true;		
	}
	
	public void ConnectAndShowRemoteForm(String ip,String port, String pass,int language) {
		System.out.println("Connecting to "+ip+":"+port+" pass:"+pass);
		boolean isSuccessConnect=true;//Viet 1 class/ham de try ket noi
		if(isSuccessConnect) {
			RemoteScreenForm.OpenForm(ip, port, pass,language);
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
			if(arrIPName.get(i).toLowerCase().contains("wireless")||arrIPName.get(i).toLowerCase().contains("lan")||
			(arrIPName.get(i).toLowerCase().contains("ethernet")&&!arrIPName.get(i).toLowerCase().contains("virtual"))) {
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
	
	public void AnnounceConnectError(String errMess) {
		System.out.println(errMess);
	}

	public void OpenChatWindow(int port) {
		// TODO Auto-generated method stub
		try {
			
			System.out.println("Chuan bi create instance server chat form");
			ServerChatForm.CreateInstanceServerChatForm(port);
			ServerChatForm serverChatForm=ServerChatForm.GetInstance();
			serverChatForm.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void CloseChatWindow() {
		System.out.println("Vao close chat window");
		// TODO Auto-generated method stub
		try {
			ServerChatForm.GetInstance().CloseAudioChat();
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		try {
			ServerChatForm.GetInstance().CloseChat();
		} catch (Exception e) {
			// TODO: handle exception
		}
		System.out.println("Chuan bi tat form chat");
		ServerChatForm.GetInstance().dispose();
		
	}

	public void ReOpenChat() {
		// TODO Auto-generated method stub
		ServerChatForm serverChatForm=ServerChatForm.GetInstance();
		if(serverChatForm!=null)
			serverChatForm.setVisible(true);
	}

	public void CloseConnect() {
		// TODO Auto-generated method stub
		try {
			server.interrupt();
			server.CloseServer();
			server.stop();
			isOpenConnection=false;
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
}
