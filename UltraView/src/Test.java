import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;

public class Test {
	public static void main(String[] args) throws Exception {
		System.out.println(ShowIP());
	}
	public static void TestMouse() throws InterruptedException {
		Robot bot = null;
		  try {
		   bot = new Robot();
		  } catch (Exception failed) {
		   System.err.println("Failed instantiating Robot: " + failed);
		  }
		  int mask = InputEvent.BUTTON1_DOWN_MASK;
		  Thread.sleep(1000);
		  bot.mouseMove(100, 100);
		  Thread.sleep(1000);
		  bot.mousePress(mask);
		  Thread.sleep(1000);
		  bot.mouseMove(200, 100);
		  Thread.sleep(100);
		  bot.mouseMove(300, 100);
		  Thread.sleep(100);
		  bot.mouseMove(400, 100);
		  Thread.sleep(100);
		  bot.mouseMove(500, 100);
		  Thread.sleep(100);
		  bot.mouseMove(600, 100);
		  Thread.sleep(100);
		  bot.mouseMove(700, 100);
		  Thread.sleep(100);
		  bot.mouseRelease(mask);
	}
	public static void TestKey() throws InterruptedException {
		Thread.sleep(3000);
		Robot bot = null;
		  try {
		   bot = new Robot();
		  } catch (Exception failed) {
		   System.err.println("Failed instantiating Robot: " + failed);
		  }
		  bot.keyPress(KeyEvent.VK_T);
		  bot.keyPress(KeyEvent.VK_A);
		  bot.keyPress(KeyEvent.VK_N);
	}
	
	public static String ShowIP(){
		String ip;
		ArrayList<String>arrIP=new ArrayList<>();
		ArrayList<String>arrIPName=new ArrayList<>();
		try {
		    Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
		    while (interfaces.hasMoreElements()) {
		        NetworkInterface iface = interfaces.nextElement();
		        // filters out 127.0.0.1 and inactive interfaces
		        if (iface.isLoopback() || !iface.isUp())
		            continue;

		        Enumeration<InetAddress> addresses = iface.getInetAddresses();
		        while(addresses.hasMoreElements()) {
		            InetAddress addr = addresses.nextElement();

		            // *EDIT*
		            if (addr instanceof Inet6Address) continue;

		            ip = addr.getHostAddress();
		            arrIP.add(ip);
		            arrIPName.add(iface.getDisplayName());
		        }
		    }
		} catch (SocketException e) {
		    throw new RuntimeException(e);
		}
		ip="";
		//return cai nao co wireless
		for(int i=0;i<arrIPName.size();i++) {
			if(arrIPName.get(i).toLowerCase().contains("wireless")||arrIPName.get(i).toLowerCase().contains("lan")) {
				System.out.println(arrIPName.get(i)+" ; "+arrIP.get(i));
				return arrIP.get(i);
			}
		}
		//Neu khong return cai ke cuoi
		if(arrIP.size()>=1)
			return arrIP.get(arrIP.size()-1);
		//khong thi return cai dau tien
		else 
			return "";
			
	}
	
}
