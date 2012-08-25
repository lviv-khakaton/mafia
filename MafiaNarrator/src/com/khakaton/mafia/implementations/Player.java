package com.khakaton.mafia.implementations;

public class Player {
	private PlayerType type;
	private Boolean alive;
	
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
}
