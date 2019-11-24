package GUI;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.Console;
import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JTabbedPane;
import javax.swing.JSplitPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.border.MatteBorder;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import BLL.BLL_LANForm;
import BLL.BLL_LANServerChat;

import javax.swing.JCheckBox;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Component;
import java.awt.Font;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.SoftBevelBorder;
import java.awt.SystemColor;
import java.awt.Window.Type;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

public class LANForm extends JFrame {

	private JPanel contentPane;

	public static boolean isOpened=false;
	private JTextField txtYourIP;
	private JTextField txtYourPort;
	private JTextField txtYourPassword;
	private JTextField txtPartnerIP;
	private JTextField txtPartnerPort;
	private JTextField txtPartnerPassword;
	private BLL_LANForm bll_LANForm;
	private JLabel lblStatus;
	private JLabel lblAllowConnection;
	private JLabel lblRemoteAnother;
	private JLabel lblInfoAllowConnection;
	private JLabel lblInfoRemoteAnother;
	private JLabel lblYourIP;
	private JLabel lblYourPort;
	private JLabel lblYourPassword;
	private JButton btnOpenConnect;
	private JLabel lblPartnerIP;
	private JLabel lblPartnerPort;
	private JLabel lblPartnerPassword;
	private JButton btnConnect;
	private JMenu mnFile;
	private JMenuItem mntmClose;
	private JMenu mnSetting;
	private JMenu mnLanguage;
	private JMenuItem mntmEnglish;
	private JMenuItem mntmVietnamese;
	private JMenu mnHelp;
	private JMenuItem mntmManual;
	private JMenuItem mntmAbout;
	private JButton btnOpenchat;
	
	private String msgPortInvalid="";
	private String msgOpenConnectFailed="";
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					//LANForm frame = new LANForm();
					//frame.setVisible(true);
					LANForm.OpenForm(null);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	
	public static void OpenForm(String[]args) {
		if(isOpened)
			return;
		else {
			isOpened=true;
			instance=new LANForm();
			instance.setVisible(true);
			//main(args);
		}
	}
	
	private static LANForm instance=null;
	public static LANForm GetInstance() {
		return instance;
	}
	public static void RemoveInstance(){
		instance=null;
	}
	
