package client;

import java.rmi.Remote;
import java.rmi.RemoteException;

import server.Item;

public interface IAuctionListener extends Remote {

	public void update(Item item) throws RemoteException;

}
