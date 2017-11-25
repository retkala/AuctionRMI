package server;

import java.rmi.RemoteException;

public class ServerFactory extends AbstractServerFactory{

	private final String AUCTION = "Auction";
	@Override
	public IAuctionServer createServer(String serverKind) throws RemoteException {
		if (serverKind.equals(AUCTION)) {
			return AuctionServerImpl.getInstance();
		}
		return null;
	}

}
