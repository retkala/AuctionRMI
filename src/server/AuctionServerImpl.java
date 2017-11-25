package server;


import java.rmi.RemoteException;
import java.util.Observer;
import java.util.Timer;
import client.IAuctionListener;
import java.util.ArrayList;

public class AuctionServerImpl implements IAuctionServer {

	
	private static IAuctionServer instance = null;
	
	private ArrayList<Item> items = new ArrayList<Item>();
	private AuctionServerTimerTask timerTask; 
	private Timer timer;
	
	protected AuctionServerImpl() throws RemoteException {
		super();
		this.timerTask = new AuctionServerTimerTask(this);
		this.timer = new Timer(true);
		timer.scheduleAtFixedRate(this.timerTask, 0, 1000);
		
	}

	public static IAuctionServer getInstance() throws RemoteException {
		if (instance == null) {
			instance = new AuctionServerImpl();
		}
		return instance; 
		
	}

	@Override
	public void placeItemForBid(String ownerName, String itemName, String itemDesc, double startBid, int auctionTime)
			throws RemoteException {
		if (isNotUnique(itemName)) {
			throw new RemoteException("Item name is not unique.");
		}
		Item item = new Item(ownerName, itemName, itemDesc, startBid, auctionTime);
		items.add(item);
		System.out.println(item.toString() + " was added.");

	}

	@Override
	public void bidOnItem(String bidderName, String itemName, double bid) throws RemoteException {
		for (Item i : items) {
			if(i.getItemName().equalsIgnoreCase(itemName)) {
				if(i.getCurrentBid() < bid ) {
					i.setCurrentBid(bid);
					i.setCurrentBidderName(bidderName);
					System.out.println("Server - there was a bid - item: " + i.toString());				
					i.notifyObservers(i);			
				} else {
					throw new RemoteException("New bid has to be higher than the current one.");
				}
				
			}
		}
	}

	@Override
	public ArrayList<Item> getItems() throws RemoteException {
		return items;
	}

	@Override
	public void registerListener(IAuctionListener actionListener, String itemName) throws RemoteException {
		System.out.println("Registered new observer for: " + itemName);
		for(Item item : items) {
			if(item.getItemName().equals(itemName)) {
				item.addObserver((Observer) actionListener);
			}
		}
	}
	

	private boolean isNotUnique(String itemName) {
		return items.stream().anyMatch(i -> i.getItemName().equals(itemName));
		
	}	

}
