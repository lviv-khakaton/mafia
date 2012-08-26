
package com.khakaton.mafia.implementations;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.dyno.visual.swing.layouts.Constraints;
import org.dyno.visual.swing.layouts.GroupLayout;
import org.dyno.visual.swing.layouts.Leading;

//VS4E -- DO NOT REMOVE THIS LINE!
public class MafiaServerFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private JButton jButton0;
	private JSpinner jSpinner0;
	private JLabel jLabel0;
	private static final String PREFERRED_LOOK_AND_FEEL = "javax.swing.plaf.metal.MetalLookAndFeel";
	public MafiaServerFrame() {
		initComponents();
	}

	private void initComponents() {
		setLayout(new GroupLayout());
		add(getJButton0(), new Constraints(new Leading(216, 10, 10), new Leading(81, 10, 10)));
		add(getJLabel0(), new Constraints(new Leading(14, 10, 10), new Leading(86, 12, 12)));
		add(getJSpinner0(), new Constraints(new Leading(153, 10, 10), new Leading(86, 18, 12, 12)));
		setSize(320, 240);
	}

	private JLabel getJLabel0() {
		if (jLabel0 == null) {
			jLabel0 = new JLabel();
			jLabel0.setText("Number of players");
		}
		return jLabel0;
	}

	private JSpinner getJSpinner0() {
		if (jSpinner0 == null) {
			SpinnerModel model = new SpinnerNumberModel(3, 3, 25, 1);
			jSpinner0 = new JSpinner(model);
		}
		return jSpinner0;
	}

	private JButton getJButton0() {
		if (jButton0 == null) {
			jButton0 = new JButton();
			jButton0.setText("Begin");
			jButton0.addMouseListener(new MouseAdapter() {
	
				public void mouseClicked(MouseEvent event) {
					try {
						jButton0MouseMouseClicked(event);
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
				MafiaServerFrame frame = new MafiaServerFrame();
				frame.setDefaultCloseOperation(MafiaServerFrame.EXIT_ON_CLOSE);
				frame.setTitle("MafiaFrame");
				frame.getContentPane().setPreferredSize(frame.getSize());
				frame.pack();
				frame.setLocationRelativeTo(null);
				frame.setVisible(true);
			}
		});
	}

	private void jButton0MouseMouseClicked(MouseEvent event) throws IOException {
		System.out.println("Server started.\n");
		ServerSocket ss = new ServerSocket(4444);
		
		GroupGameImpl gg = new GroupGameImpl((Integer) jSpinner0.getValue());
		
		while(true)
		{
			Socket client = ss.accept();
			DataInputStream dis = new DataInputStream(client.getInputStream());
			String playername = dis.readUTF();
			gg.addPlayer(playername, client);
			System.out.println("Player added : " + playername);
			System.out.println(gg.getPlayersCount() + " players connected.");
			if(gg.getPlayersCount()==gg.getTotalCount())
			{
				gg.start();
				System.out.println("Game started.");
			}
		}
	}

}
