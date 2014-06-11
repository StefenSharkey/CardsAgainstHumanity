package com.stefensharkey.cah.client;

import java.net.Socket;
import java.util.ArrayList;

import com.stefensharkey.cah.card.BlackCard;
import com.stefensharkey.cah.card.WhiteCard;
import com.stefensharkey.cah.player.Player;
import com.stefensharkey.cah.server.Server;

public class LocalPlayer extends Player
{
	private ArrayList<WhiteCard> hand = new ArrayList<>();
	private ArrayList<BlackCard> wonCards = new ArrayList<>();
	
	private int score;
	
	private String name;
	
	private Server server;
	private Socket socket;
	
	public LocalPlayer(String name, int cards)
	{
		super(name, cards);
		this.name = name;
		
		score = 0;
	}
}
