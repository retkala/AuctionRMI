package server;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class AuctionServer {

  

    public AuctionServer() {
        try {
            IAuctionServer as = new AuctionServerImpl();
            Naming.rebind("rmi://localhost:1099/Auctions", as);
        } catch (Exception e) {
            System.out.println("Server Error: " + e);
        }
    }

    public static void main(String args[]) {
    	String serverKind = "Auction";
    	AbstractServerFactory serverFactory = new ServerFactory();
    	try {
			IAuctionServer auctionServer = serverFactory.createServer(serverKind);
			IAuctionServer stub = (IAuctionServer) UnicastRemoteObject.exportObject(auctionServer, 0);
			Registry registry = LocateRegistry.getRegistry();
			registry.rebind("Auctions", stub);					
			
		} catch (RemoteException e) {
			System.err.println("Server Error: "+ e.getMessage());	
			
		} 
        
    }


}
