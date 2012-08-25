package com.khakaton.mafia.implementations;

public class Player {
	private PlayerType type;
	private Boolean alive;
	private int decision;
	private String playerName; 
	
	public Player(PlayerType type){
		this.alive=true;
		this.type=type;
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

	public int getDecision() {
		return decision;
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
