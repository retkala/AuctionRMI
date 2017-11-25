package server;

import java.rmi.RemoteException;

public abstract class AbstractServerFactory {
	abstract IAuctionServer createServer(String serverType) throws RemoteException;
}
