package com.stefensharkey.cah;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class BlackDeck
{
	private static ArrayList<BlackCard> deck = new ArrayList<>();
	
	public BlackDeck()
	{
		
	}
	
	public void fillDeck()
	{
		try
		{
				DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
				
				Document document = documentBuilder.parse("black_cards.xml");
				NodeList nodeList = document.getElementsByTagName("card");
		
				for(int x = 0; x < nodeList.getLength(); x++)
					deck.add(new BlackCard(nodeList.item(x).getTextContent().trim()));
		} catch(ParserConfigurationException | SAXException | IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public BlackCard getCard()
	{
		Random r1 = new Random();
		return deck.remove((int)r1.nextInt(deck.size()));
	}
	
	public boolean isEmpty()
	{
		return deck.size() > 0 ? false : true;
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
