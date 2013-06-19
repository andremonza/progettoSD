package logServer;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import remoteClient.Peer;


public class Server implements LoggingServer {
	
	private final int maxPeer;
	private ArrayList<Peer> peers;
	
	public Server(int maxPeer) {
		this.maxPeer = maxPeer;
		this.peers = new ArrayList<Peer>();
	}

	@Override
	public boolean logIn(Peer peer) {
		if(this.peers.size() < this.maxPeer) {
			this.peers.add(peer);
			
			System.out.println("Logged " + this.peers.size() + "-th peer");

			if(this.peers.size() == this.maxPeer) {
				System.out.println("Now assign neighbors");
				setPeersNeighbors();
			}

			return true;
		}
		
		return false;
	}

	private void setPeersNeighbors() {
		
		try {
			/* 
			 * Assign neighbors to peer nodes
			 */
			for(Peer p: this.peers) {
				p.setNeighbor(this.peers, this.peers.indexOf(p));
			}

			/*
			 * Peers start only after neighbors are set
			 */
			for(Peer p: this.peers) {
				p.start();
			}			

			/*
			 *  Shutdow the logger
			 */
			Registry registry = LocateRegistry.getRegistry();
			registry.unbind("Logger");
			if(! UnicastRemoteObject.unexportObject(this, false)) {
				System.out.println("Unexporting fails..Force it");
				UnicastRemoteObject.unexportObject(this, true);
			}

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
