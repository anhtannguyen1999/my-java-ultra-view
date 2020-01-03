package NET;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sound.sampled.*;

import OS.Audio;

public class LANAudioClient {
		
	public LANAudioClient(int clientPort, InetAddress serverIp,int serverport) {
		StartClient(clientPort,serverIp, serverport);
		InitAudio();
	}
	
	private void StartClient(int clientPort,InetAddress serverIp,int serverport) {
		try {
			System.out.println("Chuan bi mo client audio");
			datagramSocket=new DatagramSocket(clientPort);
			System.out.println("Open client audio at "+clientPort +" And connect to "+serverIp.toString()+":"+serverport);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			System.out.println("LANAudioClientThread Exception: create UDP faild");
		}
		serverIP=serverIp;
		serverPort=serverport;
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
			audioIn = (TargetDataLine)AudioSystem.getLine(infoIn);
			audioOut=(SourceDataLine)AudioSystem.getLine(infoOut);
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			System.out.println("LANAudioClientThread Exception: create audioin faild");
		}
		OpenAudioIn();
		OpenAudioOut();
	}
	
	private void OpenAudioIn() {
		try {
			audioIn.open(format);
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			System.out.println("LANAudioClientThread Exception: open audioin faild +" +e.toString());
		}
	}
	
	private TargetDataLine audioIn =null;
	private SourceDataLine audioOut =null;
	private DatagramSocket datagramSocket;

	private InetAddress serverIP;
	private int serverPort;
	private boolean isRecordAndSend;
	private ClientAudioSenderThread clientAudioSenderThread;
	
	public void RecordAndSend() {
		if(clientAudioSenderThread==null)
			clientAudioSenderThread=new ClientAudioSenderThread();
		clientAudioSenderThread.start();
	}
	
	public void StopRecordAndSend() {
		isRecordAndSend=false;
		audioIn.stop();
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
		audioOut.stop();
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
			audioIn.start();
			isRecordAndSend=true;
			int i=0;
			while (isRecordAndSend)
			{	
				try
				{
					audioIn.read(byte_buff, 0, byte_buff.length);
					DatagramPacket data =new DatagramPacket(byte_buff,byte_buff.length,serverIP,serverPort);
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
			audioIn.close();
			audioIn.drain();
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
			audioOut.start();
			isReceiveAndSpeak=true;
			while(isReceiveAndSpeak) {
				DatagramPacket data =new DatagramPacket(byte_buff,byte_buff.length);
				try {
					datagramSocket.receive(data);
					byte_buff =data.getData();
					audioOut.write(byte_buff, 0, byte_buff.length);
					Thread.sleep(2);
				}catch (Exception e) {
					System.out.println("AudioClientERR: Socket NULL: "+e);
				}
			}
			audioOut.close();
			audioOut.drain();
			System.out.println("player stop");
		}
	}

	private void OpenAudioOut() {
		try {
			audioOut.open(format);
		} catch (LineUnavailableException e) {
			System.out.println("LANAudioClientThread Exception: open audioout faild");
		}
	}

	public void StopLANAudioClient() {
		StopRecordAndSend();
		StopReceiveAndSpeak();
		datagramSocket.close();
		datagramSocket=null;
	}
}

