import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import sun.misc.resources.Messages;

public class FXNet extends Application{


    private NetworkConnection  conn = createServer();
    //creating label port
    Text Port = new Text("Port");//port text


    //Creating a Grid Pane
    GridPane gridPane = new GridPane();

    ObservableList<String> names = FXCollections.observableArrayList(
            "LeaderBoard"+" "+" "+"playerID"+" "+" "+" Points"," ");
    ListView<String> Leader = new ListView<String>(names);//observable list for the leaderboard

    //Label for name

    //Text field for name
    TextField serverText = new TextField();
    TextField portNum=new TextField();
    private Text title = new Text("BINGO");
    Button connectServerBtn =new Button("Connect");//takes the data from theportnumber textField
    Button Exit =new Button("Exit");


    TextField clientNo = new TextField();
    Text clientNumber = new Text("Number of Clients");//used as a label



    private Parent createContent() {
        //Setting size for the pane
       // gridPane.setMinSize(400, 200);



        //Setting the vertical and horizontal gaps between the columns
        gridPane.setVgap(5);
        gridPane.setHgap(5);




        Port.setStroke(Paint.valueOf("Green"));
        title.setFont(Font.font("Tahoma", FontWeight.NORMAL, 50));

        GridPane.setConstraints(title, 1, 2, 5, 2, HPos.CENTER, VPos.CENTER);
        GridPane.setConstraints(portNum,1,0,1,1, HPos.LEFT, VPos.CENTER);
        GridPane.setConstraints(serverText,5,5,15,15, HPos.CENTER, VPos.TOP);
        GridPane.setConstraints(clientNo,5,0,15,15,HPos.RIGHT,VPos.TOP);
        GridPane.setConstraints(clientNumber,4,0,5,5,HPos.CENTER,VPos.TOP);
        //Port and IP input fields
        GridPane.setConstraints(Port, 3, 0, 1, 1, HPos.RIGHT, VPos.CENTER);

        portNum.setOnAction(e->{
            Port.setText("Port: "+portNum.getText());
        });




        //Main buttons
        GridPane.setConstraints(Exit, 1, 9, 1, 1, HPos.CENTER, VPos.BOTTOM);
        GridPane.setConstraints(connectServerBtn, 2, 9, 2, 1, HPos.CENTER, VPos.BOTTOM);
        ;
        connectServerBtn.setStyle("-fx-background-color: darkslateblue; -fx-text-fill: white;");

        portNum.setStyle("-fx-font: normal bold 20px 'serif' ");

        gridPane.setStyle("-fx-background-color: White;");


        //Creating a scene object
        int textAreaSize = 150;

        serverText.setPrefHeight(textAreaSize);

       gridPane.getChildren().addAll(Port,portNum,Exit,connectServerBtn,title,clientNo,serverText,Leader);

        return gridPane;



    }

    public void setupButtons(){

        int buttonWidth = 150;
        int buttonHeight = 11;
        Exit.setMaxSize(buttonWidth, 50);
        connectServerBtn.setMaxSize(buttonWidth, buttonHeight);
        connectServerBtn.setDisable(true);
        Exit.setDisable(true);
    }
    public void newClient(){
       serverText.setText(conn.AllClients.size()+" "+"client connected");
    }
    private void interpretData(String data) {
        String[] parsedData = data.split(" ");

        switch (parsedData[0]){
            case "newClient":
                newClient();
                break;
            /*case "waiting":
                waitingForOthers();
                break;
            case "gameReady":
                setGameReady();
                break;
            case "leaderBoard":
                leaderBoard(parsedData);
                break;
            case "gameWinner":
                gameWinner(parsedData);
                break;
            case "newGame":
                newGame();
                break;*/
            case "connectionClosed":
                connectionClosed();
                break;
            default:
                break;
        }
    }

    private void connectionClosed(){
        clientNo.setText("Server connection closed ");
        connectServerBtn.setDisable(false);
    }
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // TODO Auto-generated method stub
        primaryStage.setScene(new Scene(createContent()));
        primaryStage.show();

    }

    @Override
    public void init() throws Exception{
        conn.startConn();
    }

    @Override
    public void stop() throws Exception{
        conn.closeConn();
    }

    private Server createServer() {
        return new Server(5556, data-> {
            Platform.runLater(()-> {
                String serverData = data.toString();
                System.out.println("Server: " + serverData);
                interpretData(serverData);
            });
        });
    }


}