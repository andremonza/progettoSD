package peer;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Server implements LoggingServer {
	
	private final int maxPeer; 
	private int nPeer;
	private Peer[] peer;
	
	public Server(int maxPeer) {
		this.maxPeer = maxPeer;
		this.nPeer = 0;
		this.peer = new Peer[this.maxPeer];
	}

	//@Override
	public boolean logIn(Peer peer) {
		if(this.nPeer < this.maxPeer) {
			this.peer[this.nPeer] = peer;
			this.nPeer ++;
			
			System.out.println("Logged " + this.nPeer + "-th peer");

			if(this.nPeer == this.maxPeer) {
				System.out.println("Now assign neighbors");
				setPeersNeighbors();
			}

			return true;
		}
		
		return false;
	}

	private void setPeersNeighbors() {
		
		try {

			// Assign neighbors to peer nodes
			for(int i = 0; i < this.maxPeer; i ++) {

				Registry registry = LocateRegistry.getRegistry(this.peer[i].getAddr());	
				Peer p = (Peer) registry.lookup(this.peer[i].getName());
				p.setNeighbor(this.peer[(i + 1) % this.maxPeer]);
			}

			/*
			// Start peers' work
			Registry registry = LocateRegistry.getRegistry(this.peer[0].getAddr());
			Peer first = (Peer) registry.lookup(this.peer[0].getName());
			first.wakeUp(42);
			*/
			

			// Shutdow the logger
			Registry registry = LocateRegistry.getRegistry();
			registry.unbind("Logger");
			if(! UnicastRemoteObject.unexportObject(this, false)) {
				System.out.println("Unexporting fails..Force it");
				UnicastRemoteObject.unexportObject(this, true);
			}
			System.out.println("Terminate");

		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) {
		if(args.length < 1) {
			System.err.println("Usage: java peer.Server <N_PEER>");
			System.exit(1);
		}
		Server server = new Server(Integer.parseInt(args[0]));
		
		try {
			LoggingServer stub = (LoggingServer) UnicastRemoteObject.exportObject(server, 0);

			// Bind the remote object's stub in the registry
			Registry registry = LocateRegistry.getRegistry();
			registry.rebind("Logger", stub);
			
			System.err.println("Server ready");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
