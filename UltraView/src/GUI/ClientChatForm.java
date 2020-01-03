package GUI;

import java.awt.EventQueue;
import java.awt.RenderingHints.Key;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JTextArea;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;

import BLL.BLL_LANAudioClient;
import BLL.BLL_LANAudioServer;
import BLL.BLL_LANClientChat;

public class ClientChatForm extends ChatForm{
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientChatForm.CreateInstanceClientChatForm("192.168.1.135",2001);
					ClientChatForm frame = ClientChatForm.GetInstance();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	private BLL_LANClientChat bll_LANClientChat=null;
	public static ClientChatForm instance=null;
	public static ClientChatForm GetInstance() {
		return instance;
	}
	public static void CreateInstanceClientChatForm(String IP, int port) {
		instance=new ClientChatForm(IP, port);
	}
	public static void RemoveInstance() {
		instance.CloseAudioChat();
		instance=null;
	}
	private ClientChatForm(String IP, int port) {
		super();
		KhoiTaoEventSend();
		StartClientChatSoket(IP,port);
		setTitle("Chat (c)");
		SetLblPartnerIP(IP);
	}
	
	@Override
	public void MicStateChange() {
		// TODO Auto-generated method stub
		super.MicStateChange();
		try {
			if(chbxMic.isSelected()) {
				System.out.println("Client mic open");
				try {
					BLL_LANAudioClient.GetInstance().StartRecordingAndSending();
				} catch (Exception e) {
					System.out.println("Khong ket noi duoc den server");
				}
			}else {
				System.out.println("Client mic close");
				BLL_LANAudioClient.GetInstance().StopRecordingAndSending();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	
	@Override
	public void SpeakerStateChange() {
		// TODO Auto-generated method stub
		super.SpeakerStateChange();
		try {
			if(chbxSpeaker.isSelected()) {
				System.out.println("Client speaker open");
				BLL_LANAudioClient.GetInstance().StartReceivingAndSpeaking();
			}else {
				System.out.println("Client speaker close");
				BLL_LANAudioClient.GetInstance().StopReceivingAndSpeaking();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	@Override
	public void KhoiTaoEventSend() {
		//Khoi tao event send
			btnSend.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					SendMessageInTextbox();
				}
			});
			//Cho textbox
			txtText.addKeyListener(new KeyAdapter() {
				private boolean isShift=false;
				@Override
				public void keyReleased(KeyEvent e) {
					//System.out.println(e.getKeyCode());
					if(e.getKeyCode()==10&&!isShift) {
						int length=txtText.getText().length();
						txtText.setText(txtText.getText().substring(0, length-1));
						SendMessageInTextbox();
						txtText.setText("");
					}
					else if(e.getKeyCode()==10&&isShift) {
						txtText.append("\n");
					}
					if(e.getKeyCode()==16)
						isShift=false;
				}
				
				@Override
				public void keyPressed(KeyEvent e) {
					if(e.getKeyCode()==16&&!isShift)
						isShift=true;
				}
				
			});
	}
	@Override
	public void SendMessageInTextbox() {
		try {
			if(!txtText.getText().equals("")) {
				String message=txtText.getText().replace("\n", "\n      ");
				txtText.setText("");
				if(bll_LANClientChat!=null) {
					bll_LANClientChat.SendMessage(message);
					chatData+="Me: "+message+"\n";
					txtChatBox.setText(chatData);
					//System.out.println("Client send: "+message);
				}else {
					System.out.println("No connection");
				}
			}
		}catch (Exception e) {
			System.out.println("Can not send message!");
		}
	}
	
	String serverIP;
	int serverPort;
	public void StartClientChatSoket(String IP, int port) {
		try {
			serverIP= IP;
			serverPort=port;
			bll_LANClientChat=new BLL_LANClientChat(InetAddress.getByName(IP),port);
			bll_LANClientChat.Start();
		} catch (UnknownHostException e) {
			System.out.println("IP khong hop le!");
		}
	}
	
	private BLL_LANAudioClient bll_LANAudioClient;
	@Override
	public void OpenAudioChat() {
		try {
			for(int i=0;i<10;i++) {
				try {
					Thread.sleep(500);
					if(bll_LANClientChat!=null) {
						int mylocalport = bll_LANClientChat.GetClientChatSocketPort();
						bll_LANAudioClient= BLL_LANAudioClient.GetInstance(mylocalport+1, serverIP, serverPort+1);
						System.out.println("Start client audio at "+(mylocalport+1));
						break;
					}
				} catch (Exception e) {
				}
			}
			bll_LANAudioClient.StartSocketAndInitAudio();
			bll_LANAudioClient.StartReceivingAndSpeaking();
			bll_LANAudioClient.StartRecordingAndSending();
		} catch (Exception e) {
			System.out.println("Khoi tao voice chat that bai!");
		}
	}
	@Override
	public void CloseAudioChat() {
		// TODO Auto-generated method stub
		BLL_LANAudioClient.RemoveInstance();
		//BLL_RemoteScreenForm.GetInstance().AnnounceConnectError("Mat ket noi!");
	}
	@Override
	public void CloseChat() {
		// TODO Auto-generated method stub
		bll_LANClientChat.Stop();
		setVisible(false);
		dispose();
		instance=null;
	}	
}
