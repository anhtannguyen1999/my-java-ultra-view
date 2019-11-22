package GUI;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import BLL.BLL_LANClientChat;
import BLL.BLL_LANServerChat;

import javax.annotation.Resource;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JEditorPane;
import javax.swing.JList;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JFormattedTextField;
import java.awt.Color;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JButton;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.Font;
import java.awt.Insets;
import javax.swing.JToolBar;
import javax.swing.JCheckBox;
import javax.swing.SwingConstants;
import java.awt.Component;
import javax.swing.ImageIcon;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

public abstract class ChatForm extends JFrame {

	protected JPanel contentPane;
	protected JTextArea txtChatBox;	
	protected JButton btnSend;
	protected JTextArea txtText;
	protected String chatData="";
	protected JCheckBox chbxSpeaker;
	protected JCheckBox chbxMic;
	public void AddMessage(String message) {
		chatData+=message+"\n";
		txtChatBox.setText(chatData);
	}
	
	public void MicStateChange() {
		if(chbxMic.isSelected()) {
			chbxMic.setIcon(new ImageIcon("resource/micOn.png"));
		}else {
			chbxMic.setIcon(new ImageIcon("resource/micOff.png"));
		}
	}
	public void SpeakerStateChange() {
		if(chbxSpeaker.isSelected()) {
			chbxSpeaker.setIcon(new ImageIcon("resource/speakerOn.png"));
		}else {
			chbxSpeaker.setIcon(new ImageIcon("resource/speakerOff.png"));
		}
	}
	/**
	 * Create the frame.
	 */	
	public abstract void OpenAudioChat();
	public abstract void CloseAudioChat();
	public ChatForm() {
		setTitle("Chat");
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent arg0) {
				OpenAudioChat();
				System.out.println("Chat form duoc open");
			}
			@Override
			public void windowClosing(WindowEvent arg0) {
				//CloseAudioChat();
				chbxMic.setSelected(false);
				chbxSpeaker.setSelected(false);
				System.out.println("Close audio chat roi!");
			}
		});
		CreateChatFormGUI();
	}
	
	public void AddMessage(String name, String message) {
		chatData+=name+": "+message+"\n";
		txtChatBox.setText(chatData);
	}
	
	public void CreateChatFormGUI() {
		setBounds(100, 100, 410, 560);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setAutoscrolls(true);
		
		JPanel panel = new JPanel();
		
		
		JScrollPane scrollPane_1 = new JScrollPane();
		
		txtText = new JTextArea();		
		txtText.setMargin(new Insets(4, 10, 4, 4));
		txtText.setFont(new Font("Tahoma", Font.PLAIN, 16));
		txtText.setWrapStyleWord(true);
		txtText.setLineWrap(true);
		scrollPane_1.setViewportView(txtText);
		
		btnSend = new JButton("Send");
		btnSend.setFocusable(false);
		
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(scrollPane, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 358, Short.MAX_VALUE)
						.addComponent(panel, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
							.addComponent(scrollPane_1, GroupLayout.DEFAULT_SIZE, 285, Short.MAX_VALUE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(btnSend)))
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(panel, GroupLayout.PREFERRED_SIZE, 39, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 383, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addComponent(scrollPane_1, GroupLayout.DEFAULT_SIZE, 43, Short.MAX_VALUE)
						.addComponent(btnSend, GroupLayout.DEFAULT_SIZE, 43, Short.MAX_VALUE))
					.addContainerGap())
		);
		
		chbxMic = new JCheckBox("");
		chbxMic.setIcon(new ImageIcon("resource\\micOn.png"));
		chbxMic.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				MicStateChange();
			}
		});
		
		chbxSpeaker = new JCheckBox("");
		chbxSpeaker.setIcon(new ImageIcon("resource\\speakerOn.png"));
		chbxSpeaker.setSelected(true);
		chbxSpeaker.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				SpeakerStateChange();
			}
		});
		panel.add(chbxSpeaker);
		chbxSpeaker.setAlignmentX(Component.RIGHT_ALIGNMENT);
		panel.add(chbxMic);
		chbxMic.setAlignmentX(Component.RIGHT_ALIGNMENT);
		chbxMic.setSelected(true);
		
		txtChatBox = new JTextArea();
		txtChatBox.setMargin(new Insets(5, 10, 5, 5));
		txtChatBox.setFont(new Font("Tahoma", Font.PLAIN, 16));
		txtChatBox.setEditable(false);
		scrollPane.setViewportView(txtChatBox);
		txtChatBox.setRows(10);
		txtChatBox.setLineWrap(true);
		txtChatBox.setWrapStyleWord(true);
		txtChatBox.setText(chatData);
		contentPane.setLayout(gl_contentPane);
	}
}
