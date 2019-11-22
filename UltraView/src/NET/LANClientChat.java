package NET;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import GUI.ClientChatForm;

public class LANClientChat extends Thread implements LANChat{
	private Socket socket = null;
	private DataInputStream iStream = null;
	private DataOutputStream oStream = null;
	private LANSocketInfor serverInfor=null;
	private boolean isChatting=false;
	private ClientChatForm myClientChatForm=null;
	public LANClientChat(LANSocketInfor serverInfor) {
		this.serverInfor=serverInfor;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		try {
			myClientChatForm=ClientChatForm.GetInstance();
			socket = new Socket(serverInfor.getIp(), serverInfor.getPort());
			System.out.println("Connected: " + socket);
		    open();
		    String message = "";
		    isChatting=true;
		    System.out.println("Khoi tao client thanh cong!");
			while (isChatting) {
				try {
					message=iStream.readUTF();
					System.out.println(message);
					myClientChatForm.AddMessage("Partner",message);
					}
				catch(IOException ioe) {
					System.out.println("Khong co ket noi!");
					isChatting=false;
				}
			}
			close();
			
		}
		catch (Exception e) {
			System.out.println("Loi khoi tao client");
		}
	}
	
	@Override
	public void SendMessage(String message) {
		try {
			oStream.writeUTF(message);
			oStream.flush();
			
		} catch (IOException e) {
			System.out.println("Khong gui tin nhan duoc!");
		}
		
	}
	@Override
	public void open() throws IOException {
		// TODO Auto-generated method stub
		iStream = new DataInputStream(socket.getInputStream());
		oStream=new DataOutputStream(socket.getOutputStream());
	}
	@Override
	public void close() throws IOException {
		try {
			isChatting=false;
			if (socket != null)
				socket.close();
			if (iStream != null)
				iStream.close();
			if(oStream!=null)
				oStream.close();
			System.out.println("Chat dong ket noi");
		} catch (Exception e) {
			System.out.println("Loi dong ket noi");
		}
	}
	@Override
	public void StopChat() {
		isChatting=false;
		try {
			close();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public int GetClientChatSocketPort() {
		// TODO Auto-generated method stub
		return socket.getLocalPort();
	}
}
