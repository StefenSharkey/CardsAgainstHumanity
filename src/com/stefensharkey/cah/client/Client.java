package com.stefensharkey.cah.client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.font.TextAttribute;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.stefensharkey.cah.card.BlackCard;
import com.stefensharkey.cah.card.WhiteCard;
import com.stefensharkey.cah.gui.ScrollPaneLayout;
import com.stefensharkey.cah.player.Player;

public class Client extends JFrame
{
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
	private JScrollPane jScrollPane;
	private JPanel bottomPanel;
	private JPanel bottomContainer;
	private JPanel centerPanel;
	
	private Player[] players = new Player[25];
	
	private BlackCard czarCard;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					Client frame = new Client();
					frame.setVisible(true);
				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Client()
	{
		getContentPane().setLayout(null);
		player = new Player("Player 1", 0);
		for(int x = 1; x < players.length; x++)
			players[x] = new Player("Player " + (x+1) + " ", 0);
		players[0] = player;
		
		setTitle("Cards Against Humanity");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setPreferredSize(new Dimension(1200, 600));

		getContentPane().setLayout(new BorderLayout(0, 0));
		
		jMenuBar = new JMenuBar();
		setJMenuBar(jMenuBar);
		
		jMenuFile = new JMenu("File");
		jMenuBar.add(jMenuFile);
		
		jScrollPane = new JScrollPane();
		getContentPane().add(jScrollPane, BorderLayout.EAST);
		jScrollPane.setLayout(new ScrollPaneLayout());
		jScrollPane.getViewport().add(createPlayerList(players));
		
		jList = new JList<>();
		jList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		
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
			public void actionPerformed(ActionEvent e)
			{
				Connect connect = new Connect();
				connect.start();
			}
		});
		jMenuFile.add(jMenuItemNewGame);
		
		bottomPanel = new JPanel();
		getContentPane().add(bottomPanel, BorderLayout.SOUTH);
		bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
		bottomPanel.add(new JSeparator(JSeparator.HORIZONTAL));
		
//		bottomContainer = new JPanel();
//		bottomContainer.setLayout(new GridLayout());
//		bottomContainer.add(createCardGrid(2, 5));
		
//		bottomPanel.add(bottomContainer);
		
		centerPanel = new JPanel();
		getContentPane().add(centerPanel, BorderLayout.CENTER);
		centerPanel.setLayout(new GridLayout());
		centerPanel.add(createCardGrid(5, 5));
		
		pack();
		setLocationRelativeTo(null);
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
	
	public JPanel createPlayerList(Player[] players)
	{
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		JPanel panel1 = new JPanel();
		panel1.setLayout(new BoxLayout(panel1, BoxLayout.Y_AXIS));
		
		for(int x = 0; x < players.length; x++)
		{
			JPanel panel2 = new JPanel();
			panel2.setLayout(new BoxLayout(panel2, BoxLayout.Y_AXIS));
			GridBagConstraints labelConstraints = new GridBagConstraints();
			JLabel name = new JLabel(players[x].getName());
			JLabel score = new JLabel("Score: " + players[x].getScore());
			panel2.add(name, labelConstraints);
			panel2.add(score, labelConstraints);
			panel1.add(panel2);
			panel1.add(new JSeparator(JSeparator.HORIZONTAL));
		}
		panel.add(panel1);
		return panel;
	}
	
	public JPanel createWhiteCard(WhiteCard whiteCard)
	{
		JPanel card = new JPanel();
		card.setLayout(new BorderLayout());
		JButton button = new JButton(whiteCard.toString());
		card.add(button);
		return card;
	}
	
	public JPanel createWhiteCard(ArrayList<WhiteCard> whiteCards)
	{
		JPanel cards = new JPanel();
		for(WhiteCard whiteCard : whiteCards)
			cards.add(createWhiteCard(whiteCard));
		return cards;
	}
	
	public JPanel createCardGrid(int height, int width)
	{
		JPanel panel = new JPanel();
		GridLayout gridLayout = new GridLayout(height, width);
		panel.setLayout(gridLayout);
		for(int x = 0; x < gridLayout.getColumns(); x++)
			for(int y = 0; y < gridLayout.getRows(); y++)
				panel.add(createWhiteCard(new WhiteCard("White Card " + ((x*gridLayout.getColumns())+(y)))));
		return panel;
	}
	
	public void addCenterCards(JPanel panel)
	{
		bottomContainer = new JPanel();
		bottomContainer.setLayout(new GridLayout());
		bottomContainer.add(createCardGrid(2, 5));
		panel.add(bottomContainer);
	}
	
	public void updateBottomCard(WhiteCard whiteCard)
	{
		
	}
	
	public JLabel setClientPlayer(String text)
	{
		JLabel label = new JLabel(text);
		Font font = label.getFont();
		Map attributes = font.getAttributes();
		attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
		label.setFont(font.deriveFont(attributes));
		return label;
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
					if(lineRead.startsWith("The Black Card is: "))
					{
						// start at the length of the string
						czarCard = new BlackCard(lineRead.substring(lineRead.lastIndexOf("The Black Card is: ")));
						System.out.println(czarCard);
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
				String ip = JOptionPane.showInputDialog(jFrame, "Please enter the server IP:");
				setFocusable(false);
				
				if(ip != null && !ip.trim().equals(""))
				{
					String port = JOptionPane.showInputDialog(jFrame, "Please enter the server port (leave blank for 666):");
					
					if(port == null || port.equals(""))
						port = String.valueOf(PORT);
					
					if(ip.equals("localhost"))
						ip = "127.0.0.1";
					System.out.println("Connecting to " + ip + " on port " + Integer.parseInt(port));
					socket = new Socket(ip, Integer.parseInt(port));

					System.out.println("Connected.");
		
					scanner = new Scanner(System.in);
					
					printWriter = new PrintWriter(socket.getOutputStream(),	true);
					
					String name = JOptionPane.showInputDialog(jFrame, "Please enter your name:");
					
					printWriter.println(name);
					
					player = new Player(name, 0);
					
					ClientWatchdog clientWatchdog = new ClientWatchdog(socket);
					Thread thread = new Thread(clientWatchdog);
					thread.start();
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
