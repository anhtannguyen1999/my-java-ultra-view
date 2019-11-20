package BLL;

import NET.LANServerChat;
import NET.LANSocketInfor;

public class BLL_LANServerChat implements BLL_LANChat {
	private LANServerChat lanServerChat=null;
	private int serverPort;
	public BLL_LANServerChat(int serverPort) {
		this.serverPort=serverPort;
	}
	
	@Override
	public void Start() {
		lanServerChat=new LANServerChat(serverPort);
		lanServerChat.start();//Start server and receive message
	}
	
	//Stop server
	@Override
	public void Stop() {
		if(lanServerChat!=null) {
			lanServerChat.StopChat();
			lanServerChat=null;
		}
	}
	
	//Send message
	@Override
	public void SendMessage(String message) {
		if(lanServerChat!=null) {
			lanServerChat.SendMessage(message);
		}
	}

	public LANSocketInfor GetClientChatInfor() {
		// TODO Auto-generated method stub
		return lanServerChat.GetClientChatInfor();
	}
}
