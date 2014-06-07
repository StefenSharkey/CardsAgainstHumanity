package com.stefensharkey.cah.card;

public class WhiteCard
{
	private String text;
	
	public WhiteCard(String text)
	{
		setText(text);
	}
	
	public void setText(String text)
	{
		this.text = text;
	}
	
	public String toString()
	{
		return text;
	}
}
