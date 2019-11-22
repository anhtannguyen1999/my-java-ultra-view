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
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JCheckBox;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.LineBorder;
import java.awt.Font;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class RemoteScreenForm extends JFrame {
	public static void main(String[] args) {
		OpenForm("192.168.1.135", "1999", "ahihi");
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
	public JPanel panel;
	public JCheckBox chbxMouse;
	public JCheckBox chbxKeys;
	
	private int colRowNum=16;
	private JButton btnReOpenChat;
	public RemoteScreenForm(String ip, String port, String pass) {
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				if(chbxKeys.isSelected()) {
					//System.out.println("kprs");
					bll_RemoteScreenForm.SendKeyEvent(arg0.getKeyCode(), true);
				}
			}
			@Override
			public void keyReleased(KeyEvent e) {
				if(chbxKeys.isSelected()) {
					//System.out.println("krlease");
					bll_RemoteScreenForm.SendKeyEvent(e.getKeyCode(), false);
				}
			}
		});
		this.ip=ip;
		this.port=port;
		this.pass=pass;
		
		//Them cac event
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent arg0) {
				System.out.println("Resize");
				
				//panel.setSize(contentPane.getWidth()-25, contentPane.getHeight()-10);
				if(panel!=null) {
					panel.removeAll();
					panel.updateUI();
					for(int i=0;i<colRowNum;i++) 
						for(int j=0;j<colRowNum;j++) {
							JLabel label=new JLabel("");
							//label.setBackground(Color.PINK);
							panel.add(label);
							label.setSize(300, 300);
							
						}
				}
			}
		});
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				bll_RemoteScreenForm.CloseChatWindow();
				bll_RemoteScreenForm.DisConnectRemote();
				isOpened=false;
			}
			@Override
			public void windowOpened(WindowEvent e) {
				isOpened=true;
				//Goi BLL de mo thread get hinh
				bll_RemoteScreenForm.ConnectRemoteTo(ip, port, pass);
				
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
		
		panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel.addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent arg0) {
				if(chbxMouse.isSelected())
					bll_RemoteScreenForm.SendMouseWheel(arg0);
			}
		});
		panel.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent arg0) {
				if(chbxMouse.isSelected()) {
					float xRatio=(float) (arg0.getX()*1.0/panel.getWidth());
					float yRatio=(float)(arg0.getY()*1.0/panel.getHeight());	
					bll_RemoteScreenForm.SendMouseMove(xRatio, yRatio);
				}
				
			}
			@Override
			public void mouseDragged(MouseEvent e) {
				if(chbxMouse.isSelected()) {
					float xRatio=(float) (e.getX()*1.0/panel.getWidth());
					float yRatio=(float)(e.getY()*1.0/panel.getHeight());	
					bll_RemoteScreenForm.SendMouseMove(xRatio, yRatio);
				}
			}
		});
		panel.addMouseListener(new MouseAdapter() {			
			@Override
			public void mousePressed(MouseEvent e) { //Bat luc nhan xuong
				if(chbxMouse.isSelected())
					bll_RemoteScreenForm.SendClick(e,true);//true=>mouse down
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				if(chbxMouse.isSelected())
					bll_RemoteScreenForm.SendClick(e,false);//false=>mouse up
				//System.out.println("clk");
			}
		});
				
		chbxMouse = new JCheckBox("Mouse");
		chbxMouse.setFocusable(false);
		chbxMouse.setSelected(true);
		chbxMouse.setFont(new Font("Calibri", Font.BOLD, 13));
		
		chbxKeys = new JCheckBox("Keys");
		chbxKeys.setFocusable(false);
		chbxKeys.setSelected(true);
		chbxKeys.setFont(new Font("Calibri", Font.BOLD, 13));
		
		btnReOpenChat = new JButton("ReOpenChat");
		btnReOpenChat.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				bll_RemoteScreenForm.ReOpenChat();
			}
		});
		//panel.addMouse
		//panel.setBackground(Color.RED);
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addComponent(panel, GroupLayout.DEFAULT_SIZE, 891, Short.MAX_VALUE)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap(656, Short.MAX_VALUE)
					.addComponent(btnReOpenChat)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(chbxMouse)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(chbxKeys)
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(panel, GroupLayout.DEFAULT_SIZE, 437, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(chbxKeys)
						.addComponent(chbxMouse)
						.addComponent(btnReOpenChat)))
		);
		
		panel.setLayout(new GridLayout(colRowNum, colRowNum, 0, 0));
		
		for(int i=0;i<colRowNum;i++) 
			for(int j=0;j<colRowNum;j++) {
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
					int index=oip.row*colRowNum+oip.column;
					JLabel label=(JLabel)components[index];
					Image dimg = oip.image.getImage().getScaledInstance(label.getWidth(), label.getHeight(), Image.SCALE_SMOOTH);
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
				int index=lanIIO.row*colRowNum+lanIIO.column;
				JLabel label=(JLabel)components[index];
				Image dimg = lanIIO.image.getImage().getScaledInstance(label.getWidth(), label.getHeight(), Image.SCALE_SMOOTH);
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
