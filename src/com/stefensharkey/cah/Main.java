package com.stefensharkey.cah;

import java.util.Scanner;

public class Main
{
	public static void main(String[] args)
	{
		Scanner keyboard = new Scanner(System.in);
		System.out.print("How many players are there? ");
		int players = keyboard.nextInt();
		System.out.print("What will the max score be? ");
		int maxScore = keyboard.nextInt();
		System.out.println();
		Game game = new Game();
		game.startGame(players, maxScore);
	}
}
