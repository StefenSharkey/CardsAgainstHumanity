package com.stefensharkey.cah.client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.stefensharkey.cah.card.WhiteCard;
import com.stefensharkey.cah.player.Player;

public class Client
{
//	private String serverUrl = "67.246.253.66";
	private String serverUrl = "";
	private String name;
	
	private Player player;
	
	private Scanner scanner;
	private PrintWriter printWriter;
	
	private static final int PORT = 666;
	
	private JFrame jFrame;
	private JMenuBar jMenuBar;
	private JMenu jMenuFile;
	private JMenuItem jMenuItemNewGame;
	private JList<Player> jList;
	
	private Player[] players = new Player[0];

	public static void main(String[] args)
	{
		Client client = new Client();
		client.init();
	}

	public void init()
	{
		jFrame = new JFrame("Cards Against Humanity");
		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jFrame.setResizable(false);
		jFrame.setPreferredSize(new Dimension(800, 600));
		
		jMenuBar = new JMenuBar();
		jFrame.setJMenuBar(jMenuBar);
		
		jMenuFile = new JMenu("File");
		jMenuBar.add(jMenuFile);
		
		jFrame.getContentPane().setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		jFrame.getContentPane().add(panel, BorderLayout.EAST);
		panel.setLayout(new GridLayout(2, 0, 0, 0));
		
		jList = new JList<>();
		jList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jList.setListData(players);
		panel.add(jList);
		
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException e)
		{
			e.printStackTrace();
		}
		
		jMenuItemNewGame = new JMenuItem("New Game");
		jMenuItemNewGame.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				Connect connect = new Connect();
				Thread thread = new Thread(connect);
				connect.start();
			}
		});
		jMenuFile.add(jMenuItemNewGame);
		
		jFrame.pack();
		jFrame.setLocationRelativeTo(null);
		jFrame.setVisible(true);
	}
	
	public boolean isInteger(String s)
	{
	    try
	    { 
	        Integer.parseInt(s); 
	    } catch(NumberFormatException e)
	    {
	        return false; 
	    }
	    return true;
	}
	
	class ClientWatchdog implements Runnable
	{
		private Socket socket = null;
		private InputStreamReader inputStreamReader = null;
		private BufferedReader bufferedReader = null;
		
		private String lineRead = "";
		
		public ClientWatchdog(Socket socket)
		{
			this.socket = socket;
		}
		
		public void run()
		{
			try
			{
				inputStreamReader = new InputStreamReader(socket.getInputStream());
				bufferedReader = new BufferedReader(inputStreamReader);
				while((lineRead = bufferedReader.readLine()) != null)
				{
					System.out.print("From server: ");
					switch(lineRead)
					{
						case "Selection: ":
							System.out.print(lineRead);
							printWriter.println(lineRead = scanner.nextLine());
							System.out.println();
							break;
						case "You've gained a card: ":
							System.out.println(lineRead);
							player.addCard(new WhiteCard(lineRead.substring(lineRead.indexOf("You've gained a card: "))));
							break;
						case "You've gained the cards: ":
							ArrayList<WhiteCard> tmp = new ArrayList<>();
							String[] cards = {};
							System.out.println(lineRead);
							cards = lineRead.split(",");
							
							for(String string : cards)
								tmp.add(new WhiteCard(string));
							
							player.addCards(tmp);
							break;
						default:
							System.out.println(lineRead);
							break;
					}
				}
			} catch(IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	class Connect extends Thread
	{
		public void run()
		{
			Socket socket = null;
			try
			{
				String name = JOptionPane.showInputDialog(jFrame, "Please enter the server IP:");
				String port = JOptionPane.showInputDialog(jFrame, "Please enter the server port (leave blank for 666):");
				
				if(port == null || port.equals(""))
					port = "666";
				
				if(name != null)
				{
					if(name.equals("localhost"))
						name = "127.0.0.1";
					System.out.println("Connecting to " + name + " on port " + Integer.parseInt(port));
					socket = new Socket(name, Integer.parseInt(port));
		
					System.out.println("Connected.");
		
					InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
					BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
		
					scanner = new Scanner(System.in);
					
					printWriter = new PrintWriter(socket.getOutputStream(),	true);
					String lineRead = bufferedReader.readLine();
		
					System.out.print("From server: " + lineRead);
		
					printWriter.println(name = scanner.nextLine());
					
					player = new Player(name, 0);
					
					ClientWatchdog clientWatchdog = new ClientWatchdog(socket);
					Thread thread = new Thread(clientWatchdog);
					thread.start();
					

					Player player = new Player("yes", 0);
					players = new Player[players.length+1];
					players[0] = player;
					jList.setListData(players);
					
				}
			} catch (UnknownHostException e)
			{
				e.printStackTrace();
				run();
			} catch (InterruptedIOException e)
			{
				System.out.println("Timeout while attempting to establish connection.");
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
}
