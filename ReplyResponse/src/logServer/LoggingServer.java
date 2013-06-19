package logServer;

import java.rmi.Remote;
import java.rmi.RemoteException;

import remoteClient.Peer;


public interface LoggingServer extends Remote {
	
	public boolean logIn(Peer peer) throws RemoteException;
}
