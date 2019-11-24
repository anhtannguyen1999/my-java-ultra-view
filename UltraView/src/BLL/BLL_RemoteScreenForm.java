package BLL;

import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import javax.swing.JOptionPane;

import GUI.ClientChatForm;
import GUI.RemoteScreenForm;
import GUI.ServerChatForm;
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
	public void ConnectRemoteTo(String ip,String port,String pass) throws Exception{
		int iPort;
		try {
			iPort= Integer.parseInt(port);
		} catch (Exception e) {
			RemoteScreenForm.GetInstance().ShowMessage("Port is invalid", "Input problem", JOptionPane.ERROR_MESSAGE);
			throw new Exception("Port khong hop le!");
		}
		
		if(iPort>=65535||iPort<10) {
			RemoteScreenForm.GetInstance().ShowMessage("Port is invalid", "Input problem", JOptionPane.ERROR_MESSAGE);
			throw new Exception("Port khong hop le!");
		}
			
		//Neu port hop le thi open client
		LANclient=new LANClientThread(ip,iPort,pass);
		LANclient.start();
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
		try {
			LANclient.SendMessage("MMOVE-"+xRatio+"-"+yRatio);
		}catch (Exception e) {//bat loi vi luc dau no send chuot cho server
			// TODO: handle exception
		}
		
	}
	
	public void SendMouseWheel(MouseWheelEvent e) {	
		if (e.getWheelRotation() > 0)
			LANclient.SendMessage("MWHEEL-DOWN-"+e.getScrollAmount());
		else
			LANclient.SendMessage("MWHEEL-UP-"+e.getScrollAmount());
		//System.out.println(e.getScrollAmount());
	}
	
	private String oldKeyEvent="";
	private String newKeyEvent="";
	public void SendKeyEvent(int keyCode,boolean type) {//type=true->keyPress | type->false->keyRelease
		if(type==true) {
			newKeyEvent="KEY-DOWN-"+keyCode;
		}
		else {
			newKeyEvent="KEY-UP-"+keyCode;
		}
		if(!oldKeyEvent.equals(newKeyEvent)) {
			System.out.println(newKeyEvent);
			LANclient.SendMessage(newKeyEvent);
			oldKeyEvent=newKeyEvent;
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
		//Tat khung chat neu dang hide
		try {
			if(!ClientChatForm.GetInstance().isVisible()) {
				ClientChatForm.RemoveInstance();
				System.out.println("Xoa instance");
			}
		} catch (Exception e) {
			//Chat form da tat
		}
	}
	
	public void OpenChatWindow(String serverIP,int serverPort) {
		// TODO Auto-generated method stub
		try {
			ClientChatForm.CreateInstanceClientChatForm(serverIP,serverPort);
			ClientChatForm clientChatForm = ClientChatForm.GetInstance();
			clientChatForm.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void CloseChatWindow() {
		// TODO Auto-generated method stub
		try {
			ClientChatForm.GetInstance().CloseAudioChat();
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		try {
			ClientChatForm.GetInstance().CloseChat();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public void ReOpenChat() {
		// TODO Auto-generated method stub
		ClientChatForm clientChatForm=ClientChatForm.GetInstance();
		if(clientChatForm!=null)
			clientChatForm.setVisible(true);
	}
	
	public void ShowStatus(String status) {
		RemoteScreenForm.GetInstance().ShowStatus(status);
	}
}
