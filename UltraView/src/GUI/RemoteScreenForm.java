package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.Image;

import javax.imageio.ImageIO;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
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
import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;
import java.beans.PropertyChangeEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.UIManager;
import java.awt.SystemColor;
import java.awt.FlowLayout;

public class RemoteScreenForm extends JFrame {
	public static void main(String[] args) {
		OpenForm("192.168.1.135", "1999", "ahihi",1);
	}
	//STATIC
	//Check trang thai form chi mo 1 lan 1 form
	public static RemoteScreenForm instance;
	public static boolean isOpened=false;
	public static void OpenForm(String ip, String port, String pass,int language) {
		if(isOpened)
			return;
		else {
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					try {
						instance = new RemoteScreenForm(ip, port, pass,language);
						instance.setVisible(true);
						instance.SetLanguage(language);
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
	private JPanel panel_1;
	private JLabel lblStatus;
	public RemoteScreenForm(String ip, String port, String pass,int language) {
		setFont(new Font("Tahoma", Font.PLAIN, 16));
		setTitle("Remote screen");
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

				Locale locale=null;
				switch (language) {
				case 0://en
					locale = new Locale("en");
					break;
				case 1://vi
					locale = new Locale("vi");
					break;
				default:
					locale = new Locale("en");
					break;
				}
				lg=ResourceBundle.getBundle("internationalization.message.language", locale);
				ShowStatus(lg.getString("sttConnectingTo")+ip+":"+port+" "+lg.getString("sttPass")+": "+pass+"..." );
				
				try {
					bll_RemoteScreenForm.ConnectRemoteTo(ip, port, pass);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					dispose();
				}
				
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
		chbxMouse.setToolTipText("Enable/Disable mouse remote.");
		chbxMouse.setFocusable(false);
		chbxMouse.setSelected(true);
		chbxMouse.setFont(new Font("Tahoma", Font.PLAIN, 15));
		
		chbxKeys = new JCheckBox("Keys");
		chbxKeys.setToolTipText("Ebable/Disable keys remote.");
		chbxKeys.setFocusable(false);
		chbxKeys.setSelected(true);
		chbxKeys.setFont(new Font("Tahoma", Font.PLAIN, 15));
		
		btnReOpenChat = new JButton("");
		btnReOpenChat.setFocusable(false);
		btnReOpenChat.setBackground(SystemColor.control);
		btnReOpenChat.setToolTipText("ReOpen Chat");
		try {
			btnReOpenChat.setIcon(new ImageIcon(ImageIO.read(new File("./resource/chatIcon.png"))));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			System.out.println("Can not load icon!");
		}
		btnReOpenChat.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				bll_RemoteScreenForm.ReOpenChat();
			}
		});
		
		panel_1 = new JPanel();
		//panel.addMouse
		//panel.setBackground(Color.RED);
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, 483, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 216, Short.MAX_VALUE)
					.addComponent(btnReOpenChat, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(chbxMouse)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(chbxKeys)
					.addContainerGap())
				.addComponent(panel, GroupLayout.DEFAULT_SIZE, 891, Short.MAX_VALUE)
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(panel, GroupLayout.DEFAULT_SIZE, 428, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
							.addComponent(chbxKeys)
							.addComponent(chbxMouse))
						.addComponent(btnReOpenChat, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
						.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, 16, GroupLayout.PREFERRED_SIZE)))
		);
		
		lblStatus = new JLabel("Status:...");
		lblStatus.setFont(new Font("Tahoma", Font.PLAIN, 14));
		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1.setHorizontalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addComponent(lblStatus)
					.addContainerGap(427, Short.MAX_VALUE))
		);
		gl_panel_1.setVerticalGroup(
			gl_panel_1.createParallelGroup(Alignment.TRAILING)
				.addGroup(Alignment.LEADING, gl_panel_1.createSequentialGroup()
					.addComponent(lblStatus)
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		panel_1.setLayout(gl_panel_1);
		
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
	
	public void ShowStatus(String status) {
		lblStatus.setText("Status: "+status);
	}
	
	public void ShowMessage(String message, String tile,int type) {
		JOptionPane.showMessageDialog(this, message, tile, type);
	}

	private ResourceBundle lg=null;
	public void SetLanguage(int language) {
		Locale locale=null;
		switch (language) {
		case 0://en
			locale = new Locale("en");
			break;
		case 1://vi
			locale = new Locale("vi");
			break;
		default:
			locale = new Locale("en");
			break;
		}
		lg = ResourceBundle.getBundle("internationalization.message.language", locale);
		chbxMouse.setText(lg.getString("chbxMouse"));
		chbxKeys.setText(lg.getString("chbxKeys"));
		this.setTitle(lg.getString("frmRemoteScreen"));
	}
	
	public String GetLanguageString(String key) {
		if(lg==null)
			lg = ResourceBundle.getBundle("internationalization.message.language", new Locale("en"));
		return lg.getString(key);
	}
	
}
