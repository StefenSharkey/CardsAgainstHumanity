package com.stefensharkey.cah;

import java.net.Socket;
import java.util.ArrayList;

import com.stefensharkey.cah.card.BlackCard;
import com.stefensharkey.cah.card.WhiteCard;
import com.stefensharkey.cah.player.Player;

public interface IPlayer
{
	public void addCards(Player player);
	
	public void addCards(Socket socket);
	
	public void addCards();
	
	public ArrayList<WhiteCard> addCards(ArrayList<WhiteCard> whiteCards);
	
	public void addCard(WhiteCard whiteCard, Socket socket);
	
	public void addCard(WhiteCard whiteCard);
	
	public void incrementScore(Player player);
	
	public void incrementScore(Socket socket);
	
	public void incrementScore();
	
	public BlackCard addWonCard(BlackCard blackCard, Player player);
	
	public BlackCard addWonCard(BlackCard blackCard, Socket socket);
	
	public BlackCard addWonCard(BlackCard blackCard);
	
	public ArrayList<BlackCard> getWonCards();
	
	public int getScore();
	
	public String getName();
	
	public ArrayList<WhiteCard> getHand();
	
	public String toString();
}
