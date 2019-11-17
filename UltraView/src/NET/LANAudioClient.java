package NET;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sound.sampled.*;

import OS.Audio;

public class LANAudioClient {
		
	public LANAudioClient(int clientPort, InetAddress serverIP,int serverPort) {
		StartClient(clientPort,serverIP, serverPort);
		InitAudio();
	}
	
	private void StartClient(int clientPort,InetAddress serverIP,int serverPort) {
		try {
			System.out.println("Chuan bi mo client audio");
			datagramSocket=new DatagramSocket(clientPort);
			System.out.println("Open client audio at "+clientPort +" And connect to "+serverIP.toString()+":"+serverPort);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			System.out.println("LANAudioClientThread Exception: create UDP faild");
		}
		server_ip=serverIP;
		server_port=serverPort;
	}
	private AudioFormat format;
	private void InitAudio() {
		format = Audio.GetAudioFormat();
		DataLine.Info infoIn = new DataLine.Info(TargetDataLine.class,format);
		DataLine.Info infoOut = new DataLine.Info(SourceDataLine.class,format);
		if(!AudioSystem.isLineSupported(infoIn))
		{
			System.out.println("Not support");
			return;
		}
		
		if(!AudioSystem.isLineSupported(infoOut))
		{
			System.out.println("infoOut Not support");
			return;
		}
		try {
			audio_in = (TargetDataLine)AudioSystem.getLine(infoIn);
			audio_out=(SourceDataLine)AudioSystem.getLine(infoOut);
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			System.out.println("LANAudioClientThread Exception: create audioin faild");
		}
		OpenAudioIn();
		OpenAudioOut();
	}
	
	private void OpenAudioIn() {
		try {
			audio_in.open(format);
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			System.out.println("LANAudioClientThread Exception: open audioin faild");
		}
	}
	
	private TargetDataLine audio_in =null;
	private SourceDataLine audio_out =null;
	private DatagramSocket datagramSocket;

	private InetAddress server_ip;
	private int server_port;
	private boolean isRecordAndSend;
	private ClientAudioSenderThread clientAudioSenderThread;
	
	public void RecordAndSend() {
		if(clientAudioSenderThread==null)
			clientAudioSenderThread=new ClientAudioSenderThread();
		clientAudioSenderThread.start();
	}
	
	public void StopRecordAndSend() {
		isRecordAndSend=false;
		audio_in.stop();
		if(clientAudioSenderThread!=null) {
			clientAudioSenderThread.interrupt();
			clientAudioSenderThread=null;
		}
			
	}
	
	//Receive And Speak
	private ClientAudioReceiverThread clientAudioReceiverThread=null;
	public void ReceiveAndSpeak() {
		if(clientAudioReceiverThread==null) {
			clientAudioReceiverThread=new ClientAudioReceiverThread();
			clientAudioReceiverThread.start();
		}
	}

	public void StopReceiveAndSpeak() {
		isReceiveAndSpeak=false;
		audio_out.stop();
		if(clientAudioReceiverThread!=null) {
			clientAudioReceiverThread.interrupt();
			clientAudioReceiverThread=null;
		}
	}
	
	public class ClientAudioSenderThread extends Thread {
		private byte byte_buff[]= new byte[512];
		@Override
		public void run() {
			super.run();
			OpenAudioIn();
			audio_in.start();
			isRecordAndSend=true;
			int i=0;
			while (isRecordAndSend)
			{
				
				try
				{
					audio_in.read(byte_buff, 0, byte_buff.length);
					DatagramPacket data =new DatagramPacket(byte_buff,byte_buff.length,server_ip,server_port);
//					System.out.println("send "+i++);
//					if(i>1000) 
//						i=0;
					datagramSocket.send(data);
					Thread.sleep(2);
					
				}
				catch(Exception ex)
				{
					System.out.println("AudioClientERR: Server NULL: "+ex);
				}
		
			}
			audio_in.close();
			audio_in.drain();
			System.out.println("AudioClientERR: Recroder stop!");
		}
	}
	
	private boolean isReceiveAndSpeak=false;
	public class ClientAudioReceiverThread extends Thread {
		private byte byte_buff[]= new byte[512];
		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			//System.out.println("Client Nhan audio tu: "+datagramSocket.getPort()+" MY PORT: "+datagramSocket.getLocalPort());
			OpenAudioOut();
			audio_out.start();
			isReceiveAndSpeak=true;
			while(isReceiveAndSpeak) {
				DatagramPacket data =new DatagramPacket(byte_buff,byte_buff.length);
				try {
					datagramSocket.receive(data);
					byte_buff =data.getData();
					audio_out.write(byte_buff, 0, byte_buff.length);
					Thread.sleep(2);
				}catch (Exception e) {
					System.out.println("AudioClientERR: Socket NULL: "+e);
				}
			}
			audio_out.close();
			audio_out.drain();
			System.out.println("player stop");
		}
	}

	private void OpenAudioOut() {
		try {
			audio_out.open(format);
		} catch (LineUnavailableException e) {
			System.out.println("LANAudioClientThread Exception: open audioout faild");
		}
	}

}

