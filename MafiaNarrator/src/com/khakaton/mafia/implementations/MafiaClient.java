package com.khakaton.mafia.implementations;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class MafiaClient {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws UnknownHostException 
	 */
	public static void main(String[] args) throws UnknownHostException, IOException {
		// TODO Auto-generated method stub
		Socket socket = new Socket("localhost", 4444);
		Scanner s = new Scanner(System.in);
		String name = s.nextLine();
		DataOutputStream os = new DataOutputStream(socket.getOutputStream());
		DataInputStream is = new DataInputStream(socket.getInputStream());
		os.writeUTF(name);
		while(true) {
			String dialog = is.readUTF();
			System.out.println(dialog);
			if(dialog == "Goodbye")
				break;
			if(dialog.startsWith("Please, go sleep.") )
				continue;
			int chosen = s.nextInt();
			os.writeInt(chosen);
		}
		System.out.println("Game over.");
	}

}
