package Interface;


import java.rmi.*;

public interface Notifiable extends Remote {

	public void notify(String stockSym, double price) throws RemoteException;

	public void exit() throws RemoteException;
}
