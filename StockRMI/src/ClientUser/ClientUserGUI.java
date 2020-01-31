package ClientUser;

import Constant.AddressConstants;
import Interface.Notifiable;
import Interface.StockRMI;
import javafx.application.Application;
import javafx.application.Platform;
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
import java.rmi.server.UnicastRemoteObject;

public class ClientUserGUI extends Application implements Notifiable, Serializable {

    private String username;
    private Button subscribeButton;
    private Button unsubscribeButton;
    private Text enterCompanyNameDisplayText;
    private Text stockPriceUpdateText;
    private TextField companyNameTextField;

    private Button submitButton;
    private Text enterUsernameDisplayText;
    private TextField usernameTextField;

    private StockRMI stockRMI;


    private void subscribe(String username, String companyName, StockRMI stockRMI) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    boolean isNotAlreadySubscribed = stockRMI.subscribe(username, companyName);

                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            if (isNotAlreadySubscribed) {
                                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Subscribed successfully", ButtonType.OK);
                                alert.showAndWait();
                            } else {
                                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "User is already subscribed to the stock", ButtonType.OK);
                                alert.showAndWait();
                            }                        }
                    });
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void unsubscribe(String username, String companyName, StockRMI stockRMI) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    boolean isSubscribed = stockRMI.unSubscribe(username, companyName);

                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            if (isSubscribed) {
                                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Unsubscribed successfully", ButtonType.OK);
                                alert.showAndWait();
                            } else {
                                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "User is already unsubscribed to the stock", ButtonType.OK);
                                alert.showAndWait();
                            }                        }
                    });


                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void setupListeners() {
        this.subscribeButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                subscribe(username, companyNameTextField.getText(), stockRMI);
            }
        });

        this.unsubscribeButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                unsubscribe(username, companyNameTextField.getText(), stockRMI);
            }
        });
    }

    private GridPane setupSubscriptionGridPane() {
        //creating display label company name
        this.enterCompanyNameDisplayText = new Text("Enter name of company : ");

        //creating label company name
        this.stockPriceUpdateText = new Text("");

        //Creating Text Filed for company name
        this.companyNameTextField = new TextField();

        //Creating Buttons
        this.subscribeButton = new Button("Subscribe");
        this.unsubscribeButton = new Button("Unsubscribe");

        //Setup listeners
        setupListeners();

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
        gridPane.add(this.enterCompanyNameDisplayText, 0, 0);
        gridPane.add(this.companyNameTextField, 1, 0);

        gridPane.add(this.subscribeButton, 0, 2);
        gridPane.add(this.unsubscribeButton, 1, 2);

        gridPane.add(this.stockPriceUpdateText, 0, 3);

        //Styling nodes
        this.subscribeButton.setStyle("-fx-background-color: darkslateblue; -fx-text-fill: white;");
        this.unsubscribeButton.setStyle("-fx-background-color: darkslateblue; -fx-text-fill: white;");

        this.enterCompanyNameDisplayText.setStyle("-fx-font: normal bold 20px 'serif' ");
        gridPane.setStyle("-fx-background-color: BEIGE;");

        return gridPane;
    }

    private void setupSubscriptionScene(Stage stage) {
        //Register user
        register();

        //Creating a scene object
        Scene scene = new Scene(setupSubscriptionGridPane());

        //Setting title to the Stage
        stage.setTitle("Welcome " + username);

        //Adding scene to the stage
        stage.setScene(scene);

        //Displaying the contents of the stage
        stage.show();
    }

    private void register() {
        try {
            this.stockRMI.registerCallBack(this, username);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void setupSubmitListener(Stage stage) {
        this.submitButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                username = usernameTextField.getText();
                setupSubscriptionScene(stage);
            }
        });
    }

    private GridPane setupLoginGridPane(Stage stage) {
        //creating display label username name
        this.enterUsernameDisplayText = new Text("Enter username : ");

        //Creating Text Filed for username name
        this.usernameTextField = new TextField();

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
        gridPane.add(this.enterUsernameDisplayText, 0, 0);
        gridPane.add(this.usernameTextField, 1, 0);

        gridPane.add(this.submitButton, 0, 2);

        //Styling nodes
        this.submitButton.setStyle("-fx-background-color: darkslateblue; -fx-text-fill: white;");

        this.enterUsernameDisplayText.setStyle("-fx-font: normal bold 20px 'serif' ");
        gridPane.setStyle("-fx-background-color: BEIGE;");

        return gridPane;
    }

    private void setupLoginScreen(Stage stage) {
        //Creating a scene object
        Scene scene = new Scene(setupLoginGridPane(stage));

        //Setting title to the Stage
        stage.setTitle("Login Form");

        //Adding scene to the stage
        stage.setScene(scene);

        //Displaying the contents of the stage
        stage.show();
    }

    @Override
    public void start(Stage stage) {
        try {

            Registry registry = LocateRegistry.getRegistry(AddressConstants.PORT);
            this.stockRMI = (StockRMI) registry.lookup(AddressConstants.STOCK_SERVICE_URL);


            UnicastRemoteObject.exportObject(this, 0);

            setupLoginScreen(stage);

            //setupSubscriptionScene(stage);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() throws Exception {
        super.stop();
    }

    public static void main(String args[]){
        launch(args);
    }

    @Override
    public void notify(String stockSym, double price) throws RemoteException {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                stockPriceUpdateText.setText(stockSym + " stock price changed to Rs. " + price);
            }
        });
    }

    @Override
    public void exit() throws RemoteException {
        try {

            UnicastRemoteObject.unexportObject(this, true);
            System.out.println("StockPriceCallback exiting.");

        } catch (Exception e) {

            System.out.println("Exception thrown" + e);
        }
    }
}