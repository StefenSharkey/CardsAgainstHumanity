package com.stefensharkey.cah.player;

import java.util.ArrayList;

import com.stefensharkey.cah.card.BlackCard;
import com.stefensharkey.cah.card.WhiteCard;
import com.stefensharkey.cah.card.WhiteDeck;

public class Player
{
	private ArrayList<WhiteCard> hand = new ArrayList<>();
	private ArrayList<BlackCard> wonCards = new ArrayList<>();
	
	private int score;
	
	private String name;
	
	public Player(String name, int cards)
	{
		this.name = name;
		
		addCards(cards);
		
		score = 0;
	}
	
	public void addCards(int num)
	{
		for(int x = 0; x < num; x++)
			hand.add(new WhiteDeck().getCard());
	}
	
	public void addCards(ArrayList<WhiteCard> whiteCards)
	{
		for(WhiteCard whiteCard : whiteCards)
			hand.add(whiteCard);
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
