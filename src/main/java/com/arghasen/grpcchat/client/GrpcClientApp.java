package com.arghasen.grpcchat.client;

import java.awt.BorderLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Observable;
import java.util.Observer;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class GrpcClientApp extends JFrame implements Observer {

	private JTextField hostTextField;
	private JTextField portTextField;

	private JTextField usernameTextField;
	private JPasswordField passwordTextField;

	private JTextArea textArea;

	private JTextField inputTextField;
	private JTextField inputTextField1;

	private JButton sendButton;
	private JButton loginButton;
	private JButton connectButton;
	private GrpcClient chatAccess;
	private String userName;

	public GrpcClientApp(GrpcClient grpcClient) {
		chatAccess = grpcClient;
		initUI();
	}

	private void initUI() {
		setTitle("GRPC CHAT CLIENT");
		setSize(800, 600);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		Box box = Box.createHorizontalBox();
		add(box, BorderLayout.NORTH);
		JLabel hostLabel = new JLabel("Host");
		hostTextField = new JTextField();
		JLabel portLabel = new JLabel("Port");
		portTextField = new JTextField();
		connectButton = new JButton("Connect");
		box.add(hostLabel);
		box.add(hostTextField);
		box.add(portLabel);
		box.add(portTextField);
		box.add(connectButton);

		JLabel userLabel = new JLabel("User");
		usernameTextField = new JTextField();
		JLabel passLabel = new JLabel("Pass");
		passwordTextField = new JPasswordField();
		loginButton = new JButton("Login");
		loginButton.setEnabled(false);
		box.add(userLabel);
		box.add(usernameTextField);
		box.add(passLabel);
		box.add(passwordTextField);
		box.add(loginButton);

		// Action for the connect Button
		ActionListener connnectListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String host = hostTextField.getText();
				String port = portTextField.getText(); // insecure but used for ease of use.
				if (host != null && port.trim().length() > 0 && port != null && port.trim().length() > 0) {
					chatAccess.init(host, port);
				}
				loginButton.setEnabled(true);
//				hostTextField.setEnabled(false);
//				portTextField.setEnabled(false);
//				connectButton.setEnabled(false);
			}
		};
		// Action for the login Button
		ActionListener loginListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String username = usernameTextField.getText();
				String password = passwordTextField.getText(); // insecure but used for ease of use.
				if (username != null && username.trim().length() > 0 && password != null
						&& password.trim().length() > 0) {
					chatAccess.login(username,password);
					userName = username;
				}
				//box.setVisible(false);
			}
		};
		textArea = new JTextArea(20, 50);
		textArea.setEditable(false);
		textArea.setLineWrap(true);
		add(new JScrollPane(textArea), BorderLayout.CENTER);

		Box box1 = Box.createHorizontalBox();
		add(box1, BorderLayout.SOUTH);

		JLabel recipientLabel = new JLabel("Recipient");
		inputTextField = new JTextField();
		JLabel msgLabel = new JLabel("Msg");
		inputTextField1 = new JTextField();
		sendButton = new JButton("Send");
		box1.add(recipientLabel);
		box1.add(inputTextField);
		box1.add(msgLabel);
		box1.add(inputTextField1);
		box1.add(sendButton);
		sendButton.setEnabled(false);

		// Action for the inputTextField and the goButton
		ActionListener sendListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String recipient = inputTextField.getText();
				String msg = inputTextField1.getText();
				if (recipient != null && recipient.trim().length() > 0 && msg != null && msg.trim().length() > 0) {
					// chatAccess.send(str);
					textArea.append("<" + userName + "> @" + recipient + ": " + msg);
					textArea.append("\n");
				}
				inputTextField.selectAll();
				inputTextField.requestFocus();
				inputTextField.setText("");
			}
		};
		inputTextField.addActionListener(sendListener);
		sendButton.addActionListener(sendListener);
		loginButton.addActionListener(loginListener);
		connectButton.addActionListener(connnectListener);
		
		//testing  defaults
		hostTextField.setText("localhost");
		portTextField.setText("50051");
		usernameTextField.setText("argha");
		passwordTextField.setText("abc123");
		
		//
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				// chatAccess.close();
			}
		});
	}

	@Override
	public void update(Observable o, Object arg) {
		final Object finalArg = arg;
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				textArea.append(finalArg.toString());
				textArea.append("\n");
			}
		});
	}

	public static void main(String[] args) {
		GrpcClient client = new GrpcClient();
		JFrame frame = new GrpcClientApp(client);
		// frame.setTitle("MyChatApp - connected to " + server + ":" + port);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setVisible(true);
	}

}
