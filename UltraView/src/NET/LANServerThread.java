package NET;

import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import BLL.BLL_RemoteScreenForm;
import DTO.DTO_ArrayLANImageInforObject;
import OtherComponent.ScreenCapturer;

public class LANServerThread extends Thread {
	
	private int port;
	private String pass;
	private LANClientInfor clientInfor=null;
	private static ServerSocket ss;
	private static Socket s;//client socket
	private static InputStream is;
	private static ObjectInputStream ois;
	private static OutputStream os;
	private static ObjectOutputStream oos;
	
	private static BufferedOutputStream bout;// = new BufferedOutputStream(os);

    public LANServerThread(int port, String pass) {
		// TODO Auto-generated constructor stub
		this.port=port;
		this.pass=pass;
	}
	@Override
	public void run() {
		super.run();
		StartServer();
	}
	
	private void StartServer() {
		try {
			//Khoi tao server
			try {
				ss = new ServerSocket(port);
			}catch(Exception e){
				System.out.println(e);
			}
			
			while (clientInfor==null) {
				//Bat client socket doi ket noi
				s = ss.accept();
				System.out.println("Co client ket noi");
				is = s.getInputStream();
				ois = new ObjectInputStream(is);
								
				//Lay may thong diep client gui toi
				String message = (String) ois.readObject();
				System.out.println("Reading messge");
                
              //Neu check pass dung va chua co client nao ket noi thi gan clientinfor
                if(message.equals("RequireConnect:"+pass)&&clientInfor==null) {
                	clientInfor=new LANClientInfor(s.getInetAddress(), s.getPort());
                	System.out.println("Pass chinh xac, start sending image to"+clientInfor.getIp().toString()+":"+clientInfor.getPort());
                	
                	//Khoi tao cac output stream
                	os = s.getOutputStream();
                	
                	//Buffer
    	        	bout = new BufferedOutputStream(os);

    				oos = new ObjectOutputStream(bout);
    				
    				oos.writeObject("XacThucThanhCong");
    				oos.reset();
                	//Goi ham gui image va break
                	try {
            			LoopSendImage();
            		} catch (Exception e) {
            			// TODO: handle exception
            			BLL_RemoteScreenForm.GetInstance().AnnounceConnectError("Khong co ket noi!");
            			
            		}
                }
                else {
        			ois.close();
        			is.close();
        			s.close();
                }
            }
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Loi roi: "+e.toString());
		}
	}

	private DTO_ArrayLANImageInforObject arrayLANImageInforObject=null;
	private void LoopSendImage() {
		ScreenCapturer screenCapturer=new ScreenCapturer();
		while(true) {
			try {
    	         Thread.sleep(40);
    	         //Convert to byte
    	         arrayLANImageInforObject=screenCapturer.GetLANScreenCaptureImageArray();
    	         if (arrayLANImageInforObject.arr.size()==0) {
    	        	 continue;
    	         }
    	         try {
    	 			oos.writeObject(arrayLANImageInforObject);
    	 			arrayLANImageInforObject.Clear();
    	 			oos.flush();
    	 			os.flush();
    	 			oos.reset();
    	 			java.lang.Runtime.getRuntime().gc();
    	 		} catch (Exception e) {
    	 			
    	 			System.out.println("Send obj to client failed!");
    	 		}
                 
			} 
        	catch (Exception ex) {
	               System.out.println(ex.toString());
	               
    	    }
		}
		
	}
	public static void main(String[] args) {
		LANServerThread lanServerThread=new LANServerThread(1999, "ahihi");
		lanServerThread.run();
	}
	
	
}
