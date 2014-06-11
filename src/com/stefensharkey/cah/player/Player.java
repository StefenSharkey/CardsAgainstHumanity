package com.stefensharkey.cah.player;

import java.net.Socket;
import java.util.ArrayList;

import com.stefensharkey.cah.IPlayer;
import com.stefensharkey.cah.card.BlackCard;
import com.stefensharkey.cah.card.WhiteCard;
import com.stefensharkey.cah.card.WhiteDeck;
import com.stefensharkey.cah.server.Server;

public class Player implements IPlayer
{
	private ArrayList<WhiteCard> hand; 
	private ArrayList<BlackCard> wonCards;
	
	private int score;
	
	private String name;
	
	private Server server;
	private Socket socket;
	
	private static final int CARD_NUM = 10;
	
	public Player(String name, int cards, Socket socket)
	{
		this(name, cards);
		this.socket = socket;
		server = new Server();
	}
	
	public Player(String name, int cards)
	{
		this.name = name;
		
		score = 0;
		
		hand = new ArrayList<>();
		wonCards = new ArrayList<>();
	}
	
	public void addCards(Player player)
	{
		Server server = new Server();
		ArrayList<WhiteCard> tmp = new ArrayList<>();
		for(int x = getHand().size(); x < CARD_NUM; x++)
			tmp.add(new WhiteDeck().getCard());
		hand.addAll(tmp);
		server.printToClient("You've gained the cards: " + tmp, player);
	}
	
	public void addCards(Socket socket)
	{
		Server server = new Server();
		ArrayList<WhiteCard> tmp = new ArrayList<>();
		for(int x = getHand().size(); x < CARD_NUM; x++)
			tmp.add(new WhiteDeck().getCard());
		hand.addAll(tmp);
		server.printToClient("You've gained the cards: " + tmp, socket);
	}
	
	public void addCards()
	{
		Server server = new Server();
		ArrayList<WhiteCard> tmp = new ArrayList<>();
		for(int x = getHand().size(); x < CARD_NUM; x++)
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
	
	public void addCard(WhiteCard whiteCard, Socket socket)
	{
		hand.add(whiteCard);
		server.printToClient(whiteCard.toString(), socket);
	}
	
	public void addCard(WhiteCard whiteCard)
	{
		hand.add(whiteCard);
	}
	
	public void incrementScore(Player player)
	{
		score++;
		server.printToClient("Your score: " + score, player);
	}
	
	public void incrementScore(Socket socket)
	{
		score++;
		server.printToClient("Your score: " + score, socket);
	}
	
	public void incrementScore()
	{
		score++;
	}
	
	public BlackCard addWonCard(BlackCard blackCard, Player player)
	{
		wonCards.add(blackCard);
		server.printToClient("You've won the black card: " + blackCard, player);
		return blackCard;
	}
	
	public BlackCard addWonCard(BlackCard blackCard, Socket socket)
	{
		wonCards.add(blackCard);
		server.printToClient("You've won the black card: " + blackCard, socket);
		return blackCard;
	}
	
	public BlackCard addWonCard(BlackCard blackCard)
	{
		wonCards.add(blackCard);
		return blackCard;
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
