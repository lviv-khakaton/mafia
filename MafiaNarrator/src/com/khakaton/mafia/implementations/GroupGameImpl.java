package com.khakaton.mafia.implementations;

import java.net.Socket;
import java.util.ArrayList;
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
		players = new ArrayList<Player>();
		this.mafiaCount = mafiaCount;
		this.doctorCount = doctorCount;
		this.detectiveCount = detectiveCount;
		this.setTotalCount(totalCount);
	}
	
	public void makeRoles(){
		
		List<PlayerType>playerTypes = new ArrayList<PlayerType>();
		for (int i=0; i<mafiaCount; i++){
			playerTypes.add(PlayerType.Mafia);
		}
		for (int i=0; i<doctorCount; i++){
			playerTypes.add(PlayerType.Doctor);			
		}
		for (int i=0; i<detectiveCount; i++){
			playerTypes.add(PlayerType.Detective);
		}
		for (int i=mafiaCount+doctorCount+detectiveCount; i<getTotalCount(); i++){
			playerTypes.add(PlayerType.Citizen);
		}
		
		Collections.shuffle(playerTypes);
		
		for (int i=0; i<getTotalCount(); i++){
			players.add(new Player(playerTypes.get(i)));
		}
	}
	
	public void addPlayer(String name, Socket socket) {
		//TODO
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

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		GroupGameImpl game = new GroupGameImpl(2,2,2,10);
		game.makeRoles();
		for (int i=0; i<game.players.size(); i++){
			System.out.println(game.players.get(i).getType());
		}
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	
	public int getPlayersCount() {
		if(players==null)
			return 0;
		return players.size();
	}
	
}
