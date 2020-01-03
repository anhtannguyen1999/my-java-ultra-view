package BLL;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;

import NET.LANAudioServer;
import OS.Audio;


public class BLL_LANAudioServer implements BLL_LANAudioChat{
	public static void main(String[] args) {
		BLL_LANAudioServer bll_LANAudioServer=new BLL_LANAudioServer(1999);
		bll_LANAudioServer.StartSocketAndInitAudio();
		//bll_LANAudioServer.StartReceivingAndSpeaking();
		bll_LANAudioServer.SetClientIPAndPort("192.168.1.135", 2000 );//mo server va ket noi toi port 2000 cua client
		bll_LANAudioServer.StartRecordingAndSending();
	}
	
	private static BLL_LANAudioServer instance=null;
	public static BLL_LANAudioServer GetInstance(int serverStreamAudioPort) {
		if(instance==null) {
			instance=new BLL_LANAudioServer(serverStreamAudioPort);
		}
		return instance;
	}
	
	public static BLL_LANAudioServer GetInstance() {
		return instance;
	}
	
	public static void RemoveInstance() {
		if(instance!=null) {
			try {
				//instance.StopRecordingAndSending();
				//instance.StopReceivingAndSpeaking();
				instance.StopLANAudioServer();
			}catch (Exception e) {
				// TODO: handle exception
			}
			instance=null;
		}
	}
	
	private int port;
	LANAudioServer lanAudioServerThread=null;
	
	
	public BLL_LANAudioServer(int port) {
		this.port=port;
	}
	
	public void StartReceivingAndSpeaking() {
		if(lanAudioServerThread==null)
			StartSocketAndInitAudio();
		lanAudioServerThread.ReceiveAndSpeak();
	}
	public void StopReceivingAndSpeaking() {
		if(lanAudioServerThread!=null) {
			lanAudioServerThread.StopReceiveAndSpeak();
		}
	}
	
	public void StartRecordingAndSending() {
		if(lanAudioServerThread!=null)	
			lanAudioServerThread.RecordAndSend();
	}
	
	public void StopRecordingAndSending() {
		if(lanAudioServerThread!=null)
			lanAudioServerThread.StopRecordAndSend();
	}
	
	public void StopLANAudioServer() {
		if(lanAudioServerThread!=null)
			lanAudioServerThread.StopLANAudioServer();
	}
	
	public void StartSocketAndInitAudio()
	{
		try {
			lanAudioServerThread =new LANAudioServer(port);
		}
		catch(Exception ex)
		{
			System.out.println("LANAudioServerThread: StartSocketAndInitAudio fail");
			
		}	
	}
	
	public void SetClientIPAndPort(String clientIP,int clientPort) {
		if(lanAudioServerThread==null)
			StartSocketAndInitAudio();
		try {
			InetAddress ip= InetAddress.getByName(clientIP);
			lanAudioServerThread.SetClientIPAndPort(ip, clientPort);
			
		}catch (Exception e) {
			System.out.println("BLL_LANAudioServer EXC:SetClientIPAndPort");
		}
	}
	
	public void SetClientIPAndPort(InetAddress clientIP,int clientPort) {
		if(lanAudioServerThread==null)
			StartSocketAndInitAudio();
		try {
			lanAudioServerThread.SetClientIPAndPort(clientIP, clientPort);
		}catch (Exception e) {
			System.out.println("BLL_LANAudioServer EXC:SetClientIPAndPort");
		}
		
	}
}
