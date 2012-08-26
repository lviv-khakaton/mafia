package com.khakaton.mafia.implementations;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;

public class Player {
	private PlayerType type;
	private Boolean alive;
	private String playerName;
	private DataOutputStream os;
	private DataInputStream is;
	
	public Player(String playerName, Socket socket){
		try {
			os = new DataOutputStream(socket.getOutputStream());
			is = new DataInputStream(socket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		this.playerName = playerName;
		this.alive = true;
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
		if(!this.alive) {
			try {
				os.writeInt(1);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void setActive(boolean active, List<Boolean> actives) throws IOException {
		if(active) {
			//set active user ( make his screen brighter )
			os.writeInt(0);
			for(Boolean a : actives)
				os.writeBoolean(a);
			os.flush();
			return ;
		}
		//set inactive
		os.writeInt(7);
		os.flush();
	}
	
	public void notifyFinish(boolean mafiaWon) {
		try {
			os.writeInt(2);
			if( (type==PlayerType.Mafia && mafiaWon) ||
					(type!=PlayerType.Mafia && !mafiaWon) )
				os.writeBoolean(true);
			else
				os.writeBoolean(false);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public int getDecision() {
		int _decision = -1;
		try {
			_decision = is.readInt();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return _decision;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public void notifyClient(List<String> playerNames) {
		try {
			os.writeInt(playerNames.size());
		} catch (IOException e) {
			e.printStackTrace();
		}
		for(String name : playerNames)
			try {
				os.writeUTF(name);
			} catch (IOException e) {
				e.printStackTrace();
			}
		String code = "";
		switch (type) {
			case Detective :
				code = "Detective"; break;
			case Mafia :
				code = "Mafia"; break;
			case Doctor :
				code = "Doctor"; break;
			default :
				code = "Citizen"; break;
		}
		try {
			os.writeUTF(code);
			os.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
