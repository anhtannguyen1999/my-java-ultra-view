package NET;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;


import OS.Audio;
;

public class LANAudioServer
{
	private SourceDataLine audioOut =null;
	private DatagramSocket datagramSocket;
	
	private InetAddress clientIP;
	private int clientPort;
	private int serverPort;
	private boolean isCalling;
	private AudioFormat format;
	//DataLine.Info info;
	public LANAudioServer(int port) {
		StartServer(port);
		InitAudio();
	}
	public void SetClientIPAndPort(InetAddress clientip,int clientport) {
		clientIP=clientip;
		clientPort=clientport;
	}
	private void StartServer(int port) {
		serverPort=port;
		try {
			System.out.println("Open server audio at "+serverPort);
			datagramSocket=new DatagramSocket(serverPort);
		} catch (SocketException e) {
			System.out.println("AudioServer Exception: create UDP fail");
		}		
	}
	
	private void InitAudio() {
		format = Audio.GetAudioFormat();
		DataLine.Info infoOut = new DataLine.Info(SourceDataLine.class,format);
		DataLine.Info infoIn = new DataLine.Info(TargetDataLine.class,format);
		if(!AudioSystem.isLineSupported(infoOut))
		{
			System.out.println("infoOut Not support");
			return;
		}
		if(!AudioSystem.isLineSupported(infoIn))
		{
			System.out.println("infoIn Not support");
			return;
		}
		try {
			audioOut = (SourceDataLine)AudioSystem.getLine(infoOut);
			audioIn=(TargetDataLine)AudioSystem.getLine(infoIn);
		} catch (LineUnavailableException e) {
			System.out.println("LANAudioServerThread Exception: init audioout faild");
		}
		OpenAudioOut();
	}
	private void OpenAudioOut() {
		try {
			audioOut.open(format);
		} catch (LineUnavailableException e) {
			System.out.println("LANAudioServerThread Exception: open audioout faild");
		}
	}
	private ServerAudioReceiverThread serverAudioReceiverThread=null;
	public void ReceiveAndSpeak() {
		if(serverAudioReceiverThread==null)
			serverAudioReceiverThread=new ServerAudioReceiverThread();
//		if(serverAudioReceiverThread!=null) {
//			serverAudioReceiverThread.interrupt();
//			serverAudioReceiverThread=new ServerAudioReceiverThread();
//		}
			
		serverAudioReceiverThread.start();		
	}
	
	@SuppressWarnings("deprecation")
	public void StopReceiveAndSpeak() {
		isCalling=false;
		audioOut.stop();
		if(serverAudioReceiverThread!=null) {
			serverAudioReceiverThread.interrupt();
			//serverAudioReceiverThread.stop();
			serverAudioReceiverThread=null;
		}
		//audioOut.flush();
	}
	public class ServerAudioReceiverThread extends Thread{
		private byte byte_buff[]= new byte[512];
		@Override	
		public void run()
		{
			super.run();
			OpenAudioOut();
			audioOut.start();
			isCalling=true;
			int i=0;
			while (isCalling)
			{
				DatagramPacket data =new DatagramPacket(byte_buff,byte_buff.length);
				try {
					datagramSocket.receive(data);
					byte_buff =data.getData();
					audioOut.write(byte_buff, 0, byte_buff.length);
					Thread.sleep(2);
				}
				catch(Exception ex)
				{
					System.out.println("AudioServerERR: receiver Socket NULL: "+ex);
				}
			}
			audioOut.close();
			audioOut.drain();
			System.out.println("player stop");
		}
	}
	
	
	
	//Record and send
	private ServerAudioSenderThread serverAudioSenderThread=null;
	public void RecordAndSend() {
		if(serverAudioSenderThread==null)
			serverAudioSenderThread=new ServerAudioSenderThread();
		serverAudioSenderThread.start();
	}

	private TargetDataLine audioIn =null;
	public void StopRecordAndSend() {
		isRecordAndSend=false;
		audioIn.stop();
		if(serverAudioSenderThread!=null) {
			serverAudioSenderThread.interrupt();
			serverAudioSenderThread=null;
		}
	}
	
	private boolean isRecordAndSend=false;
	public class ServerAudioSenderThread extends Thread {
		private byte byte_buff[]= new byte[512];
		@Override
		public void run() {
			super.run();
			OpenAudioIn();
			audioIn.start();
			isRecordAndSend=true;
			//int i=0;
			while (isRecordAndSend)
			{
				try
				{
					audioIn.read(byte_buff, 0, byte_buff.length);
					DatagramPacket data =new DatagramPacket(byte_buff,byte_buff.length,clientIP,clientPort);
					//System.out.println("send "+i++);
//					if(i>1000) 
//						i=0;
					datagramSocket.send(data);
					Thread.sleep(2);	
				}
				catch(Exception ex)
				{
					System.out.println("AudioServerERR: sender Socket NULL: "+ex);
				}
		
			}
			audioIn.close();
			audioIn.drain();
			System.out.println("AudioServerERR: Recroder stop!");
		}

		private void OpenAudioIn() {
			try {
				audioIn.open(format);
			} catch (LineUnavailableException e) {
				// TODO Auto-generated catch block
				System.out.println("LANAudioServerThread Exception: open audioin faild");
			}
		}
	}
	
	public void StopLANAudioServer() {
		StopRecordAndSend();
		StopReceiveAndSpeak();
		datagramSocket.close();
		datagramSocket=null;
	}
}

