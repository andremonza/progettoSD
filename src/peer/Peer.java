package peer;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Peer extends Remote {
	
	public void setName(String name) throws RemoteException;
	public String getName() throws RemoteException;
	public String getAddr() throws RemoteException;
	
	public void setNeighbor(Peer neighbor) throws RemoteException;
	public void wakeUp(int value) throws RemoteException;
}
