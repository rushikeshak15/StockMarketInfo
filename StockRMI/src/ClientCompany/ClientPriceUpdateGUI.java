package ClientCompany;

import Constant.AddressConstants;
import Interface.StockRMI;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;

public class ClientPriceUpdateGUI extends Application implements Serializable {

    private String companyName;
    private double price;
    
    private StockRMI stockRMI;
    private Text companyNameDisplayText;
    private TextField companyNameTextField;
    private Text priceDisplayText;
    private TextField priceTextField;
    private Button submitButton;

    private GridPane setupUpdateGridPane(Stage stage) {
        
        //creating display label for company name
        this.companyNameDisplayText = new Text("Enter name of company: ");

        //Creating Text Field for company name
        this.companyNameTextField = new TextField();

        //creating display label for stock price
        this.priceDisplayText = new Text("Enter price of stock: ");
        
        //creating Text Field for stock price
        this.priceTextField = new TextField();
        
        
        //Creating Buttons
        this.submitButton = new Button("Submit");

        setupSubmitListener(stage);

        //Creating a Grid Pane
        GridPane gridPane = new GridPane();

        //Setting size for the pane
        gridPane.setMinSize(400, 200);

        //Setting the padding
        gridPane.setPadding(new Insets(10, 10, 10, 10));

        //Setting the vertical and horizontal gaps between the columns
        gridPane.setVgap(5);
        gridPane.setHgap(5);

        //Setting the Grid alignment
        gridPane.setAlignment(Pos.CENTER);

        //Arranging all the nodes in the grid
        gridPane.add(this.companyNameDisplayText, 0, 0);
        gridPane.add(this.companyNameTextField, 1, 0);

        gridPane.add(this.priceDisplayText, 0, 2);
        gridPane.add(this.priceTextField, 1, 2);
        
        gridPane.add(this.submitButton, 0, 4);

        //Styling nodes
        this.submitButton.setStyle("-fx-background-color: darkslateblue; -fx-text-fill: white;");

        this.companyNameDisplayText.setStyle("-fx-font: normal bold 20px 'serif' ");
        this.priceDisplayText.setStyle("-fx-font: normal bold 20px 'serif' ");
        gridPane.setStyle("-fx-background-color: BEIGE;");

        return gridPane;
    }
    
    private void setupUpdateScreen(Stage stage) {
        
        Scene scene = new Scene(setupUpdateGridPane(stage));

        //Setting title to the Stage
        stage.setTitle("Price Updation Form");

        //Adding scene to the stage
        stage.setScene(scene);

        //Displaying the contents of the stage
        stage.show();

    }
    
    @Override
    public void start(Stage stage) throws Exception {
        try {

            Registry registry = LocateRegistry.getRegistry(AddressConstants.PORT);
            this.stockRMI = (StockRMI) registry.lookup(AddressConstants.STOCK_SERVICE_URL);

            setupUpdateScreen(stage);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
    }

    private void setupSubmitListener(Stage stage) {
        this.submitButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                companyName = companyNameTextField.getText();
                price = Double.parseDouble(priceTextField.getText());
                
                update(stockRMI);
            }
        });
    }
    
    private void update(StockRMI stockRMI) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    stockRMI.stockUpdate(companyName, price);
                    
                    Platform.runLater(new Runnable(){
                        @Override
                        public void run() {
                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, companyName + " is now priced at " + price + ". Sending updates of " + companyName + " to subscribers.", ButtonType.OK);
                            alert.showAndWait();
                        }
                    });
                    
                    
                   
                } catch (RemoteException ex) {
                    Logger.getLogger(ClientPriceUpdateGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }).start();
    }
    
    @Override
    public void stop() throws Exception {
        super.stop();
    }

    public static void main(String args[]){
        launch(args);
    }
    
}
