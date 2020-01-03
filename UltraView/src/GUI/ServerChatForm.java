package GUI;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import BLL.BLL_LANAudioClient;
import BLL.BLL_LANAudioServer;
import BLL.BLL_LANServerChat;
import NET.LANSocketInfor;

public class ServerChatForm extends ChatForm {
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ServerChatForm.CreateInstanceServerChatForm(2001);
					ServerChatForm frame=ServerChatForm.GetInstance();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public static ServerChatForm instance=null;
	public static ServerChatForm GetInstance() {
		return instance;
	}
	
	public static void CreateInstanceServerChatForm(int serverPort) {
		instance=new ServerChatForm(serverPort);
		System.out.println("Da CreateInstanceServerChatForm ");
	}
	
	private ServerChatForm(int serverPort) {
		super();
		KhoiTaoEventSend();
		StartServerChatSocket(serverPort);
		setTitle("Chat (s)");
	}
	
	@Override
	public void MicStateChange() {
		// TODO Auto-generated method stub
		super.MicStateChange();
		try {
			if(chbxMic.isSelected()) {
				System.out.println("Server mic open");
				BLL_LANAudioServer.GetInstance().StartRecordingAndSending();
			}else {
				System.out.println("Server mic close");
				BLL_LANAudioServer.GetInstance().StopRecordingAndSending();
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
				System.out.println("Server speaker open");
				BLL_LANAudioServer.GetInstance().StartReceivingAndSpeaking();

			}else {
				System.out.println("Server speaker close");
				BLL_LANAudioServer.GetInstance().StopReceivingAndSpeaking();
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
				if(bll_LANServerChat!=null) {
					bll_LANServerChat.SendMessage(message);
					chatData+="Me: "+message+"\n";
					txtChatBox.setText(chatData);
					//System.out.println("Server send: "+message);
				}else {
					System.out.println("No connection");
				}
			}
		}catch (Exception e) {
			System.out.println("Can not send message!");
		}
	}
	private BLL_LANServerChat bll_LANServerChat=null;
	public void StartServerChatSocket(int serverPort) {
		System.out.println("Start server chat socket");
		mylocalport=serverPort;
		bll_LANServerChat=new BLL_LANServerChat(serverPort);
		bll_LANServerChat.Start();
	}

	private int mylocalport;
	private BLL_LANAudioServer bll_LANAudioServer;
	@Override
	public void OpenAudioChat() {
		// TODO Auto-generated method stub
		try {
			for(int i=0;i<10;i++) {
				try {
					Thread.sleep(500);
					if(bll_LANServerChat!=null) {
						LANSocketInfor clientChatInfo= bll_LANServerChat.GetClientChatInfor();
						bll_LANAudioServer= BLL_LANAudioServer.GetInstance(mylocalport+1);
						bll_LANAudioServer.SetClientIPAndPort(clientChatInfo.getIp(), clientChatInfo.getPort()+1);
						System.out.println("Start client audio at "+(mylocalport+1));
						break;
					}
				} catch (Exception e) {
				}
			}
			bll_LANAudioServer.StartReceivingAndSpeaking();
    		bll_LANAudioServer.StartRecordingAndSending();
			
		} catch (Exception e) {
			System.out.println("Khoi tao voice chat that bai!");
		}
	}

	@Override
	public void CloseAudioChat() {
		// TODO Auto-generated method stub
		BLL_LANAudioServer.RemoveInstance();
	}
	@Override
	public void CloseChat() {
		// TODO Auto-generated method stub
		bll_LANServerChat.Stop();
		setVisible(false);
		dispose();
		instance=null;
	}
}
