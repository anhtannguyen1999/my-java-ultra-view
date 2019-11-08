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
import javax.swing.border.MatteBorder;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import BLL.BLL_LANForm;

public class LANForm extends JFrame {

	private JPanel contentPane;

	public static boolean isOpened=false;
	private JTextField txtYourIP;
	private JTextField txtYourPort;
	private JTextField txtYourPassword;
	private JTextField txtPartnerIP;
	private JTextField txtPartnerPort;
	private JTextField txtPartnerPassword;
	
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
			main(args);
		}
	}
	
	
	/**
	 * Create the frame.
	 */
	public LANForm() {
		//Event
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				isOpened=false;
			}
			@Override
			public void windowOpened(WindowEvent e) {
				txtYourIP.setText(BLL_LANForm.GetMyIPv4());
				txtYourPort.setText(BLL_LANForm.GetMyPort());
				txtYourPassword.setText(BLL_LANForm.RandromPassword());
			}
		});
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		setBounds(100, 100, 650, 400);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JPanel pnlOpenCnn = new JPanel();
		pnlOpenCnn.setBorder(new MatteBorder(0, 0, 0, 1, (Color) new Color(0, 0, 0)));
		
		JPanel pnlRemoteForm = new JPanel();
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(pnlOpenCnn, GroupLayout.PREFERRED_SIZE, 318, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(pnlRemoteForm, GroupLayout.DEFAULT_SIZE, 308, Short.MAX_VALUE))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(pnlOpenCnn, GroupLayout.PREFERRED_SIZE, 287, GroupLayout.PREFERRED_SIZE)
						.addComponent(pnlRemoteForm, GroupLayout.PREFERRED_SIZE, 286, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(17, Short.MAX_VALUE))
		);
		
		JLabel lblNewLabel_5 = new JLabel("Remote PC");
		
		JLabel lblNewLabel_6 = new JLabel("New label");
		
		JLabel lblNewLabel_7 = new JLabel("Partner IP");
		
		JLabel lblNewLabel_8 = new JLabel("Partner Port");
		
		JLabel lblNewLabel_9 = new JLabel("Password");
		
		txtPartnerIP = new JTextField();
		txtPartnerIP.setColumns(10);
		
		txtPartnerPort = new JTextField();
		txtPartnerPort.setColumns(10);
		
		txtPartnerPassword = new JTextField();
		txtPartnerPassword.setColumns(10);
		
		JButton btnConnect = new JButton("Connect");
		btnConnect.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				BLL_LANForm.ConnectAndShowRemoteForm(txtPartnerIP.getText(),txtPartnerPort.getText(),txtPartnerPassword.getText());
			}
		});
		GroupLayout gl_pnlRemoteForm = new GroupLayout(pnlRemoteForm);
		gl_pnlRemoteForm.setHorizontalGroup(
			gl_pnlRemoteForm.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_pnlRemoteForm.createSequentialGroup()
					.addGroup(gl_pnlRemoteForm.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_pnlRemoteForm.createSequentialGroup()
							.addGap(117)
							.addGroup(gl_pnlRemoteForm.createParallelGroup(Alignment.LEADING)
								.addComponent(lblNewLabel_6)
								.addComponent(lblNewLabel_5)))
						.addGroup(gl_pnlRemoteForm.createSequentialGroup()
							.addContainerGap()
							.addGroup(gl_pnlRemoteForm.createParallelGroup(Alignment.LEADING)
								.addComponent(lblNewLabel_7)
								.addComponent(lblNewLabel_8)
								.addComponent(lblNewLabel_9))
							.addGap(37)
							.addGroup(gl_pnlRemoteForm.createParallelGroup(Alignment.LEADING, false)
								.addComponent(txtPartnerPassword)
								.addComponent(btnConnect, Alignment.TRAILING)
								.addComponent(txtPartnerIP, GroupLayout.DEFAULT_SIZE, 145, Short.MAX_VALUE)
								.addComponent(txtPartnerPort))))
					.addContainerGap(34, Short.MAX_VALUE))
		);
		gl_pnlRemoteForm.setVerticalGroup(
			gl_pnlRemoteForm.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_pnlRemoteForm.createSequentialGroup()
					.addComponent(lblNewLabel_5)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblNewLabel_6)
					.addGap(40)
					.addGroup(gl_pnlRemoteForm.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel_7)
						.addComponent(txtPartnerIP, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_pnlRemoteForm.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel_8)
						.addComponent(txtPartnerPort, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGroup(gl_pnlRemoteForm.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_pnlRemoteForm.createSequentialGroup()
							.addGap(61)
							.addComponent(btnConnect))
						.addGroup(gl_pnlRemoteForm.createSequentialGroup()
							.addGap(18)
							.addGroup(gl_pnlRemoteForm.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblNewLabel_9)
								.addComponent(txtPartnerPassword, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))))
					.addContainerGap(59, Short.MAX_VALUE))
		);
		pnlRemoteForm.setLayout(gl_pnlRemoteForm);
		
		JLabel lblNewLabel = new JLabel("OpenConnect");
		
		JLabel lblNewLabel_1 = new JLabel("M\u00F4 t\u1EA3");
		
		JLabel lblNewLabel_2 = new JLabel("Your IP");
		
		JLabel lblNewLabel_3 = new JLabel("Your Port");
		
		txtYourIP = new JTextField();
		txtYourIP.setEditable(false);
		txtYourIP.setColumns(10);
		
		txtYourPort = new JTextField();
		txtYourPort.setColumns(10);
		
		JLabel lblNewLabel_4 = new JLabel("Password");
		
		txtYourPassword = new JTextField();
		txtYourPassword.setColumns(10);
		
		JButton btnOpenConnect = new JButton("Open Connect");
		btnOpenConnect.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				BLL_LANForm.OpenConnect(txtYourIP.getText(),txtYourPort.getText(),txtYourPassword.getText());
			}
		});
		GroupLayout gl_pnlOpenCnn = new GroupLayout(pnlOpenCnn);
		gl_pnlOpenCnn.setHorizontalGroup(
			gl_pnlOpenCnn.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_pnlOpenCnn.createSequentialGroup()
					.addGroup(gl_pnlOpenCnn.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_pnlOpenCnn.createSequentialGroup()
							.addGap(116)
							.addGroup(gl_pnlOpenCnn.createParallelGroup(Alignment.LEADING)
								.addComponent(lblNewLabel_1)
								.addComponent(lblNewLabel)))
						.addGroup(gl_pnlOpenCnn.createSequentialGroup()
							.addGap(21)
							.addGroup(gl_pnlOpenCnn.createParallelGroup(Alignment.LEADING)
								.addComponent(btnOpenConnect, Alignment.TRAILING)
								.addGroup(Alignment.TRAILING, gl_pnlOpenCnn.createSequentialGroup()
									.addGroup(gl_pnlOpenCnn.createParallelGroup(Alignment.LEADING)
										.addComponent(lblNewLabel_3)
										.addComponent(lblNewLabel_2)
										.addComponent(lblNewLabel_4))
									.addGap(18)
									.addGroup(gl_pnlOpenCnn.createParallelGroup(Alignment.TRAILING)
										.addComponent(txtYourPassword, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 149, Short.MAX_VALUE)
										.addComponent(txtYourIP, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 149, Short.MAX_VALUE)
										.addComponent(txtYourPort, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 149, Short.MAX_VALUE))
									.addPreferredGap(ComponentPlacement.RELATED)))))
					.addContainerGap(60, GroupLayout.PREFERRED_SIZE))
		);
		gl_pnlOpenCnn.setVerticalGroup(
			gl_pnlOpenCnn.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_pnlOpenCnn.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblNewLabel)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(lblNewLabel_1)
					.addGap(18)
					.addGroup(gl_pnlOpenCnn.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel_2)
						.addComponent(txtYourIP, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_pnlOpenCnn.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel_3)
						.addComponent(txtYourPort, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_pnlOpenCnn.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel_4)
						.addComponent(txtYourPassword, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addComponent(btnOpenConnect)
					.addContainerGap(71, Short.MAX_VALUE))
		);
		pnlOpenCnn.setLayout(gl_pnlOpenCnn);
		contentPane.setLayout(gl_contentPane);
	}

}
