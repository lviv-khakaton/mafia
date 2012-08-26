package com.khakaton.mafia.implementations;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import org.eclipse.wb.swing.FocusTraversalOnArray;
import java.awt.Component;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class MafiaClientFrame extends JFrame {

	private JPanel contentPane;
	private JTextField jTextField0;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MafiaClientFrame frame = new MafiaClientFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	
	
	public MafiaClientFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		final JLabel jLabel0 = new JLabel("MyNameHere");
		jLabel0.setBounds(12, 12, 103, 15);
		contentPane.add(jLabel0);
		
		jTextField0 = new JTextField();
		jTextField0.setText("EnterYourName");
		jTextField0.setBounds(27, 118, 114, 19);
		contentPane.add(jTextField0);
		jTextField0.setColumns(10);

		
		
		final JButton jButton0 = new JButton("Ok");
		jButton0.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("PressedOK");
				Socket socket = null;
				try {
					socket = new Socket("localhost", 4444);
				} catch (UnknownHostException e2) {
					e2.printStackTrace();
				} catch (IOException e2) {
					e2.printStackTrace();
				}

				final String name = jTextField0.getText();
				//11111111111111111111111111111111111111111111111111111111111111111111111111111111
			    long start = System.currentTimeMillis();

	        	start += 1000;
			    new java.util.Timer().schedule(new java.util.TimerTask() 
			    {
	                public void run() 
	                {
	                	jLabel0.setText(name);
	                	
	                }
			    }, new Date(start));

				
				DataOutputStream os = null;
				try {
					os = new DataOutputStream(socket.getOutputStream());
				} catch (IOException e2) {
					e2.printStackTrace();
				}
				final DataOutputStream oos = os;
				DataInputStream is = null;
				try {
					is = new DataInputStream(socket.getInputStream());
				} catch (IOException e2) {
					e2.printStackTrace();
				}
				try {
					os.writeUTF(name);
				} catch (IOException e2) {
					e2.printStackTrace();
				}
				final DataInputStream iis = is;
				System.out.println(name);
				int n = 0;
				try {
					n = is.readInt();
				} catch (IOException e2) {
					e2.printStackTrace();
				}
				final int m = n;
				System.out.println(n);
				String[] names = new String[n];
				for (int i=0; i < n; ++i)
				{
					try {
						names[i] = is.readUTF();
					} catch (IOException e) {
						e.printStackTrace();
					}
					System.out.println(names[i]);
				}
				String type = "";
				try {
					type = is.readUTF();
				} catch (IOException e2) {
					e2.printStackTrace();
				}
				System.out.println(type);
				jLabel0.setText(name + " " + type);
				jTextField0.setVisible(false);
				jButton0.setVisible(false);
				jButton0.setEnabled(false);
				contentPane.remove(jButton0);
				contentPane.repaint(0, 0, 1000, 1000);
				System.out.println(type);
				contentPane.repaint();
				jTextField0.repaint();
				jButton0.repaint();
				final JButton[] buttons = new JButton[n];
				for (int i=0; i < n; ++i)
				{
					buttons[i] = new JButton(names[i]);
					buttons[i].setVisible(true);
					buttons[i].setEnabled(false);
					buttons[i].setBounds(80*(i+1), 80,80,80);
					buttons[i].setActionCommand(Integer.toString(i));
					buttons[i].addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e) {
						try {
						oos.writeInt(Integer.parseInt(e.getActionCommand()));
					} catch (NumberFormatException e1) {
						e1.printStackTrace();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
						}
					});
					contentPane.add(buttons[i]);
				}
				contentPane.repaint();
				
				start += 1000;
			    new java.util.Timer().schedule(new java.util.TimerTask() 
			    {
	                public void run() 
	                {
	                	while(true) {
	                		int dialog = -1;
							try {
								dialog = iis.readInt();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
	                		if (dialog == 0)
	                		{
	                			System.out.println("Get0");
	                			for (int i=0; i < m; ++i)
	                			{
	                				try {
										buttons[i].setEnabled(iis.readBoolean());
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
	                			}
	                		}else
	                		if (dialog == 1)
	                		{
	                			System.out.println("Get1");
	                			jLabel0.setText("You are dead");
	                		}
	                		else
	                		if (dialog == 2)
	                		{
	                			System.out.println("Get2");
	                			boolean won = false;
								try {
									won = iis.readBoolean();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
	                			if (won)
	                				jLabel0.setText("You won");
	                			else
	                				jLabel0.setText("You lost");
	                		}else
	                		if (dialog == 7)
	                		{
	                			System.out.println("Get7");
	                			for (int i=0; i < m; ++i)
	                			{
	                				buttons[i].setEnabled(false);
	                			}
	                		}
	                	}
	                }
			    }, new Date(start));
				
			}
		});
		jButton0.setBounds(292, 115, 117, 25);
		contentPane.add(jButton0);
		contentPane.setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{jLabel0, jTextField0, jButton0}));
		
	}
}
