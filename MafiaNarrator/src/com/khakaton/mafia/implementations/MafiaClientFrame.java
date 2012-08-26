package com.khakaton.mafia.implementations;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.dyno.visual.swing.layouts.Constraints;
import org.dyno.visual.swing.layouts.GroupLayout;
import org.dyno.visual.swing.layouts.Leading;

//VS4E -- DO NOT REMOVE THIS LINE!
public class MafiaClientFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private JButton jButton0;
	private JTextField jTextField0;
	private JLabel jLabel0;
	private JPanel jPanel0;
	private static final String PREFERRED_LOOK_AND_FEEL = "javax.swing.plaf.metal.MetalLookAndFeel";

	public MafiaClientFrame() {
		initComponents();
	}

	private void initComponents() {
		setLayout(new GroupLayout());
		add(getJButton0(), new Constraints(new Leading(203, 10, 10), new Leading(68, 12, 12)));
		add(getJTextField0(), new Constraints(new Leading(42, 10, 10), new Leading(71, 12, 12)));
		add(getJLabel0(), new Constraints(new Leading(12, 12, 12), new Leading(12, 12, 12)));
		add(getJPanel0(), new Constraints(new Leading(10, 295, 10, 10), new Leading(22, 214, 10, 10)));
		setSize(320, 240);
	}

	private JPanel getJPanel0() {
		if (jPanel0 == null) {
			jPanel0 = new JPanel();
			jPanel0.setLayout(new GridLayout(4,6));
		}
		return jPanel0;
	}

	private JLabel getJLabel0() {
		if (jLabel0 == null) {
			jLabel0 = new JLabel();
			jLabel0.setText("");
		}
		return jLabel0;
	}

	private JTextField getJTextField0() {
		if (jTextField0 == null) {
			jTextField0 = new JTextField();
			jTextField0.setText("Enter your name");
		}
		return jTextField0;
	}

	private JButton getJButton0() {
		if (jButton0 == null) {
			jButton0 = new JButton();
			jButton0.setText("jButton0");
			jButton0.addMouseListener(new MouseAdapter() {
	
				public void mouseClicked(MouseEvent event) {
					try {
						jButton0MouseMouseClicked(event);
					} catch (UnknownHostException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
		}
		return jButton0;
	}

	private static void installLnF() {
		try {
			String lnfClassname = PREFERRED_LOOK_AND_FEEL;
			if (lnfClassname == null)
				lnfClassname = UIManager.getCrossPlatformLookAndFeelClassName();
			UIManager.setLookAndFeel(lnfClassname);
		} catch (Exception e) {
			System.err.println("Cannot install " + PREFERRED_LOOK_AND_FEEL
					+ " on this platform:" + e.getMessage());
		}
	}

	/**
	 * Main entry of the class.
	 * Note: This class is only created so that you can easily preview the result at runtime.
	 * It is not expected to be managed by the designer.
	 * You can modify it as you like.
	 */
	public static void main(String[] args) {
		installLnF();
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				MafiaClientFrame frame = new MafiaClientFrame();
				frame.setDefaultCloseOperation(MafiaClientFrame.EXIT_ON_CLOSE);
				frame.setTitle("MafiaClientFrame");
				frame.getContentPane().setPreferredSize(frame.getSize());
				frame.pack();
				frame.setLocationRelativeTo(null);
				frame.setVisible(true);
			}
		});
	}

	private void jButton0MouseMouseClicked(MouseEvent event) throws UnknownHostException, IOException 
	{
		Socket socket = new Socket("localhost", 4444);
		String name = jTextField0.getText();
		jLabel0.setText(name);
		final DataOutputStream os = new DataOutputStream(socket.getOutputStream());
		DataInputStream is = new DataInputStream(socket.getInputStream());
		os.writeUTF(name);
		int n = is.readInt();
		String[] names = new String[n];
		for (int i=0; i < n; ++i)
		{
			names[i] = is.readUTF();
		}
		String type = is.readUTF();
		jLabel0.setText(name + " " + type);
		jTextField0.setVisible(false);
		jButton0.setVisible(false);
		JButton[] buttons = new JButton[n];
		for (int i=0; i < n; ++i)
		{
			buttons[i] = new JButton(names[i]);
			buttons[i].setSize(40, 40);
			buttons[i].setActionCommand(Integer.toString(i));
			buttons[i].addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
			  try {
				os.writeInt(Integer.parseInt(e.getActionCommand()));
			} catch (NumberFormatException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			  }
			});
			jPanel0.add(buttons[i]);
		}
		
		while(true) {
			int dialog = is.readInt();
			if (dialog == 0)
			{
				for (int i=0; i < n; ++i)
				{
					buttons[i].setEnabled(is.readBoolean());
				}
				
			}else
			if (dialog == 1)
			{
				Font font = new Font("Arial", 1, 48);
				jLabel0.setFont(font);
				jLabel0.setText("You are dead");
			}
			else
			if (dialog == 2)
			{
				boolean won = is.readBoolean();
				Font font = new Font("Arial", 1, 48);
				jLabel0.setFont(font);
				if (won)
					jLabel0.setText("You won");
				else
					jLabel0.setText("You lost");
			}else
			if (dialog == 7)
			{
				for (int i=0; i < n; ++i)
				{
					buttons[i].setEnabled(false);
				}
			}
		}
	}

}
