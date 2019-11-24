package NET;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import GUI.ServerChatForm;

public class LANServerChat extends Thread implements LANChat{
	private Socket socket = null;
	private ServerSocket serverSocket = null;
	private DataInputStream iStream =  null;
	private DataOutputStream oStream=null;
	private int serverPort;
	private boolean isChatting=false;
	private ServerChatForm myServerChatForm=null;
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		try {
			myServerChatForm=ServerChatForm.GetInstance();
			serverSocket = new ServerSocket(serverPort);
			socket=serverSocket.accept();
			myServerChatForm.SetLblPartnerIP(socket.getInetAddress().toString());
			open();
			isChatting = true;
			while (isChatting) {
				try {
					String message = iStream.readUTF();
					
					myServerChatForm.AddMessage("Partner",message);
				}
				catch(IOException ioe)
				{  isChatting = false;
					System.out.println("Khong co ket noi!");
				}
			}
	        close();
		} catch (IOException e) {
			System.out.println("Loi khoi tao server chat"+e.toString());
		}
	}
	public void SendMessage(String message) {
		try {
			oStream.writeUTF(message);
			oStream.flush();
		} catch (Exception e) {
			System.out.println("Khong gui tin nhan duoc!");
		}
	}
	
	public LANServerChat(int serverPort) {
		// TODO Auto-generated constructor stub
		this.serverPort=serverPort;	
	}
	
	public void open() throws IOException{
		iStream = new DataInputStream(socket.getInputStream());
		oStream=new DataOutputStream(socket.getOutputStream());
	}
	
	public void close() throws IOException {
		try {
			isChatting=false;
			if (socket != null)
				socket.close();
			if (iStream != null)
				iStream.close();
			if(oStream!=null)
				oStream.close();
			if(serverSocket!=null)
				serverSocket.close();
			System.out.println("Chat dong ket noi");
		}catch (Exception e) {
			System.out.println("Loi dong ket noi");
		}
   }
	
	public void StopChat() {
		isChatting=false;
		try {
			close();
		} catch (IOException e) {
		}
	}
	
	public LANSocketInfor GetClientChatInfor() {
		// TODO Auto-generated method stub
		return new LANSocketInfor(socket.getInetAddress(), socket.getPort());
	}
	
}
