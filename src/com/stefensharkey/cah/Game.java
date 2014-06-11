package com.stefensharkey.cah;

import java.net.Socket;
import java.util.ArrayList;

import com.stefensharkey.cah.card.BlackCard;
import com.stefensharkey.cah.card.BlackDeck;
import com.stefensharkey.cah.card.WhiteCard;
import com.stefensharkey.cah.card.WhiteDeck;
import com.stefensharkey.cah.player.Player;
import com.stefensharkey.cah.server.Server;

public class Game
{
	private Server server;
	private WhiteDeck whiteDeck = new WhiteDeck();
	private BlackDeck blackDeck = new BlackDeck();
	private ArrayList<ArrayList<WhiteCard>> playedCards = new ArrayList<>();
	
	private int spots;
	private int maxScore;
	private int czar = -1;
	
	public static int numPlayers;
	
	public void startGame(int numPlayers, int maxScore)
	{
		blackDeck.fillDeck();
		whiteDeck.fillDeck();
		this.maxScore = maxScore;
		this.numPlayers = numPlayers;
		
		server = new Server();
		server.startServer();
		
		server.printToClient("");
		
		do
			tick();
		while(!whiteDeck.isEmpty() && !blackDeck.isEmpty() && !isMaxScoreReached());
		getReasonGameEnd();
	}
	
	public void tick()
	{
		pickCzar();
		displayBlackCard();
		playedCards = new ArrayList<>();
		for(int x = 0; x < Server.playerList.size(); x++)
			if(x != czar)
			{
				playedCards.add(selectCard(Server.playerList.getEntry(x).getKey(), Server.playerList.getEntry(x).getValue()));
				Server.playerList.getEntry(x).getKey().addCards(spots, Server.playerList.getEntry(x).getValue());
			}
		
		for(int x = 0; x < playedCards.size(); x++)
				server.printToClient(Server.playerList.getEntry(x).getKey().getName() + " played " + playedCards.get(x));
		
		pickCard();
		
		for(Player player : Server.playerList.keySet())
			server.printToClient(player.getName() + " has " + player.getScore() + " points!");
	}
	
	public void pickCzar()
	{
		czar++;
		if(czar > Server.playerList.size()-1)
			czar = 0;
		for(int x = 0; x < Server.playerList.size(); x++)
			if(x != czar)
				server.printToClient(Server.playerList.getEntry(czar).getKey().getName() + " is " + "the Czar!", Server.playerList.getEntry(x).getKey());
		
		server.printToClient("You are the Czar!", Server.playerList.getEntry(czar).getKey());
		server.printToClient();
	}
	
	public ArrayList<WhiteCard> selectCard(Player player, Socket socket)
	{
		ArrayList<WhiteCard> tmp = new ArrayList<>();
		Server tmpServer = new Server();
		server.printToClient(player.getName() + "'s Cards", player);
		for(int x = 0; x < player.getHand().size(); x++)
			server.printToClient(x + " - " + player.getHand().get(x), player);
		server.printToClient("Select which card" + (spots > 1 ? "s" : "") + " you would like to play.", player);
		for(int x = 0; x < spots; x++)
		{
			server.printToClient("Selection: ", player);
			tmp.add(player.getHand().remove(Integer.parseInt(tmpServer.waitForClient(player, socket))));
		}
		return tmp;
	}
	
	public void pickCard()
	{
		Server tmpServer = new Server();
		server.printToClient("Which card wins? Selection: ", Server.playerList.getEntry(czar).getKey());
		for(int x = 0; x < playedCards.size(); x++)
			for(int y = 0; y < spots; y++)
				server.printToClient((x+1) + " - " + playedCards.get(x).get(y), Server.playerList.getEntry(czar).getKey());

		server.printToClient("Selection: ", Server.playerList.getEntry(czar).getKey());
		int winningCard = Integer.parseInt(tmpServer.waitForClient(Server.playerList.getEntry(czar).getKey(), Server.playerList.getEntry(czar).getValue()));
		server.printToClient(Server.playerList.getEntry(czar).getKey().getName() + " selected the card: " + playedCards.get(winningCard-1));
		if(winningCard <= czar)
			winningCard--;
		server.printToClient(Server.playerList.getEntry(winningCard).getKey().getName() + " gets a point!");
		Server.playerList.getEntry(winningCard).getKey().addScore();
		server.printToClient();
	}
	
	public BlackCard displayBlackCard()
	{
		BlackCard tmp = blackDeck.getCard();
		spots = tmp.getSpots();
		server.printToClient("The Black Card is: " + tmp.toString());
		server.printToClient();
		return tmp;
	}
	
	public void getReasonGameEnd()
	{
		if(whiteDeck.isEmpty())
			server.printToClient("White Deck is empty!");
		
		if(blackDeck.isEmpty())
			server.printToClient("Black Deck is empty!");
		
		if(isMaxScoreReached())
			server.printToClient("Max score has been reached!");
	}
	
	public int getMaxScore()
	{
		return maxScore;
	}
	
	public boolean isMaxScoreReached()
	{
		for(Player player : Server.playerList.keySet())
			if(player.getScore() == maxScore)
				return true;
		return false;
	}
}
