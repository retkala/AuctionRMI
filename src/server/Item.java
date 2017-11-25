package server;

import java.io.Serializable;
import java.util.Observable;

public class Item extends Observable implements Serializable {

	private static final long serialVersionUID = 8768520661831471533L;
	private String ownerName;
	private String itemName;
	private String itemDescription;
	private double currentBid;
	private String currentBidderName;
	private int remainingTime;

	public Item(String ownerName, String itemName, String itemDescription, double startBid, int remainingTime) {
		this.ownerName = ownerName;
		this.itemName = itemName;
		this.itemDescription = itemDescription;
		this.currentBid = startBid;
		this.currentBidderName = "";
		this.remainingTime = remainingTime;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getItemDescription() {
		return itemDescription;
	}

	public void setItemDescription(String itemDescription) {
		this.itemDescription = itemDescription;
	}

	public double getCurrentBid() {
		return currentBid;
	}

	public void setCurrentBid(double currentBid) {
		this.currentBid = currentBid;
		setChanged();
	}

	public String getCurrentBidderName() {
		return currentBidderName;
	}

	public void setCurrentBidderName(String currentBidderName) {
		this.currentBidderName = currentBidderName;
	}

	public int getRemainingTime() {
		return remainingTime;
	}

	public void setRemainingTime(int remainingTime) {
		this.remainingTime = remainingTime;
		setChanged();

	}

	@Override
	public String toString() {
		return "Item [ownerName: " + ownerName + ", itemName: " + itemName + ", itemDescription: " + itemDescription
				+ ", currentBid: " + currentBid + ", currentBidderName: " + currentBidderName + ", remainingTime:"
				+ remainingTime + "]";
	}
	
	

}
