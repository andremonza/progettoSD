package peer;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Client implements Peer, Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String name;
	private String ipAddr;
	private Peer neighbor;
	
	public Client(String name) {
		setName(name);
		try {
			this.ipAddr = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//@Override
	public void setName(String name) {
		this.name = name;
	}

	//@Override
	public String getName() {
		return this.name;
	}
	
	//@Override
	public String getAddr() {
		return this.ipAddr;
	}
	
	//@Override
	public void setNeighbor(Peer neighbor) {
		this.neighbor = neighbor;
		
		String neighborName = "";
		try {
			neighborName = this.neighbor.getName();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		
		System.out.println(this.name + " is linked to " + neighborName);
		GUI gui = new GUI(this.name, this);
	}

	//@Override
	public void wakeUp(int value) {
	}

	public void giocata(int value) {
		//hvkjsvdljasvdsaljvsdfhvba
		
		// Wait for a while..
		for(int i = 0; i < 3; i ++) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(i + "..");
		}
		System.out.println("value=" + value);
		
		// Pass token to neighbor
		passaTurno(value);
	}

	private void passaTurno(int token) {
		Registry registry;
		try {
			registry = LocateRegistry.getRegistry(this.neighbor.getAddr());
			Peer n = (Peer) registry.lookup(this.neighbor.getName());
			
			n.wakeUp(token);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {

		if(args.length < 2) {
			System.err.println("Usage: java peer.Server <LOGGER_ADDR> <CLIENT_NAME>");
			System.exit(1);
		}
		
		String loggerAddr = args[0];
		Client client = new Client(args[1]);
		
		try {
			Peer stub = (Peer) UnicastRemoteObject.exportObject(client, 0);
			
			// Obtain logger stub and make client stub accessible
			Registry loggerRegistry = LocateRegistry.getRegistry(loggerAddr);
			Registry localRegistry = LocateRegistry.getRegistry();
			LoggingServer server = (LoggingServer) loggerRegistry.lookup("Logger");
			localRegistry.rebind(stub.getName(), stub);

			server.logIn(client);

			System.err.println("Client " + client.getName() + " starts");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
