package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.Image;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import BLL.BLL_RemoteScreenForm;
import DTO.DTO_ArrayLANImageInforObject;
import DTO.DTO_LANImageInforObject;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class RemoteScreenForm extends JFrame {
	public static void main(String[] args) {
		OpenForm("192.168.57.2", "1999", "ahihi");
	}
	//STATIC
	//Check trang thai form chi mo 1 lan 1 form
	public static RemoteScreenForm instance;
	public static boolean isOpened=false;
	public static void OpenForm(String ip, String port, String pass) {
		if(isOpened)
			return;
		else {
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					try {
						instance = new RemoteScreenForm(ip, port, pass);
						instance.setVisible(true);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}
	}
	
	public static RemoteScreenForm GetInstance() {
		return instance;
	}
	
	//Get instance cua BLL_RemoteScreenForm
	private BLL_RemoteScreenForm bll_RemoteScreenForm=BLL_RemoteScreenForm.GetInstance();
	private JPanel contentPane;
	private String ip,port,pass;

	//Khoi tao GUI
	public static JPanel panel;
	public RemoteScreenForm(String ip, String port, String pass) {
		this.ip=ip;
		this.port=port;
		this.pass=pass;
		
		//Them cac event
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent arg0) {
				System.out.println("Resize");
				
				//panel.setSize(contentPane.getWidth()-25, contentPane.getHeight()-10);
				panel.removeAll();
				panel.updateUI();
				for(int i=0;i<16;i++) 
					for(int j=0;j<16;j++) {
						JLabel label=new JLabel("");
						//label.setBackground(Color.PINK);
						panel.add(label);
						label.setSize(300, 300);
						
					}
			}
		});
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				bll_RemoteScreenForm.DisConnectRemote();
				isOpened=false;
			}
			@Override
			public void windowOpened(WindowEvent e) {
				isOpened=true;
				//Goi BLL de mo thread get hinh
				bll_RemoteScreenForm.ConnectRemoteTo(ip, port, pass);
				//System.out.println("Open");
			}
		});
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(10, 10, 919, 522);
		//setExtendedState(JFrame.MAXIMIZED_BOTH);
		
		//setUndecorated(true); //cai nay lam mat thanh tieu de
		setVisible(true);
		
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		panel = new JPanel();
		panel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				float xRatio=(float) (arg0.getX()*1.0/panel.getWidth());
				float yRatio=(float)(arg0.getY()*1.0/panel.getHeight());
				bll_RemoteScreenForm.SendClick(xRatio, yRatio);
			}
		});
		//panel.setBackground(Color.RED);
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(panel, GroupLayout.DEFAULT_SIZE, 879, Short.MAX_VALUE)
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(panel, GroupLayout.DEFAULT_SIZE, 464, Short.MAX_VALUE)
					.addContainerGap())
		);
		
		panel.setLayout(new GridLayout(16, 16, 0, 0));
		
		for(int i=0;i<16;i++) 
			for(int j=0;j<16;j++) {
				JLabel label=new JLabel("");
				//label.setBackground(Color.PINK);
				panel.add(label);
				label.setSize(300, 300);
				
			}
		
		contentPane.setLayout(gl_contentPane);
		
	}

	
	public void ShowImageToPanel(DTO_ArrayLANImageInforObject arrOIP) {
		if(arrOIP!=null) {
			try {
				for (DTO_LANImageInforObject oip : arrOIP.arr) {
					Component[] components= panel.getComponents();
					int index=oip.row*16+oip.column;
					JLabel label=(JLabel)components[index];
					Image dimg = oip.imageIcon.getImage().getScaledInstance(label.getWidth(), label.getHeight(), Image.SCALE_SMOOTH);
					label.setIcon(new ImageIcon(dimg));
					//panel.add(label00);
				}
			}catch (Exception e) {
				// TODO: handle exception
			}
			
		}
		else {
			
		}
	}
	
	public void ShowImageToPanel(DTO_LANImageInforObject lanIIO) {
		if(lanIIO!=null) {
			try {
				Component[] components= panel.getComponents();
				int index=lanIIO.row*16+lanIIO.column;
				JLabel label=(JLabel)components[index];
				Image dimg = lanIIO.imageIcon.getImage().getScaledInstance(label.getWidth(), label.getHeight(), Image.SCALE_SMOOTH);
				label.setIcon(new ImageIcon(dimg));
				//panel.add(label00);
			}catch (Exception e) {
				// TODO: handle exception
			}
			
		}
		else {
			
		}
	}
		
}