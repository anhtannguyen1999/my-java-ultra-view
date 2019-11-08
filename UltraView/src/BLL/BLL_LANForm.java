package BLL;

import GUI.RemoteScreenForm;
import NET.LANServerThread;
import OtherComponent.ReusableFunctions;

public class BLL_LANForm {
	public static String GetMyIPv4() {
		
		return "192.168.57.2";
	}

	//Ham nay dung de doc port da duoc setting truoc
	public static String GetMyPort() {
		//Doc port da duoc setting
		
		//Neu khong co thi lay mac dinh la 1999
		return "1999";
	}
	
	public static String RandromPassword() {
		return ReusableFunctions.RandomPassword(6);
	}
	
	public static void OpenConnect(String ip, String port, String pass) {
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
	
	public static void ConnectAndShowRemoteForm(String ip,String port, String pass) {
		System.out.println("Connecting to "+ip+":"+port+" pass:"+pass);
		boolean isSuccessConnect=true;//Viet 1 class/ham de try ket noi
		if(isSuccessConnect) {
			RemoteScreenForm.OpenForm(ip, port, pass);
		}
	}
}
