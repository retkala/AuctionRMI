package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;

import server.IAuctionServer;
import server.Item;

import java.util.ArrayList;
import java.util.HashMap;


public class AuctionClient implements Serializable {


	private static final long serialVersionUID = 7629309418996388615L;
	static IAuctionServer server;
	HashMap<String, IAuctionListener> bidList;
	public final int STRATEGY = 0;
	public final int STRATEGY_WAIT_FOR_OUTBID = 1;
	public final int STRATEGY_WAIT_UNTILL_LAST_SECOND = 2;

	protected AuctionClient() throws MalformedURLException, RemoteException, NotBoundException {
		super();
		server = (IAuctionServer) Naming.lookup("rmi://localhost:1099/Auctions");
		bidList = new HashMap<>();
	}

	public void placeItemForBid(String ownerName, String itemName, String itemDesc, double startBid, int auctionTime)
			throws RemoteException {
		server.placeItemForBid(ownerName, itemName, itemDesc, startBid, auctionTime);
	}

	public void bidOnItem(String bidderName, String itemName, double bid) throws RemoteException {
		System.out.println("Clinet=> bidder: " +bidderName+ "itemName: "+itemName+", bid: "+bid);
		server.bidOnItem(bidderName, itemName, bid);
	}

	public void registerListener(String itemName, int strategy, String bidderName) throws RemoteException {		
		
		if(strategy == STRATEGY_WAIT_FOR_OUTBID) {
			System.out.println("Type maximum bid value");
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			Double maxBid;
			try {
				maxBid = Double.parseDouble(in.readLine());
				WaitForOutbidStrategy s = new WaitForOutbidStrategy(server, itemName, bidderName, maxBid);
				server.registerListener(s, itemName);
			} catch (NumberFormatException e) {
				System.err.println("Wrong value: "+ e.getMessage());				
			} catch (IOException e) {
				System.err.println(e.getMessage());					
			}
			
		} else if (strategy == STRATEGY_WAIT_UNTILL_LAST_SECOND) {
			if (getRemainingTime(itemName) <= 1) {
				System.out.println("Strategy couldn't be created. Too little time to the end of the auction.");
			} else {
				WaitForLastSecondStrategy s = new WaitForLastSecondStrategy(server, itemName, bidderName);
				server.registerListener(s, itemName);
				System.out.println("Strategy was created for item: " + itemName);
			}			
		}
	}

	public static void main(String[] args) {

		try {
			System.out.println("Simple auction system. Type \"options\" for options.");

			AuctionClient client = new AuctionClient();

			Scanner sc = new Scanner(System.in);

			while (true) {
				System.out.print(">> ");

				String command;

				if (!sc.hasNextLine()) {
					sc.close();
					return;
				}

				command = sc.nextLine();

				try {
					if (command.startsWith("options")) {
						client.printOptions();
					} else if (command.startsWith("put")) {
						System.out.println("type ownner name: ");
						String ownerName = sc.nextLine();
						System.out.println("type item name: ");
						String itemName = sc.nextLine();
						System.out.println("type item description: ");
						String itemDesc = sc.nextLine();
						System.out.println("type start/minimum bid: ");
						double startBid = Double.parseDouble(sc.nextLine());
						System.out.println("type duraction of auction time: ");
						int auctionTime = Integer.parseInt(sc.nextLine());

						server.placeItemForBid(ownerName, itemName, itemDesc, startBid, auctionTime);
						System.out.println("Item was placed for bid.");

					} else if (command.startsWith("bid")) {
						System.out.println("type bidder name: ");
						String bidderName = sc.nextLine();
						System.out.println("type item name: ");
						String itemName = sc.nextLine();
						System.out.println("type bid value: ");
						double bid = Double.parseDouble(sc.nextLine());
						client.bidOnItem(bidderName, itemName, bid);

					} else if (command.startsWith("items")) {
						ArrayList<Item> items = server.getItems();
						System.out.println("Items: ");
						items.forEach(i -> System.out.println(i.toString()));

					} else if (command.startsWith("add")) {
						System.out.println("type item name: ");
						String itemName = sc.nextLine();
						System.out.println("type strategy number: ");
						int strategy = Integer.parseInt(sc.nextLine());
						System.out.println("type bidder name: ");
						String bidderName = sc.nextLine();						
						client.registerListener(itemName, strategy, bidderName);

					} else if (command.startsWith("exit")) {
						sc.close();
						System.exit(0);
					} else {
						System.err.println("No such command. Type \"options\" for help.");
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (MalformedURLException murle) {
			System.out.println();
			System.out.println("MalformedURLException");
			System.out.println(murle);
		} catch (RemoteException re) {
			System.out.println();
			System.out.println("RemoteException");
			System.out.println(re);
		} catch (NotBoundException nbe) {
			System.out.println();
			System.out.println("NotBoundException");
			System.out.println(nbe);
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public void printOptions() {
		System.out.println("options - options");
		System.out.println("put - put item for bid");
		System.out.println("bid - bid on item");
		System.out.println("items - print items");
		System.out.println("add - add listener, strategies: ");
		System.out.println("1 : If anyone outbids the client, automatically bid $1.00 more than the current bid, up to the maximum bid.");
		System.out.println("2 : Wait until the last minute of the auction period, then bid 100% more than the current bid.");
		System.out.println("add - add listener, strategies: ");		
		System.out.println("exit - exit the client");
	}

	public int getRemainingTime(String itemName) {
		ArrayList<Item> items;
		try {
			items = server.getItems();
			for(Item item : items) {
				if(item.getItemName().equals(itemName)){
					return item.getRemainingTime();
				}
			}
		} catch (RemoteException e) {			
			e.printStackTrace();
		}
		return 0;
	}
}
