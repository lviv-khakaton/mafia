package com.khakaton.mafia.implementations;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
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
	
	public void setRoles(){
		
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
			players.get(i).setType(playerTypes.get(i));
		}
	}
	
	public void addPlayer(String name, Socket socket) {
		//TODO
		players.add(new Player(name, socket));
	}
	
	@Override
	public void start() {
		// TODO Auto-generated method stub
		setRoles();
		play();
	}

	@Override
	public void play() {
		while(true) {
			doCycle();
		}
	}

	private int makeMove(List<Player> currentPlayers)
	{
		while (true)
		{
			for(Player player : currentPlayers) {
				try {
					player.setActive(true);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			HashSet<Integer> decisions = new HashSet<Integer>();
			for (int i = 0; i < currentPlayers.size(); ++i)
			{
				decisions.add(currentPlayers.get(i).getDecision());
			}
						
			if (decisions.size() == 1) {
				int voted = decisions.iterator().next();
				if(voted!=-1) {
					for(Player player : currentPlayers) {
						try {
							player.setActive(false);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					return voted;
				}
			}
		}
	}
	
	private int chooseWhomToKill(List<Player> currentPlayers)
	{
		System.out.println("in chooseWhomToKill");
		while (true)
		{
			for(Player player : currentPlayers) {
				try {
					player.setActive(true);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			Integer[] decisions = new Integer[players.size()];
			Arrays.fill(decisions, 0);
			for (int i = 0; i < currentPlayers.size(); ++i)
			{
				decisions[currentPlayers.get(i).getDecision()]++;
			}
			int firstMax = 0, secondMax = 0, firstMaxI = -1;
			for (int i = 0; i < players.size(); ++i)
			{
				if (decisions[i] > firstMax)
				{
					secondMax = firstMax;
					firstMax = decisions[i];
					firstMaxI = i;
				}else
				if (decisions[i] > secondMax)
				{
					secondMax = decisions[i];
				}
			}
			if (firstMax != secondMax) {
				for(Player player : currentPlayers) {
					try {
						player.setActive(false);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				return firstMaxI;
			}
		}
	}

	void tellResults(int killed, Boolean checked) {
		System.out.println("Killed : " + killed);
		System.out.println("Checked : " + checked);
		System.out.print("Still alive : ");
		for(Player player : players)
			if(player.getAlive())
				System.out.print(player.getPlayerName() + " ");
		System.out.print("\n");
	}
	
	void endOfGame(int mafia)
	{
		System.out.println("Game Over!\n" + mafia + " mafia left\n");
	}
	

	@Override
	public void doCycle() {
		// TODO Auto-generated method stub
		
		System.out.println("doCycle called");
		
		int mafiaLeft = mafiaCount;
		int playersLeft = players.size();
		
		PlayerType[] currentType = {PlayerType.Mafia, PlayerType.Detective, PlayerType.Doctor};
		String[] typeNames = {"PlayerType.Mafia", "PlayerType.Detective", "PlayerType.Doctor"};
		while (true)
		{
			Integer[] choosen = new Integer[4];
			for (int currentTypeIndex = 0; currentTypeIndex < 3; ++currentTypeIndex)
			{
				List<Player> currentPlayers = new ArrayList<Player>();
				for (int i = 0; i < players.size(); ++i)
				{
					if (players.get(i).getAlive() && players.get(i).getType() == currentType[currentTypeIndex])
					{
						currentPlayers.add(players.get(i));
					}
				}
				if (currentPlayers.size() > 0)
					choosen[currentTypeIndex] = makeMove(currentPlayers);
				else
					choosen[currentTypeIndex] = -1;
				System.out.println("chosen by " + typeNames[currentTypeIndex] + " : " + choosen[currentTypeIndex]);
			}
			int indexOfKilled = choosen[0];
			if (choosen[0] == choosen[2])
				indexOfKilled = -1;
			else
			{
				players.get(indexOfKilled).setAlive(false);
				--playersLeft;
				if (players.get(indexOfKilled).getType() == PlayerType.Mafia)
					--mafiaLeft;
			}
			if (mafiaLeft == 0 || mafiaLeft == playersLeft)
			{
				endOFGame(mafiaLeft);
			}
			Boolean checkedCorrectly = false;
			if (players.get(choosen[1]).getType() == PlayerType.Mafia)
				checkedCorrectly = true;
			tellResults(indexOfKilled, checkedCorrectly);
			
			List<Player> currentPlayers = new ArrayList<Player>();
			for (int i = 0; i < players.size(); ++i)
			{
				if (players.get(i).getAlive())
				{
					currentPlayers.add(players.get(i));
				}
			}
			
			System.out.println("ChooseWhomToKill called");
			
			indexOfKilled = chooseWhomToKill(currentPlayers);
			
			players.get(indexOfKilled).setAlive(false);
			--playersLeft;
			if (players.get(indexOfKilled).getType() == PlayerType.Mafia)
				--mafiaLeft;
			if (mafiaLeft == 0 || mafiaLeft == playersLeft)
			{
				endOFGame(mafiaLeft);
			}
		}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		/*
		GroupGameImpl game = new GroupGameImpl(2,2,2,10);
		game.makeRoles();
		for (int i=0; i<game.players.size(); i++){
			System.out.println(game.players.get(i).getType());
		}*/
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
