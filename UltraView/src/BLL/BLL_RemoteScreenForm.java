package BLL;

import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

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
	
	public void SendClick(MouseEvent e, boolean type) {
		//type=TRUE->ClickDown| type=FALSE->MouseUp
		if(LANclient!=null) {
			if(type==true) {
				if (e.getButton() == MouseEvent.BUTTON1){//left
					LANclient.SendMessage("CLK-LEFT-DOWN");
	            } else if (e.getButton() == MouseEvent.BUTTON2){//midle
	            	LANclient.SendMessage("CLK-MID-DOWN");
	            } else if (e.getButton() == MouseEvent.BUTTON3) {//right
	            	LANclient.SendMessage("CLK-RIGHT-DOWN");
	            }
			}else {
				if (e.getButton() == MouseEvent.BUTTON1){//left
					LANclient.SendMessage("CLK-LEFT-UP");
	            } else if (e.getButton() == MouseEvent.BUTTON2){//midle
	            	LANclient.SendMessage("CLK-MID-UP");
	            } else if (e.getButton() == MouseEvent.BUTTON3) {//right
	            	LANclient.SendMessage("CLK-RIGHT-UP");
	            }
			}
		}
	}
	public void SendMouseMove(float xRatio, float yRatio) {
		LANclient.SendMessage("MMOVE-"+xRatio+"-"+yRatio);
	}
	
	public void SendMouseWheel(MouseWheelEvent e) {	
		if (e.getWheelRotation() > 0)
			LANclient.SendMessage("MWHEEL-DOWN-"+e.getScrollAmount());
		else
			LANclient.SendMessage("MWHEEL-UP-"+e.getScrollAmount());
		//System.out.println(e.getScrollAmount());
	}
	
	public void SendKeyEvent(int keyCode,boolean type) {//type=true->keyPress | type->false->keyRelease
		if(type==true) {
			LANclient.SendMessage("KEY-DOWN-"+keyCode);
		}
		else {
			LANclient.SendMessage("KEY-UP-"+keyCode);
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
