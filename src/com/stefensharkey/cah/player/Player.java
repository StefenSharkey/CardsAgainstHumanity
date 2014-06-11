package com.stefensharkey.cah.player;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import com.stefensharkey.cah.card.BlackCard;
import com.stefensharkey.cah.card.WhiteCard;
import com.stefensharkey.cah.card.WhiteDeck;
import com.stefensharkey.cah.server.Server;

public class Player
{
	private ArrayList<WhiteCard> hand = new ArrayList<>();
	private ArrayList<BlackCard> wonCards = new ArrayList<>();
	
	private int score;
	
	private String name;
	
	private Socket socket;
	
	public Player(String name, int cards, Socket socket)
	{
		this(name, cards);
		this.socket = socket;
	}
	
	public Player(String name, int cards)
	{
		this.name = name;
		
		addCards(cards);
		
		score = 0;
	}
	
	public void addCards(int num, Socket socket)
	{
		Server server = new Server();
		ArrayList<WhiteCard> tmp = new ArrayList<>();
		for(int x = 0; x < num; x++)
			tmp.add(new WhiteDeck().getCard());
		hand.addAll(tmp);
		server.printToClient("You've gained the cards: " + tmp, socket);
	}
	
	public void addCards(int num)
	{
		Server server = new Server();
		ArrayList<WhiteCard> tmp = new ArrayList<>();
		for(int x = 0; x < num; x++)
			tmp.add(new WhiteDeck().getCard());
		hand.addAll(tmp);
		server.printToClient("You've gained the cards: " + tmp);
	}
	
	public ArrayList<WhiteCard> addCards(ArrayList<WhiteCard> whiteCards)
	{
		ArrayList<WhiteCard> tmp = new ArrayList<>();
		tmp.addAll(whiteCards);
		hand.addAll(tmp);
		return tmp;
	}
	
	public void addCard(WhiteCard whiteCard)
	{
		hand.add(whiteCard);
	}
	
	public void addScore()
	{
		score++;
	}
	
	public void addWonCard(BlackCard blackCard)
	{
		wonCards.add(blackCard);
	}
	
	public ArrayList<BlackCard> getWonCards()
	{
		return wonCards;
	}
	
	public int getScore()
	{
		return score;
	}
	
	public String getName()
	{
		return name;
	}
	
	public ArrayList<WhiteCard> getHand()
	{
		return hand;
	}
	
	public String toString()
	{
		StringBuilder tmp = new StringBuilder();
		tmp.append("Cards ").append("[").append(hand.get(0));
		for(int x = 1; x < 10; x++)
			tmp.append(", ").append(hand.get(x));
		tmp.append("]").append("\n").append("Score: ").append(getScore());
		return tmp.toString();
	}
}
