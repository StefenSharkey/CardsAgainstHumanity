package com.stefensharkey.cah;

import java.util.Scanner;

public class Main
{
	public static void main(String[] args)
	{
		Scanner keyboard = new Scanner(System.in);
		Game game = new Game();
		int players;
		int maxScore;
		System.out.println(args.length);
		if(args.length > 0)
		{
			players = args[0].equals("-players ") ? players = Integer.parseInt(args[0].substring(args[0].indexOf("-players "))) : keyboard.nextInt();
			maxScore = args[1].equals("-maxscore") ? maxScore = Integer.parseInt(args[1].substring(args[1].indexOf("-maxscore "))) : keyboard.nextInt();
		}
		else
		{
			System.out.print("How many players are there? ");
			players = keyboard.nextInt();
			System.out.print("What will the max score be? ");
			maxScore = keyboard.nextInt();
			System.out.println();
		}
		game.startGame(players, maxScore);
	}
}
