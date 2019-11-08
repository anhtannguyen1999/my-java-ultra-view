package BLL;

import NET.LANClientThread;
import NET.LANServerThread;

public class BLL_RemoteScreenForm {
	private static BLL_RemoteScreenForm instance=null;
	public static BLL_RemoteScreenForm GetInstance() {
		if(instance==null) {
			instance=new BLL_RemoteScreenForm();
		}
		return instance;
	}
	
	LANClientThread LANclient=null;
	public void ConnectRemoteTo(String ip,String port,String pass) {
		int iPort;
		try {
			iPort= Integer.parseInt(port);
			if(iPort>=65535||iPort<10)
				throw new Exception("Port khong hop le!");
			//Neu port hop le thi open client
			LANclient=new LANClientThread(ip,iPort,pass);
			LANclient.start();
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Port khong hop le!");
			return;
		}
				
	}
	
	public void SendClick(float xRatio, float yRatio) {
		if(LANclient!=null) {
			LANclient.SendClick(xRatio, yRatio);
		}
	}
	
	public void AnnounceConnectError(String errMess) {
		System.out.println(errMess);
	}
	
	public void DisConnectRemote() {
		if(LANclient!=null) {
			LANclient.DestroyClient();
			LANclient=null;
		}
	}
}
