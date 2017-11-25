package server;
import java.rmi.RemoteException;
import java.util.ListIterator;

import java.util.TimerTask;

public class AuctionServerTimerTask extends TimerTask {
	private IAuctionServer server;

	public AuctionServerTimerTask(IAuctionServer server) {
		this.server = server;

	}

	@Override
	public void run() {
		try {
			ListIterator<Item> iter = server.getItems().listIterator();

			while (iter.hasNext()) {
				Item item = iter.next();
				if (item.getRemainingTime() == 0) {
					iter.remove();
					System.out.println("Auction has finished " + item.toString());
				} else {
					item.setRemainingTime(item.getRemainingTime() - 1);
					item.notifyObservers(item);
				}
			}
		} catch (RemoteException e) {
			System.err.println("Error: " + e.getMessage());			
		}
	}

}
