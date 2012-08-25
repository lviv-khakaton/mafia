package com.khakaton.mafia.implementations;

import java.util.Collections;
import java.util.List;

import com.khakaton.mafia.interfaces.GroupGame;

public class GroupGameImpl implements GroupGame {
	private List<Player> players;
	private int mafiaCount;
	private int detectiveCount;
	private int doctorCount;
	private int totalCount;
	
	public GroupGameImpl(int mafiaCount, int detectiveCount, int doctorCount, int totalCount) {
		this.mafiaCount = mafiaCount;
		this.doctorCount = doctorCount;
		this.detectiveCount = detectiveCount;
		this.totalCount = totalCount;
	}
	
	public void makeRoles(){
		for (int i=0; i<mafiaCount; i++){
			players.add(new Player(PlayerType.Mafia));
		}		
		for (int i=0; i<doctorCount; i++){
			players.add(new Player(PlayerType.Doctor));
		}		
		for (int i=0; i<detectiveCount; i++){
			players.add(new Player(PlayerType.Detective));
		}
		for (int i=mafiaCount+doctorCount+detectiveCount; i<totalCount; i++){
			players.add(new Player(PlayerType.Citizen));
		}
		
		Collections.shuffle(players);
	}
	
	@Override
	public void start() {
		// TODO Auto-generated method stub

	}

	@Override
	public void play() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doCycle() {
		// TODO Auto-generated method stub

	}

}
