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
	private boolean finished;
	
	public GroupGameImpl(int totalCount) {
		finished = false;
		players = new ArrayList<Player>();
		int detectiveCount = 1, doctorCount = 1; 
		int mafiaCount = totalCount / 3; 
		if(totalCount==1) {
			mafiaCount = 1;
			doctorCount = detectiveCount = 0;
		}
		if(totalCount==2) {
			mafiaCount = 1;
			doctorCount = detectiveCount = 0;
		}
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
		List<String> playerNames = new ArrayList<String>();
		for(Player player : players)
			playerNames.add(player.getPlayerName());
		for(Player player : players)
			player.notifyClient(playerNames);
		play();
	}

	@Override
	public void play() {
		while(!finished) {
			doCycle();
		}
	}

	private int makeMove(List<Player> currentPlayers)
	{
		List<Boolean> actives = new ArrayList<Boolean>();
		for(Player player : players)
			actives.add(player.getAlive());
		
		while (true)
		{
			for(Player player : currentPlayers) {
				try {
					player.setActive(true, actives);
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
							player.setActive(false, actives);
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
		List<Boolean> actives = new ArrayList<Boolean>();
		for(Player player : players)
			actives.add(player.getAlive());
		
		while (true)
		{
			for(Player player : currentPlayers) {
				try {
					player.setActive(true, actives);
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
						player.setActive(false, actives);
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
		
		if (mafia==0) MakeSound.play("9.wav");
		else MakeSound.play("10.wav");
		
		finished = true;
		for(Player player : players)
			player.notifyFinish(mafia!=0);
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
		
		String[] messagesWakeUp = {"0.wav", "2.wav", "4.wav"};
		String[] messagesGoSleep = {"1.wav", "3.wav", "5.wav"};
		
				
		while (true)
		{
			
			MakeSound.play("6.wav");
			
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
				if (currentPlayers.size() > 0){
					MakeSound.play(messagesWakeUp[currentTypeIndex]);
					choosen[currentTypeIndex] = makeMove(currentPlayers);
					MakeSound.play(messagesGoSleep[currentTypeIndex]);
				}
				else
					choosen[currentTypeIndex] = -1;
				System.out.println("chosen by " + typeNames[currentTypeIndex] + " : " + choosen[currentTypeIndex]);
			}
			
			MakeSound.play("7.wav");
			
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
				endOfGame(mafiaLeft);
				return ;
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
			
			MakeSound.play("8.wav");
			
			System.out.println("ChooseWhomToKill called");
			
			indexOfKilled = chooseWhomToKill(currentPlayers);
			
			players.get(indexOfKilled).setAlive(false);
			--playersLeft;
			if (players.get(indexOfKilled).getType() == PlayerType.Mafia)
				--mafiaLeft;
			if (mafiaLeft == 0 || mafiaLeft == playersLeft)
			{
				endOfGame(mafiaLeft);
				return ;
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
