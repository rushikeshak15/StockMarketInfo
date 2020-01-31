package Server;

//import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class StockRMIServer {

    public static final int PORT = 1099;

    public static void main(String args[]) {
    
        try {

            String hostname = "172.16.132.240";

            String bindloc = "//" + hostname + ":" + StockRMIServer.PORT + "/stockService";

            System.out.println(bindloc);
            System.out.println("STOCKRMISERVER");

            // create the servant
            StockRMIServant stockService = new StockRMIServant();
            System.out.println("Create StockRMIServant object");
            System.out.println("Placing in registry");

            // start up the rmiregistry and bind the servant to the registry
            Registry registry = LocateRegistry.createRegistry(StockRMIServer.PORT);

            registry.bind(bindloc, stockService);
            System.out.println("StockRMIServant object ready");
            System.out.println();

        } catch(Exception e) {
        
            System.out.println("Server error in main " + e.getMessage());
        }

    }

}