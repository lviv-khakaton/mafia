package com.khakaton.mafia.implementations;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class MafiaServer {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		System.out.println("Server started.\n");
		ServerSocket ss = new ServerSocket(4444);	
		
		GroupGameImpl gg = new GroupGameImpl(3);
		
		while(true) {
			Socket client = ss.accept();
			DataInputStream dis = new DataInputStream(client.getInputStream());
			String playername = dis.readUTF();
			gg.addPlayer(playername, client);
			System.out.println("Player added : " + playername);
			System.out.println(gg.getPlayersCount() + " players connected.");
			if(gg.getPlayersCount()==gg.getTotalCount()) {
				
				gg.start();
				System.out.println("Game started.");
			}
		}
	}

}
