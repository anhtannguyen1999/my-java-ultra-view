package BLL;

import java.net.InetAddress;

import NET.LANClientChat;
import NET.LANSocketInfor;

public class BLL_LANClientChat implements BLL_LANChat{
	private LANClientChat lanClientChat=null;
	private LANSocketInfor serverInfor=null;
	
	public BLL_LANClientChat(InetAddress serverIP,int serverPort) {
		serverInfor=new LANSocketInfor(serverIP, serverPort);
	}
	
	public BLL_LANClientChat(LANSocketInfor serverInfor) {
		this.serverInfor=serverInfor;
	}
	
	@Override
	public void Start() {
		lanClientChat=new LANClientChat(serverInfor);
		lanClientChat.start();
	}

	@Override
	public void Stop() {
		if(lanClientChat!=null) {
			lanClientChat.StopChat();
			lanClientChat=null;
		}
	}

	@Override
	public void SendMessage(String message) {
		if(lanClientChat!=null)
			lanClientChat.SendMessage(message);
	}
	
	public int GetClientChatSocketPort() {
		return lanClientChat.GetClientChatSocketPort();
	}
}
