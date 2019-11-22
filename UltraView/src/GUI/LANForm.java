package GUI;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.Console;
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
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LANForm frame = new LANForm();
					frame.setVisible(true);
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
			}
		});
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		setBounds(100, 100, 806, 414);
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setFont(new Font("Tahoma", Font.PLAIN, 15));
		setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		mnFile.setFont(new Font("Tahoma", Font.PLAIN, 15));
		menuBar.add(mnFile);
		contentPane = new JPanel();
		contentPane.setBackground(SystemColor.inactiveCaption);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JPanel statusPanel = new JPanel();
		statusPanel.setBackground(SystemColor.inactiveCaption);
		statusPanel.setBounds(5, 335, 813, 20);
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
		
		JLabel lblNewLabel_2 = new JLabel("Your IP");
		lblNewLabel_2.setAlignmentY(Component.TOP_ALIGNMENT);
		lblNewLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 16));
		
		JLabel lblNewLabel_3 = new JLabel("Your Port");
		lblNewLabel_3.setFont(new Font("Tahoma", Font.PLAIN, 16));
		
		txtYourIP = new JTextField();
		txtYourIP.setFont(new Font("Tahoma", Font.PLAIN, 16));
		txtYourIP.setEditable(false);
		txtYourIP.setColumns(10);
		
		txtYourPort = new JTextField();
		txtYourPort.setFont(new Font("Tahoma", Font.PLAIN, 16));
		txtYourPort.setColumns(10);
		
		JLabel lblNewLabel_4 = new JLabel("Password");
		lblNewLabel_4.setFont(new Font("Tahoma", Font.PLAIN, 16));
		
		txtYourPassword = new JTextField();
		txtYourPassword.setFont(new Font("Tahoma", Font.PLAIN, 16));
		txtYourPassword.setColumns(10);
		
		JButton btnOpenConnect = new JButton("Allow Connection");
		btnOpenConnect.setFocusable(false);
		btnOpenConnect.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnOpenConnect.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if(bll_LANForm.GetIsOpenConnection()==false) {
					try {
						bll_LANForm.OpenConnect(txtYourIP.getText(),txtYourPort.getText(),txtYourPassword.getText());
						btnOpenConnect.setBackground(Color.RED);
						btnOpenConnect.setText("Close Connection");
					}catch (Exception e) {
						System.out.println("Mo ket noi that bai!");
					}
										
				}else {
					try {
						btnOpenConnect.setBackground(new Color(240,240,240));
						btnOpenConnect.setText("Open Connection");
						bll_LANForm.CloseConnect();
						bll_LANForm.CloseChatWindow();
					}catch (Exception e) {
						System.out.println("Server dong ket noi that bai!");
					}
				}
			}
		});
		
		JButton btnOpenchat = new JButton("OpenChat");
		btnOpenchat.setFocusable(false);
		btnOpenchat.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnOpenchat.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				bll_LANForm.ReOpenChat();
			}
		});
		
		JLabel lblNewLabel_1 = new JLabel("<html>Please click \"Allow Connection\" and send your IP, port, password to your partner if you want they to remote your computer.</html>");
		lblNewLabel_1.setBackground(new Color(248, 248, 255));
		lblNewLabel_1.setFont(new Font("Tahoma", Font.ITALIC, 16));
		lblNewLabel_1.setOpaque(true);
		GroupLayout gl_pnlOpenCnn = new GroupLayout(pnlOpenCnn);
		gl_pnlOpenCnn.setHorizontalGroup(
			gl_pnlOpenCnn.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_pnlOpenCnn.createSequentialGroup()
					.addGroup(gl_pnlOpenCnn.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_pnlOpenCnn.createSequentialGroup()
							.addContainerGap()
							.addComponent(lblNewLabel_1, GroupLayout.PREFERRED_SIZE, 337, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_pnlOpenCnn.createSequentialGroup()
							.addGap(31)
							.addGroup(gl_pnlOpenCnn.createParallelGroup(Alignment.TRAILING)
								.addGroup(gl_pnlOpenCnn.createSequentialGroup()
									.addGroup(gl_pnlOpenCnn.createParallelGroup(Alignment.LEADING)
										.addComponent(lblNewLabel_4)
										.addComponent(lblNewLabel_2))
									.addGap(19))
								.addGroup(gl_pnlOpenCnn.createSequentialGroup()
									.addComponent(lblNewLabel_3)
									.addGap(18)))
							.addGroup(gl_pnlOpenCnn.createParallelGroup(Alignment.TRAILING)
								.addComponent(txtYourIP, GroupLayout.DEFAULT_SIZE, 238, Short.MAX_VALUE)
								.addGroup(gl_pnlOpenCnn.createSequentialGroup()
									.addComponent(btnOpenchat, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(btnOpenConnect))
								.addComponent(txtYourPassword, 238, 238, 238)
								.addComponent(txtYourPort, 238, 238, 238))))
					.addGap(28))
		);
		gl_pnlOpenCnn.setVerticalGroup(
			gl_pnlOpenCnn.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_pnlOpenCnn.createSequentialGroup()
					.addGap(5)
					.addComponent(lblNewLabel_1)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_pnlOpenCnn.createParallelGroup(Alignment.BASELINE)
						.addComponent(txtYourIP, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblNewLabel_2))
					.addGap(18)
					.addGroup(gl_pnlOpenCnn.createParallelGroup(Alignment.LEADING)
						.addComponent(txtYourPort, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblNewLabel_3))
					.addGap(13)
					.addGroup(gl_pnlOpenCnn.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel_4)
						.addComponent(txtYourPassword, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_pnlOpenCnn.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnOpenConnect)
						.addComponent(btnOpenchat))
					.addContainerGap())
		);
		pnlOpenCnn.setLayout(gl_pnlOpenCnn);
		
		JPanel pnlRemoteForm = new JPanel();
		pnlRemoteForm.setBackground(new Color(248, 248, 255));
		pnlRemoteForm.setBorder(new MatteBorder(0, 1, 0, 0, (Color) new Color(0, 0, 0)));
		
		JLabel lblNewLabel_7 = new JLabel("Partner IP");
		lblNewLabel_7.setBounds(13, 84, 71, 20);
		lblNewLabel_7.setAlignmentY(Component.TOP_ALIGNMENT);
		lblNewLabel_7.setFont(new Font("Tahoma", Font.PLAIN, 16));
		
		JLabel lblNewLabel_8 = new JLabel("Partner Port");
		lblNewLabel_8.setBounds(13, 124, 85, 20);
		lblNewLabel_8.setAlignmentY(Component.TOP_ALIGNMENT);
		lblNewLabel_8.setFont(new Font("Tahoma", Font.PLAIN, 16));
		
		JLabel lblNewLabel_9 = new JLabel("Password");
		lblNewLabel_9.setBounds(13, 164, 67, 20);
		lblNewLabel_9.setFont(new Font("Tahoma", Font.PLAIN, 16));
		
		txtPartnerIP = new JTextField();
		txtPartnerIP.setBounds(118, 81, 216, 26);
		txtPartnerIP.setAlignmentX(Component.LEFT_ALIGNMENT);
		txtPartnerIP.setAlignmentY(Component.TOP_ALIGNMENT);
		txtPartnerIP.setFont(new Font("Tahoma", Font.PLAIN, 16));
		txtPartnerIP.setColumns(10);
		
		txtPartnerPort = new JTextField();
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
		
		JButton btnConnect = new JButton("Start Remote");
		btnConnect.setBounds(192, 200, 142, 29);
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
		
		JLabel lblNewLabel_6 = new JLabel("<html>Please enter IP, port, password of the computer that you want to remote.</html>");
		lblNewLabel_6.setBackground(new Color(248, 248, 255));
		lblNewLabel_6.setBounds(43, 13, 284, 40);
		lblNewLabel_6.setFont(new Font("Tahoma", Font.ITALIC, 16));
		lblNewLabel_6.setVerticalTextPosition(SwingConstants.TOP);
		lblNewLabel_6.setOpaque(true);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(SystemColor.inactiveCaption);
		panel_1.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		
		JLabel lblNewLabel = new JLabel("Allow Connection");
		panel_1.add(lblNewLabel);
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 20));
		
		JPanel panel_2 = new JPanel();
		panel_2.setBackground(SystemColor.inactiveCaption);
		panel_2.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		
		JLabel lblNewLabel_5 = new JLabel("Remote Another");
		panel_2.add(lblNewLabel_5);
		lblNewLabel_5.setFont(new Font("Tahoma", Font.BOLD, 20));
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
		pnlRemoteForm.add(lblNewLabel_6);
		pnlRemoteForm.add(lblNewLabel_9);
		pnlRemoteForm.add(lblNewLabel_7);
		pnlRemoteForm.add(lblNewLabel_8);
		pnlRemoteForm.add(txtPartnerIP);
		pnlRemoteForm.add(txtPartnerPort);
		pnlRemoteForm.add(txtPartnerPassword);
		pnlRemoteForm.add(btnConnect);
		panel.setLayout(gl_panel);
		
		JLabel lblStatus = new JLabel("Status: ...");
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
}
