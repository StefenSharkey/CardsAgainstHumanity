package com.stefensharkey.cah.card;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class WhiteDeck extends ArrayList<WhiteCard>
{
	private static ArrayList<WhiteCard> deck = new ArrayList<>();
	
	public void fillDeck()
	{
		try
		{
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			
			Document document = documentBuilder.parse("white_cards.xml");
			NodeList nodeList = document.getElementsByTagName("card");
	
			for(int x = 0; x < nodeList.getLength(); x++)
				deck.add(new WhiteCard(nodeList.item(x).getTextContent().trim()));
		} catch(ParserConfigurationException | SAXException | IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public ArrayList<WhiteCard> addDeck(ArrayList<WhiteCard> whiteCards)
	{
		deck.addAll(whiteCards);
		return whiteCards;
	}
	
	public WhiteCard addDeck(WhiteCard whiteCard)
	{
		deck.add(whiteCard);
		return whiteCard;
	}
	
	public WhiteCard getCard()
	{
		Random r1 = new Random();
		return deck.remove((int)r1.nextInt(deck.size()));
	}
	
	public boolean isEmpty()
	{
		return deck.size() > 0 ? false : true;
	}
	
	public int indexOf(Object object)
	{
		for(int x = 0; x < deck.size(); x++)
			if(deck.get(x).equals(object))
				return x;
		return -1;
	}
	
	public int indexOf(String string)
	{
		for(int x = 0; x < deck.size(); x++)
			if(deck.get(x).toString().equals(string))
				return x;
		return -1;
//		return indexOf(new WhiteCard(string));
	}
	
	public String toString()
	{
		StringBuilder tmp = new StringBuilder();
		tmp.append("[").append(deck.get(0));
		for(int x = 1; x < deck.size(); x++)
			tmp.append(", ").append(deck.get(x));
		tmp.append("]");
		return tmp.toString();
	}
}
