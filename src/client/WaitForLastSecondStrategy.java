package client;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.Observable;
import java.util.Observer;

import server.IAuctionServer;
import server.Item;

public class WaitForLastSecondStrategy implements Observer, IAuctionListener, Serializable{

	private static final long serialVersionUID = -7290645750931455744L;
	private String itemName;
	private String bidderName;
	private IAuctionServer server;
	private boolean hasBidded;

	public WaitForLastSecondStrategy(IAuctionServer server, String itemName, String bidderName) {
		this.server = server;
		this.itemName = itemName;
		this.bidderName = bidderName;
		this.hasBidded = false;		
	}

	@Override
	public void update(Observable arg0, Object arg1) {		
		Item currentItem = (Item) arg1;
		update(currentItem);
	}

	@Override
	public void update(Item item) {
		if (item.getRemainingTime() == 1 && !this.hasBidded) {
			try {							
				this.hasBidded = true;				
				System.out.println("Strategy2: item: "+item.toString() + ", "+ bidderName + " "+ this.hasBidded );
				server.bidOnItem(bidderName, itemName, item.getCurrentBid() * 2);				
			} catch (RemoteException e) {
				System.err.println(e.getMessage());
			}
		}		
	}
}
