package com.khakaton.mafia.implementations;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Player {
	private PlayerType type;
	private Boolean alive;
	private int decision;
	private String playerName;
	private Socket socket;
	
	public Player(String playerName, Socket socket){
		this.playerName = playerName;
		this.alive = true;
		this.socket = socket;
		decision = -1;
	}
	
	public PlayerType getType() {
		return type;
	}
	public void setType(PlayerType type) {
		this.type = type;
	}
	public Boolean getAlive() {
		return alive;
	}
	public void setAlive(Boolean alive) {
		this.alive = alive;
	}
	
	public void setActive(boolean active) throws IOException {
		DataOutputStream os = new DataOutputStream(socket.getOutputStream());
		if(active) {
			//TODO set active user ( make his screen brighter )
			os.writeUTF("Please, wake up.");
			return ;
		}
		//TODO set inactive
		os.writeUTF("Please, go sleep.");
	}
	
	

	public int getDecision() {
		int _decision = -1;
		try {
			DataInputStream is = new DataInputStream(socket.getInputStream());
			_decision = is.readInt();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return _decision;
	}

	public void setDecision(int decision) {
		this.decision = decision;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}
}
