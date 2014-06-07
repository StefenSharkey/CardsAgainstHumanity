package com.stefensharkey.cah;

public class BlackCard
{
	private String text;
	private int spots = 0;
	
	public BlackCard(String text)
	{
		if(!text.endsWith(" _"))
			text = text + " _";
		this.text = text;
		for(char s : text.toCharArray())
			if(s == '_')
				spots++;
	}
	
	public int getSpots()
	{
		return spots;
	}
	
	public void setText(String ... text)
	{
		for(String spot : text)
		{
			this.text.replaceFirst("_", spot);
		}
	}
	
	public String toString()
	{
		return text;
	}
}
