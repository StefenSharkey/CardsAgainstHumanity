package com.stefensharkey.cah.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Map;

import com.stefensharkey.cah.Game;
import com.stefensharkey.cah.player.Player;
import com.stefensharkey.cah.player.PlayerMap;

public class Server
{
	public static PlayerMap<Player, Socket> playerList = new PlayerMap<>();

	private ServerSocket serverSocket = null;
	private Socket socket = null;
	private InputStreamReader inputStreamReader = null;
	private BufferedReader bufferedReader = null;
	private PrintWriter printWriter = null;
	
	private String lineRead = "";
	
	private static final int PORT = 666;

	/**
	 * Starts listening for Game.numPlayers incoming connections. Asks client for a name, which then stores it in the playerList PlayerMap. 
	 */
	public void startServer()
	{
		try
		{
			serverSocket = new ServerSocket(PORT);
			System.out.println("Listening at 127.0.0.1 on port " + PORT);
			
			for(int x = 0; x < Game.numPlayers; x++)
			{
				socket = serverSocket.accept();

				inputStreamReader = new InputStreamReader(socket.getInputStream());
				bufferedReader = new BufferedReader(inputStreamReader);

				printWriter = new PrintWriter(socket.getOutputStream(),	true);

				printToClient("Please enter a name: ", socket);
				
				lineRead = bufferedReader.readLine();
				
				System.out.println("Received from Client: " + lineRead);
				
				playerList.put(new Player(lineRead, 10), socket);
				
				printToClient("Welcome, " + lineRead + "!", socket);
				printToClient("Your IP: " + socket.getInetAddress().toString().substring(1), socket);
			}
			
			printWriter = new PrintWriter(socket.getOutputStream(),	true);
			
		} catch (UnknownHostException e)
		{
			e.printStackTrace();
		} catch (InterruptedIOException e)
		{
			System.out.println("Timeout while attempting to establish socket connection.");
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Safely closes the server by closing the streams in the proper order.
	 */
	public void stopServer()
	{
		System.out.println("Closing connection.");
		try
		{
			bufferedReader.close();
			inputStreamReader.close();
			printWriter.close();
			socket.close();
		} catch (IOException e)
		{
			e.printStackTrace();
		} finally
		{
			try
			{
				socket.close();
				serverSocket.close();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public void printToClient()
	{
		printToClient("");
	}
	
	public void printToClient(String text)
	{
		try
		{
			for(Map.Entry<Player, Socket> entry : playerList.entrySet())
			{
				printWriter = new PrintWriter(entry.getValue().getOutputStream(), true);
				printWriter.println(text);
				System.out.println("To " + entry.getKey().getName() + ": " + text);
			}
		} catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void printToClient(String text, ArrayList<Player> players)
	{
		try
		{
			for(Map.Entry<Player, Socket> entry : playerList.entrySet())
			{
				for(Player player : players)
					if(entry.getKey().getName().equals(player.getName()))
					{
						printWriter = new PrintWriter(entry.getValue().getOutputStream(), true);
						printWriter.println(text);
						System.out.println("To " + entry.getKey().getName() + ": " + text);
					}
			}
		} catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void printToClient(String text, Player player)
	{
		try
		{
			for(Map.Entry<Player, Socket> entry : playerList.entrySet())
			{
				if(entry.getKey().getName().equals(player.getName()))
				{
					printWriter = new PrintWriter(entry.getValue().getOutputStream(), true);
					printWriter.println(text);
					System.out.println("To " + entry.getKey().getName() + ": " + text);
				}
			}
		} catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void printToClient(String text, Socket socket)
	{
		try
		{
			printWriter = new PrintWriter(socket.getOutputStream(), true);
			printWriter.println(text);
//			System.out.println(socket.));
//			if(socket.getLocalAddress().toString().substring(0, socket.getLocalAddress().toString().lastIndexOf("."+1)).equals(InetAddress.getLocalHost().toString().substring(0, socket.getLocalAddress().toString().lastIndexOf("."+1))))
//				System.out.println(socket.getLocalAddress());
			System.out.println("To " + socket.getInetAddress().toString().substring(1) + ":" + socket.getPort() + ": " + text);
		} catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public String waitForClient(Player player, Socket socket)
	{
		try
		{
			inputStreamReader = new InputStreamReader(socket.getInputStream());
			bufferedReader = new BufferedReader(inputStreamReader);
			if((lineRead = bufferedReader.readLine()) != null)
				if(lineRead.equalsIgnoreCase("stop"))
				{
					stopServer();
					return "";
				}
				else
				{
					System.out.println("From " + player.getName() + ": " + lineRead);
					return lineRead;
				}
		} catch(IOException e)
		{
			e.printStackTrace();
		}
		System.out.println("waitForClient();");
		return lineRead;
	}
}
