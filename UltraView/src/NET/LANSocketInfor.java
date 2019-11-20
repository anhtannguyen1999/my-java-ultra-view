package NET;

import java.net.InetAddress;

public class LANSocketInfor {
	private InetAddress ip;
	private int port;
	public InetAddress getIp() {
		return ip;
	}
	public void setIp(InetAddress ip) {
		this.ip = ip;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	
	public LANSocketInfor(InetAddress ip, int port) {
		// TODO Auto-generated constructor stub
		this.ip=ip;
		this.port=port;
	}
}
