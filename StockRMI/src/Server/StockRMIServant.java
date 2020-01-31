package Server;


import Interface.Notifiable;
import Interface.StockRMI;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class StockRMIServant extends UnicastRemoteObject implements StockRMI {

    private static Map <String, ArrayList <String>> stocks = new HashMap();	// <Key: stockSym, Value: price, users subscribed to the stock(subscription list)>
    
    private static Map users = new HashMap();	// <Key: user, Value: Callback Object>

    protected StockRMIServant() throws RemoteException {
    }

    @Override
    public boolean subscribe(String user, String stockSym) throws RemoteException {	// Called by user to subscribe to a stock
    
        if (stocks.containsKey(stockSym)) {	// if stock exists in stocks, add user to index 1 (2nd position) in the stock's info.
            
            ArrayList <String> stockInfo = stocks.get(stockSym);
            
            if (!stockInfo.contains(user)) {	// if user hasn't already subscribed to the stock, add them to the subscription list
                
                stockInfo.add(1, user);
                System.out.println(user + " has subscribed to stock " + stockSym);
                
                return true;
                
            } else {
            
            	return false;
            }

        } else {	// if the stock does not exist in stocks, set the price of stock to -1 and add user to subscription list.
        
            ArrayList <String> stockInfo = new ArrayList<>();
            
            stockInfo.add(0, "-1");
            stockInfo.add(1, user);
            
            stocks.put(stockSym, stockInfo);
            System.out.println(user + " has subscribed to stock " + stockSym);
            
            return true;
        }
    }

    @Override public boolean unSubscribe(String user, String stockSym) throws RemoteException {	// called by the user to unsubscribe to a stock
    
        if (stocks.containsKey(stockSym)) {	// if stock named stockSym exists in stocks, remove user from its subscription list

            ArrayList <String> stockInfo = stocks.get(stockSym);

            if (stockInfo.contains(user)) {
                stockInfo.remove(user);
                stocks.put(stockSym, stockInfo);

                System.out.println(user + " has unsubscribed to stock " + stockSym);

                return true;
            } else {
                return false;
            }

        } else {	// if stock doesn't exist in stocks

            return false;
        }
    }

    @Override
    public void stockUpdate(String stockSym, double price) throws RemoteException {	// Update price and notify all users in subscription list; called by company/ priceUpdate client
        
        if (stocks.containsKey(stockSym)) {	// if the stock and its info already exists in stocks,then update its original price to new price
            
            ArrayList <String> stockInfo = stocks.get(stockSym);
            
            stockInfo.set(0, "" + price);
	    stocks.put(stockSym, stockInfo);

            System.out.println("Price of stock " + stockSym + " updated.");
            
            doCallBack(stockSym);
        
        } else {	// if the stock and its info do not exist in stocks, then add the stock to the 
            
            ArrayList<String> stockInfo = new ArrayList<>();
            
            stockInfo.add(0, "" + price);
            stocks.put(stockSym, stockInfo);
            
            System.out.println("New stock " + stockSym + " has been added to the database.");
        }
    }

    @Override
    public void registerCallBack(Notifiable remoteClient, String user) throws RemoteException {	// called by Callback Object to register client with the server
        
        if (!(users.containsKey(user))) {	// store the callback object into users
            
            users.put(user, remoteClient);
            
            System.out.println("Registered new client:" + user);
        }
    }

    @Override
    public void deRegisterCallBack(String user) throws RemoteException {	// called by user client when it terminates
        
        Notifiable remoteClient = (Notifiable) users.get(user);
        remoteClient.exit();
        
        System.out.println("Client " + user + " exits");
    }

    private void doCallBack(String stockSym) throws RemoteException {	// notifies every user in subscription list of change in stock price
    
        ArrayList <String> stockInfo = stocks.get(stockSym);

        double price = Double.parseDouble(stockInfo.get(0));
        
        if (stockInfo.size() > 1) { // there is at least one user in the subscription list.
        
            for (int i = 1; i < stockInfo.size(); i++) {
        
                String user = (String) stockInfo.get(i);
                Notifiable remoteClient = (Notifiable) users.get(user);


                remoteClient.notify(stockSym, price);




            }
        }
    }

}