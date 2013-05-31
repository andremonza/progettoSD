package peer;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface LoggingServer extends Remote {
	
	public boolean logIn(Peer peer) throws RemoteException;
}
