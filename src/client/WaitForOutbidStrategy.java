package client;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.Observable;
import java.util.Observer;

import server.IAuctionServer;
import server.Item;

public class WaitForOutbidStrategy implements IAuctionListener, Observer, Serializable {

	private static final long serialVersionUID = -7593541164479630925L;
	private String itemName; 
	private String bidderName; 
	private IAuctionServer server;
	private Double maxBid;
	
	public WaitForOutbidStrategy(IAuctionServer server, String itemName, String bidderName, Double maxBid){
		this.server = server;
		this.itemName = itemName;
		this.bidderName = bidderName;
		this.maxBid = maxBid;
		System.out.println("WaitForOutbidStrategy was created");
	}

	@Override
	public void update(Item item) {	
		if(item.getCurrentBid() < maxBid && !item.getCurrentBidderName().equals(bidderName) && !item.getCurrentBidderName().equals("")) {
//			System.out.println("Changed object: " + item.getItemName() + "observed object: "+ itemName);
			try {
				System.out.println("Strategy1: item: "+ item.toString() + ", "+ bidderName);
				server.bidOnItem(bidderName, itemName, item.getCurrentBid()+1);				
			} catch (RemoteException e) {
				System.err.println("Error in WaitForOutbidStrategy" + e.getMessage());				
			}
		}
		
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		Item item = (Item)arg1;
		update(item);

		
	}

}