	/**
	 * Create the frame.
	 */
	public LANForm() {
		setResizable(false);
		setTitle("Ultra View");
		setBackground(Color.LIGHT_GRAY);
		setForeground(new Color(191, 205, 219));
		bll_LANForm=new BLL_LANForm();
		//Event
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				isOpened=false;
				//bll_LANForm.CloseChatWindow();
				bll_LANForm.CloseConnect();
				try {
					bll_LANForm.CloseChatWindow();
				} catch (Exception e2) {
					// TODO: handle exception
				}
				instance=null;
			}
			@Override
			public void windowOpened(WindowEvent e) {
				txtYourIP.setText(BLL_LANForm.GetMyIPv4());
				txtYourPort.setText(BLL_LANForm.GetMyPort());
				txtYourPassword.setText(BLL_LANForm.RandromPassword());
				
				SetLanguage(1);
			}
		});
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		setBounds(100, 100, 806, 414);
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setFont(new Font("Tahoma", Font.PLAIN, 15));
		setJMenuBar(menuBar);
		
		mnFile = new JMenu("File");
		mnFile.setFont(new Font("Tahoma", Font.PLAIN, 15));
		menuBar.add(mnFile);
		
		mntmClose = new JMenuItem("Close");
		mntmClose.setFont(new Font("Tahoma", Font.PLAIN, 15));
		mntmClose.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				System.out.println("Click close");
				try {
					bll_LANForm.CloseConnect();
				} catch (Exception ex) { }
				
				try {
					bll_LANForm.CloseChatWindow();
				} catch (Exception ex) { }
				dispose();
			}
		});
		mnFile.add(mntmClose);
		
		
		mnSetting = new JMenu("Setting");
		mnSetting.setBackground(Color.WHITE);
		mnSetting.setFont(new Font("Tahoma", Font.PLAIN, 15));
		menuBar.add(mnSetting);
		
		mnLanguage = new JMenu("Language");
		mnLanguage.setBackground(Color.WHITE);
		mnLanguage.setFont(new Font("Tahoma", Font.PLAIN, 15));
		mnSetting.add(mnLanguage);
		
		mntmEnglish = new JMenuItem("English");
		mntmEnglish.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				//Tieng anh
				System.out.println("Tieng Anh");
				SetLanguage(0);
			}
		});
		mntmEnglish.setBackground(Color.WHITE);
		mntmEnglish.setFont(new Font("Tahoma", Font.PLAIN, 15));
		mnLanguage.add(mntmEnglish);
		
		mntmVietnamese = new JMenuItem("Ti\u1EBFng Vi\u1EC7t");
		mntmVietnamese.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				System.out.println("Tieng Viet");
				SetLanguage(1);
			}
		});
		mntmVietnamese.setBackground(Color.WHITE);
		mntmVietnamese.setFont(new Font("Tahoma", Font.PLAIN, 15));
		mnLanguage.add(mntmVietnamese);
		
				
		mnHelp = new JMenu("Help");
		mnHelp.setBackground(Color.WHITE);
		mnHelp.setFont(new Font("Tahoma", Font.PLAIN, 15));
		menuBar.add(mnHelp);
		
		mntmManual = new JMenuItem("Manual");
		mntmManual.setFont(new Font("Tahoma", Font.PLAIN, 15));
		mntmManual.setBackground(Color.WHITE);
		mnHelp.add(mntmManual);
		
		mntmAbout = new JMenuItem("About");
		mntmAbout.setFont(new Font("Tahoma", Font.PLAIN, 15));
		mntmAbout.setBackground(Color.WHITE);
		mnHelp.add(mntmAbout);
		contentPane = new JPanel();
		contentPane.setBackground(SystemColor.inactiveCaption);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JPanel statusPanel = new JPanel();
		statusPanel.setBackground(SystemColor.inactiveCaption);
		statusPanel.setBounds(5, 335, 789, 20);
		statusPanel.setFocusable(false);
		statusPanel.setAlignmentY(0.0f);
		statusPanel.setAlignmentX(0.0f);
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(248, 248, 255));
		panel.setBounds(5, 5, 789, 329);
		panel.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		
		JPanel pnlOpenCnn = new JPanel();
		pnlOpenCnn.setBackground(new Color(248, 248, 255));
		pnlOpenCnn.setBorder(new MatteBorder(0, 0, 0, 0, (Color) new Color(0, 0, 0)));
		
		lblYourIP = new JLabel("Your IP");
		lblYourIP.setAlignmentY(Component.TOP_ALIGNMENT);
		lblYourIP.setFont(new Font("Tahoma", Font.PLAIN, 16));
		
		lblYourPort = new JLabel("Your Port");
		lblYourPort.setFont(new Font("Tahoma", Font.PLAIN, 16));
		
		txtYourIP = new JTextField();
		
		txtYourIP.setFont(new Font("Tahoma", Font.PLAIN, 16));
		txtYourIP.setEditable(false);
		txtYourIP.setColumns(10);
		
		txtYourPort = new JTextField();
		txtYourPort.setText("1999");
		txtYourPort.setFont(new Font("Tahoma", Font.PLAIN, 16));
		txtYourPort.setColumns(10);
		
		lblYourPassword = new JLabel("Password");
		lblYourPassword.setFont(new Font("Tahoma", Font.PLAIN, 16));
		
		txtYourPassword = new JTextField();
		txtYourPassword.setFont(new Font("Tahoma", Font.PLAIN, 16));
		txtYourPassword.setColumns(10);
		
		btnOpenConnect = new JButton("Allow Connection");
		btnOpenConnect.setBackground(SystemColor.control);
		btnOpenConnect.setFocusable(false);
		btnOpenConnect.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnOpenConnect.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if(bll_LANForm.GetIsOpenConnection()==false) {
					try {
						bll_LANForm.OpenConnect(txtYourIP.getText(),txtYourPort.getText(),txtYourPassword.getText());
						btnOpenConnect.setBackground(Color.RED);
						btnOpenConnect.setText(GetLanguageString("lblCloseConnection"));
						txtYourPort.setEditable(false);
						txtYourPassword.setEditable(false);
					}catch (Exception e) {
						if (e.getMessage().equals("Port khong hop le!")) {
							ShowMessage(msgPortInvalid, "Port problem", JOptionPane.ERROR_MESSAGE);
						}
						else {
							ShowMessage(msgOpenConnectFailed+txtYourIP.getText()+":"+txtYourPort.getText(), "Open connection failed", JOptionPane.ERROR_MESSAGE);
						}
						System.out.println("Mo ket noi that bai!");
						ShowStatus(GetLanguageString("sttOpenConnectFailed"));
					}
										
				}else {
					try {
						btnOpenConnect.setBackground(new Color(240,240,240));
						btnOpenConnect.setText(GetLanguageString("lblAllowConnection"));
						txtYourPort.setEditable(true);
						txtYourPassword.setEditable(true);
						bll_LANForm.CloseConnect();
						ShowStatus(GetLanguageString("sttCloseConnection"));
						
					}catch (Exception e) {
						System.out.println("Server dong ket noi that bai!");
					}
					try {
						bll_LANForm.CloseChatWindow();
					} catch (Exception e) {
					}
				}
			}
		});
		
		btnOpenchat = new JButton("");
		btnOpenchat.setBorder(null);
		btnOpenchat.setBackground(new Color(248, 248, 255));
		try {
			btnOpenchat.setIcon(new ImageIcon(ImageIO.read(new File("./resource/chatIcon.png"))));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			System.out.println("Can not load icon!");
		}
		btnOpenchat.setFocusable(false);
		btnOpenchat.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnOpenchat.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				bll_LANForm.ReOpenChat();
			}
		});
		
		lblInfoAllowConnection = new JLabel("<html>Please click \"Allow Connection\" and send your IP, port, password to your partner if you want they to remote your computer.</html>");
		lblInfoAllowConnection.setBackground(new Color(248, 248, 255));
		lblInfoAllowConnection.setFont(new Font("Tahoma", Font.ITALIC, 16));
		lblInfoAllowConnection.setOpaque(true);
		GroupLayout gl_pnlOpenCnn = new GroupLayout(pnlOpenCnn);
		gl_pnlOpenCnn.setHorizontalGroup(
			gl_pnlOpenCnn.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_pnlOpenCnn.createSequentialGroup()
					.addGroup(gl_pnlOpenCnn.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_pnlOpenCnn.createSequentialGroup()
							.addContainerGap()
							.addComponent(lblInfoAllowConnection, GroupLayout.PREFERRED_SIZE, 337, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_pnlOpenCnn.createSequentialGroup()
							.addGap(31)
							.addGroup(gl_pnlOpenCnn.createParallelGroup(Alignment.TRAILING)
								.addGroup(gl_pnlOpenCnn.createSequentialGroup()
									.addGroup(gl_pnlOpenCnn.createParallelGroup(Alignment.LEADING)
										.addComponent(lblYourPassword)
										.addComponent(lblYourIP))
									.addGap(19))
								.addGroup(gl_pnlOpenCnn.createSequentialGroup()
									.addComponent(lblYourPort)
									.addGap(18)))
							.addGroup(gl_pnlOpenCnn.createParallelGroup(Alignment.LEADING)
								.addComponent(txtYourIP, GroupLayout.DEFAULT_SIZE, 238, Short.MAX_VALUE)
								.addGroup(Alignment.TRAILING, gl_pnlOpenCnn.createSequentialGroup()
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(btnOpenchat, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(btnOpenConnect))
								.addComponent(txtYourPassword, 238, 238, Short.MAX_VALUE)
								.addGroup(gl_pnlOpenCnn.createSequentialGroup()
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(txtYourPort, GroupLayout.DEFAULT_SIZE, 238, Short.MAX_VALUE)))))
					.addGap(28))
		);
		gl_pnlOpenCnn.setVerticalGroup(
			gl_pnlOpenCnn.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_pnlOpenCnn.createSequentialGroup()
					.addGap(5)
					.addComponent(lblInfoAllowConnection)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_pnlOpenCnn.createParallelGroup(Alignment.BASELINE)
						.addComponent(txtYourIP, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblYourIP))
					.addGap(18)
					.addGroup(gl_pnlOpenCnn.createParallelGroup(Alignment.LEADING)
						.addComponent(lblYourPort)
						.addComponent(txtYourPort, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(13)
					.addGroup(gl_pnlOpenCnn.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblYourPassword)
						.addComponent(txtYourPassword, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_pnlOpenCnn.createParallelGroup(Alignment.LEADING)
						.addComponent(btnOpenConnect)
						.addComponent(btnOpenchat, GroupLayout.PREFERRED_SIZE, 29, GroupLayout.PREFERRED_SIZE))
					.addGap(17))
		);
		pnlOpenCnn.setLayout(gl_pnlOpenCnn);
		
		JPanel pnlRemoteForm = new JPanel();
		pnlRemoteForm.setBackground(new Color(248, 248, 255));
		pnlRemoteForm.setBorder(new MatteBorder(0, 1, 0, 0, (Color) new Color(0, 0, 0)));
		
		lblPartnerIP = new JLabel("Partner IP");
		lblPartnerIP.setBounds(13, 84, 71, 20);
		lblPartnerIP.setAlignmentY(Component.TOP_ALIGNMENT);
		lblPartnerIP.setFont(new Font("Tahoma", Font.PLAIN, 16));
		
		lblPartnerPort = new JLabel("Partner Port");
		lblPartnerPort.setBounds(13, 124, 85, 20);
		lblPartnerPort.setAlignmentY(Component.TOP_ALIGNMENT);
		lblPartnerPort.setFont(new Font("Tahoma", Font.PLAIN, 16));
		
		lblPartnerPassword = new JLabel("Password");
		lblPartnerPassword.setBounds(13, 164, 67, 20);
		lblPartnerPassword.setFont(new Font("Tahoma", Font.PLAIN, 16));
		
		txtPartnerIP = new JTextField();
		txtPartnerIP.setBounds(118, 81, 216, 26);
		txtPartnerIP.setAlignmentX(Component.LEFT_ALIGNMENT);
		txtPartnerIP.setAlignmentY(Component.TOP_ALIGNMENT);
		txtPartnerIP.setFont(new Font("Tahoma", Font.PLAIN, 16));
		txtPartnerIP.setColumns(10);
		
		txtPartnerPort = new JTextField();
		txtPartnerPort.setText("1999");
		txtPartnerPort.setBounds(118, 121, 216, 26);
		txtPartnerPort.setAlignmentX(Component.LEFT_ALIGNMENT);
		txtPartnerPort.setAlignmentY(Component.BOTTOM_ALIGNMENT);
		txtPartnerPort.setFont(new Font("Tahoma", Font.PLAIN, 16));
		txtPartnerPort.setColumns(10);
		
		txtPartnerPassword = new JTextField();
		txtPartnerPassword.setBounds(119, 161, 216, 26);
		txtPartnerPassword.setAlignmentY(Component.TOP_ALIGNMENT);
		txtPartnerPassword.setAlignmentX(Component.LEFT_ALIGNMENT);
		txtPartnerPassword.setFont(new Font("Tahoma", Font.PLAIN, 16));
		txtPartnerPassword.setColumns(10);
		
		btnConnect = new JButton("Start Remote");
		btnConnect.setBackground(SystemColor.control);
		btnConnect.setBounds(168, 200, 166, 29);
		btnConnect.setAlignmentX(Component.RIGHT_ALIGNMENT);
		btnConnect.setAlignmentY(Component.BOTTOM_ALIGNMENT);
		btnConnect.setFocusable(false);
		btnConnect.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnConnect.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				bll_LANForm.ConnectAndShowRemoteForm(txtPartnerIP.getText(),txtPartnerPort.getText(),txtPartnerPassword.getText());
			}
		});
		
		lblInfoRemoteAnother = new JLabel("<html>Please enter IP, port, password of the computer that you want to remote.</html>");
		lblInfoRemoteAnother.setBackground(new Color(248, 248, 255));
		lblInfoRemoteAnother.setBounds(43, 13, 284, 40);
		lblInfoRemoteAnother.setFont(new Font("Tahoma", Font.ITALIC, 16));
		lblInfoRemoteAnother.setVerticalTextPosition(SwingConstants.TOP);
		lblInfoRemoteAnother.setOpaque(true);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(SystemColor.inactiveCaption);
		panel_1.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		
		lblAllowConnection = new JLabel("Allow Connection");
		panel_1.add(lblAllowConnection);
		lblAllowConnection.setFont(new Font("Tahoma", Font.BOLD, 20));
		
		JPanel panel_2 = new JPanel();
		panel_2.setBackground(SystemColor.inactiveCaption);
		panel_2.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		
		lblRemoteAnother = new JLabel("Remote Another");
		panel_2.add(lblRemoteAnother);
		lblRemoteAnother.setFont(new Font("Tahoma", Font.BOLD, 20));
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING, false)
						.addComponent(panel_1, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(pnlOpenCnn, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 383, Short.MAX_VALUE))
					.addGap(18)
					.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
						.addComponent(pnlRemoteForm, GroupLayout.PREFERRED_SIZE, 352, GroupLayout.PREFERRED_SIZE)
						.addComponent(panel_2, GroupLayout.PREFERRED_SIZE, 342, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(panel_2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
						.addComponent(pnlOpenCnn, GroupLayout.DEFAULT_SIZE, 257, Short.MAX_VALUE)
						.addComponent(pnlRemoteForm, GroupLayout.DEFAULT_SIZE, 257, Short.MAX_VALUE))
					.addContainerGap())
		);
		pnlRemoteForm.setLayout(null);
		pnlRemoteForm.add(lblInfoRemoteAnother);
		pnlRemoteForm.add(lblPartnerPassword);
		pnlRemoteForm.add(lblPartnerIP);
		pnlRemoteForm.add(lblPartnerPort);
		pnlRemoteForm.add(txtPartnerIP);
		pnlRemoteForm.add(txtPartnerPort);
		pnlRemoteForm.add(txtPartnerPassword);
		pnlRemoteForm.add(btnConnect);
		panel.setLayout(gl_panel);
		
		lblStatus = new JLabel("Status: ...");
		lblStatus.setFont(new Font("Tahoma", Font.PLAIN, 14));
		GroupLayout gl_statusPanel = new GroupLayout(statusPanel);
		gl_statusPanel.setHorizontalGroup(
			gl_statusPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_statusPanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblStatus)
					.addContainerGap(748, Short.MAX_VALUE))
		);
		gl_statusPanel.setVerticalGroup(
			gl_statusPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_statusPanel.createSequentialGroup()
					.addComponent(lblStatus)
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		statusPanel.setLayout(gl_statusPanel);
		contentPane.setLayout(null);
		contentPane.add(panel);
		contentPane.add(statusPanel);
	}
	
	
	public void ShowStatus(String status) {
		lblStatus.setText("Status: "+status);
	}
	
	public void ShowMessage(String message, String tile,int type) {
		JOptionPane.showMessageDialog(instance, message, tile, type);
	}
	
	
	private ResourceBundle lg=null;
	
	public String GetLanguageString(String key) {
		return lg.getString(key);
	}
	
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
		lblAllowConnection.setText(lg.getString("lblAllowConnection"));
		lblRemoteAnother.setText(lg.getString("lblRemoteAnother"));
		lblInfoAllowConnection.setText(lg.getString("lblInfoAllowConnection"));
		lblInfoRemoteAnother.setText(lg.getString("lblInfoRemoteAnother"));
		lblYourIP.setText(lg.getString("lblYourIP"));
		lblYourPort.setText(lg.getString("lblYourPort"));
		lblYourPassword.setText(lg.getString("lblYourPassword"));
		btnOpenConnect.setText(lg.getString("btnOpenConnect"));
		lblPartnerIP.setText(lg.getString("lblPartnerIP"));
		lblPartnerPort.setText(lg.getString("lblPartnerPort"));
		lblPartnerPassword.setText(lg.getString("lblPartnerPassword"));
		btnConnect.setText(lg.getString("btnConnect"));
		mnFile.setText(lg.getString("mnFile"));
		mntmClose.setText(lg.getString("mntmClose"));
		mnSetting.setText(lg.getString("mnSetting"));
		mnLanguage.setText(lg.getString("mnLanguage"));
		mnHelp.setText(lg.getString("mnHelp"));
		mntmManual.setText(lg.getString("mntmManual"));
		mntmAbout.setText(lg.getString("mntmAbout"));
		
		txtYourIP.setToolTipText(lg.getString("tipYourIP"));
		txtYourPort.setToolTipText(lg.getString("tipYourPort"));
		txtYourPassword.setToolTipText(lg.getString("tipYourPassword"));
		btnOpenConnect.setToolTipText(lg.getString("tipAllowConnection"));
		txtPartnerIP.setToolTipText(lg.getString("tipPartnerIP"));
		btnOpenchat.setToolTipText(lg.getString("tipOpenChat"));
		txtPartnerPort.setToolTipText(lg.getString("tipPartnerPort"));
		txtPartnerPassword.setToolTipText(lg.getString("tipPartnerPassword"));
		btnConnect.setToolTipText(lg.getString("tipStartRemote"));
		
		msgOpenConnectFailed=lg.getString("msgOpenConnectFailed");
		msgPortInvalid=lg.getString("msgPortInvalid");
	}
}
