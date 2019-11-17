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

import NET.LANAudioClient.ClientAudioSenderThread;
import OS.Audio;
;

public class LANAudioServer
{
	private SourceDataLine audio_out =null;
	private DatagramSocket datagramSocket;
	
	private InetAddress client_ip;
	private int client_port;
	private int server_port;
	private boolean isCalling;
	private AudioFormat format;
	//DataLine.Info info;
	public LANAudioServer(int port) {
		StartServer(port);
		InitAudio();
	}
	public void SetClientIPAndPort(InetAddress clientIP,int clientPort) {
		client_ip=clientIP;
		client_port=clientPort;
	}
	private void StartServer(int port) {
		server_port=port;
		try {
			System.out.println("Open server audio at "+server_port);
			datagramSocket=new DatagramSocket(server_port);
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
			audio_out = (SourceDataLine)AudioSystem.getLine(infoOut);
			audio_in=(TargetDataLine)AudioSystem.getLine(infoIn);
		} catch (LineUnavailableException e) {
			System.out.println("LANAudioServerThread Exception: init audioout faild");
		}
		OpenAudioOut();
	}
	private void OpenAudioOut() {
		try {
			audio_out.open(format);
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
		audio_out.stop();
		if(serverAudioReceiverThread!=null) {
			serverAudioReceiverThread.interrupt();
			//serverAudioReceiverThread.stop();
			serverAudioReceiverThread=null;
		}
		//audio_out.flush();
	}
	public class ServerAudioReceiverThread extends Thread{
		private byte byte_buff[]= new byte[512];
		@Override	
		public void run()
		{
			super.run();
			OpenAudioOut();
			audio_out.start();
			isCalling=true;
			int i=0;
			while (isCalling)
			{
				DatagramPacket data =new DatagramPacket(byte_buff,byte_buff.length);
				try {
					datagramSocket.receive(data);
					byte_buff =data.getData();
					audio_out.write(byte_buff, 0, byte_buff.length);
					Thread.sleep(2);
				}
				catch(Exception ex)
				{
					System.out.println("AudioServerERR: receiver Socket NULL: "+ex);
				}
			}
			audio_out.close();
			audio_out.drain();
			System.out.println("player stop");
		}
	}
	
	
	
	//Record and send
	private ClientAudioSenderThread clientAudioSenderThread=null;
	public void RecordAndSend() {
		if(clientAudioSenderThread==null)
			clientAudioSenderThread=new ClientAudioSenderThread();
		clientAudioSenderThread.start();
	}

	private TargetDataLine audio_in =null;
	public void StopRecordAndSend() {
		isRecordAndSend=false;
		audio_in.stop();
		if(clientAudioSenderThread!=null) {
			clientAudioSenderThread.interrupt();
			clientAudioSenderThread=null;
		}
	}
	
	private boolean isRecordAndSend=false;
	public class ClientAudioSenderThread extends Thread {
		private byte byte_buff[]= new byte[512];
		@Override
		public void run() {
			super.run();
			OpenAudioIn();
			audio_in.start();
			isRecordAndSend=true;
			//int i=0;
			while (isRecordAndSend)
			{
				try
				{
					audio_in.read(byte_buff, 0, byte_buff.length);
					DatagramPacket data =new DatagramPacket(byte_buff,byte_buff.length,client_ip,client_port);
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
			audio_in.close();
			audio_in.drain();
			System.out.println("AudioServerERR: Recroder stop!");
		}

		private void OpenAudioIn() {
			try {
				audio_in.open(format);
			} catch (LineUnavailableException e) {
				// TODO Auto-generated catch block
				System.out.println("LANAudioServerThread Exception: open audioin faild");
			}
		}
	}
}

