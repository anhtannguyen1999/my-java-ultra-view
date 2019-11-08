package NET;
import java.net.*;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import javax.imageio.stream.MemoryCacheImageOutputStream;
import javax.swing.ImageIcon;

import BLL.BLL_RemoteScreenForm;
import DTO.DTO_ArrayLANImageInforObject;
import DTO.DTO_LANImageInforObject;
import GUI.RemoteScreenForm;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.*;

public class LANClientThread extends Thread{
	public static void main(String[] args) {
		LANClientThread lanClientThread=new LANClientThread("192.168.56.1",1999 ,"ahihi" );
		lanClientThread.StartClient();
	}

	private String serverIP;
	private int serverPort;
	private String pass;

	private Socket s;
	private OutputStream os;
	private ObjectOutputStream oos;
	private InputStream is;
	private ObjectInputStream ois;
	private BufferedInputStream bin;
	
	
	private boolean isReceivingImage=false;
	private int countFaild=0;
	private final int maxCountFail=6;
	
	public LANClientThread(String serverIP, int serverPort, String pass) {
		this.serverIP=serverIP;
		this.serverPort=serverPort;
		this.pass=pass;
	}

	public void run() {
		StartClient();
		System.out.println("Chay client thanh cong!");
		try {
			LoopReceiveImage();
		} catch (Exception e) {
			BLL_RemoteScreenForm.GetInstance().AnnounceConnectError("Mat ket noi!");
		}
	}
	
	//Start and destroy client
	public ThreadGuiNhanYeuCauXacThuc threadGuiNhanYeuCauXacThuc=null;
	public void StartClient() {
		//Gui goi tin yeu cau ket noi
		try {
			if(s==null) {
				s = new Socket(serverIP,serverPort);
				if(os==null&&oos==null) {
					os = s.getOutputStream();
					oos = new ObjectOutputStream(os);
				}
			}
			
			//is vs ois da chuyen cho thread o duoi khoi tao
			if(threadGuiNhanYeuCauXacThuc==null) {
				threadGuiNhanYeuCauXacThuc=new ThreadGuiNhanYeuCauXacThuc();
				threadGuiNhanYeuCauXacThuc.start();
			}
				
		}catch(Exception e){
			System.out.println(e);
		}
		isReceivingImage=true;
		
		System.out.println("Da gui yeu cau ket noi den: "+serverIP+":"+serverPort+" pass: "+pass);
		
		
	}
	
	public void DestroyClient() {
		System.out.println("Destroy client!");
		isReceivingImage=false;
		try {
			oos.close();
			os.close();
			
			ois.close();
			is.close();
			s.close();
						
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
	}
	
	public void SetStateReceiveImage(boolean state) {
		isReceivingImage=state;
	}

	//Receive image
	public void LoopReceiveImage() {
		countFaild=0;
		while(isReceivingImage) {
			try {
				ReceiveImage();
				//System.out.println("Nhan hinh roi nhe!");
			} catch (InterruptedException e) { e.printStackTrace();}
			
		}
	}
	public DTO_ArrayLANImageInforObject arrLANIIO=null;
	private RemoteScreenForm remoteScreenForm=RemoteScreenForm.GetInstance();
	public  void ReceiveImage() throws InterruptedException {
		Thread.sleep(30);
		if(arrLANIIO!=null)
			arrLANIIO.Clear();
		arrLANIIO=null;
		
		try {
			//imi=(ImageIcon) ois.readObject();
			
			arrLANIIO=(DTO_ArrayLANImageInforObject) ois.readObject();
		} catch (Exception e) { //Co gang tao client neu ket noi fail
			System.out.println("Receive Image Failed!");
			System.out.println(e.toString());
			try {
				StartClient();
			}catch (Exception e2) {
			}
		}
		if (arrLANIIO!=null){
			remoteScreenForm.ShowImageToPanel(arrLANIIO);
			//System.out.println("AHIHI");
			try {
				java.lang.Runtime.getRuntime().gc();
			} catch (Exception e) {}
			//System.out.println("OK nhan duoc");
			if(countFaild!=0)
				countFaild=0;
		}
		else {
			System.out.println("NULLLLLLL");
			countFaild++;
			Thread.sleep(30);
			if(countFaild>maxCountFail) {
				isReceivingImage=false;
				DestroyClient();
			}
				
		}
		
	}
	
	public void SendClick(float xRatio, float yRatio) {
		System.out.println("Send click "+xRatio+":"+yRatio);
		if(s!=null&&oos!=null) {
			try {
				oos.writeObject("RequireConnect:"+pass);
				oos.reset();
			}catch (Exception e) {}
		}
	}
	
	public class ThreadGuiNhanYeuCauXacThuc extends Thread{
		public ThreadGuiNhanYeuCauXacThuc() {
			// TODO Auto-generated constructor stub
		}
		public boolean isXacThuc=false;
		@Override
		public void run() {
			super.run();
			System.out.println("Dang gui yeu cau xac thuc len server");
			GetMessageXacThuc getMessageXacThuc=new GetMessageXacThuc();
			getMessageXacThuc.start();
			while(!isXacThuc) {
				try {
					Thread.sleep(10);
					oos.writeObject("RequireConnect:"+pass);
					oos.reset();
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			java.lang.Runtime.getRuntime().gc();
			System.out.println("Thread stop");
		}
		
		public class GetMessageXacThuc extends Thread {
			public void run() {
				while (true) {
					try {
						is = s.getInputStream();
						bin=new BufferedInputStream(is);
						ois = new ObjectInputStream(bin);
						String message= (String)ois.readObject();
						if(message.equals("XacThucThanhCong")) {
							isXacThuc=true;
							return;
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						//e.printStackTrace();
					} 
				}
			}
		}
	}
}
