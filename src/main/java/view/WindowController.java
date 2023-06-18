package view;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import model.Guest;
import model.Host;
import model.server.BookScrabbleHandler;
import model.server.MyServer;
import viewModel.ViewModel;
import viewModel.ViewModelGuest;

public class WindowController extends Observable  {

    public class PlayerInfo{

        private String userName;
        private int score;


        public PlayerInfo(String userName, int score){
            this.userName = userName;
            this.score = score;
        }

        public String getUserName() {
            return userName;
        }

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }

    }

    ViewModelGuest vm;

    public StringProperty[] t;

    public StringProperty currentTurn;

    Draggable draggableTile0, draggableTile1, draggableTile2, draggableTile3, draggableTile4, draggableTile5, draggableTile6;

    HBox hbox;

    VBox vboxTexts = new VBox();

    Guest guest;

    private ArrayList<PlayerInfo> playersInfo = new ArrayList<>();

    private String myUserName;

    private boolean isMyTurn = false;

    private boolean requestedChallenge = false;

    private String query;

    public static final int SIZE = 15;
    public static final int TILE_SIZE = 35;

    public static Stack<Draggable> tilesPlaced = new Stack<>();

    public static ArrayList<BoardTile> totalTilesInBoard = new ArrayList<>();

    public ArrayList<BoardTile> currentlyAdded = new ArrayList<>();

    ArrayList<String> guestsInTheGame = new ArrayList<>();


    @FXML
    ImageView imgTile0, imgTile1, imgTile2, imgTile3, imgTile4, imgTile5, imgTile6;

    @FXML
    private GridPane grid;

    @FXML
    private BorderPane borderPane;

    public void setMyUserName(String userName){
        myUserName = userName;
    }

    public void createBoardGame(){
        grid = new GridPane();
        grid.setAlignment(Pos.CENTER);

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                Rectangle rect = new Rectangle(TILE_SIZE, TILE_SIZE);
                rect.setFill(Color.GREEN);
                rect.setStroke(Color.BLACK);
                grid.add(rect, i, j);
            }
        }


        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                Rectangle rect = new Rectangle(TILE_SIZE, TILE_SIZE);
                Text TWS = new Text("TW");
                Text DWS = new Text("DW");
                Text TLS = new Text("TL");
                Text DLS = new Text("DL");
                Text Star=new Text("★");

                TWS.setFont(new Font(14));
                DWS.setFont(new Font(14));
                TLS.setFont(new Font(14));
                DLS.setFont(new Font(14));
                Star.setFont(new Font(27));

                //red: word triple tiles:
                if (i == 0) {
                    if (j == 0 || j == 7 || j == 14) {
                        rect.setFill(Color.RED);
                        rect.setStroke(Color.BLACK);

                        grid.add(rect, i, j);
                        grid.add(TWS,i,j);

                    }
                }
                if (i == 7) {
                    if (j == 0 || j == 14) {
                        rect.setFill(Color.RED);
                        rect.setStroke(Color.BLACK);

                        grid.add(rect, i, j);
                        grid.add(TWS,i,j);
                    }

                }
                if (i == 14) {
                    if (j == 0 || j == 7 || j == 14) {
                        rect.setFill(Color.RED);
                        rect.setStroke(Color.BLACK);

                        grid.add(rect, i, j);
                        grid.add(TWS,i,j);
                    }
                }

                // Yellow: double word score:
                if (i == 1) {
                    if (j == 1 || j == 13) {
                        rect.setFill(Color.YELLOW);
                        rect.setStroke(Color.BLACK);

                        grid.add(rect, i, j);
                        grid.add(DWS,i,j);
                    }
                }
                if (i == 2) {
                    if (j == 2 || j == 12) {
                        rect.setFill(Color.YELLOW);
                        rect.setStroke(Color.BLACK);

                        grid.add(rect, i, j);
                        grid.add(DWS,i,j);
                    }

                }
                if (i == 3) {
                    if (j == 3 || j == 11) {
                        rect.setFill(Color.YELLOW);
                        rect.setStroke(Color.BLACK);

                        grid.add(rect, i, j);
                        grid.add(DWS,i,j);
                    }

                }

                if (i == 4) {
                    if (j == 4 || j == 10) {
                        rect.setFill(Color.YELLOW);
                        rect.setStroke(Color.BLACK);

                        grid.add(rect, i, j);
                        grid.add(DWS,i,j);
                    }

                }

                if (i == 7 && j == 7) {
                    rect.setFill(Color.YELLOW);
                    rect.setStroke(Color.BLACK);
                    grid.add(rect, i, j);
                    grid.add(Star,i,j);


                }

                if (i == 13) {
                    if (j == 1 || j == 13) {
                        rect.setFill(Color.YELLOW);
                        rect.setStroke(Color.BLACK);
                        grid.add(rect, i, j);
                        grid.add(DWS,i,j);
                    }
                }

                if (i == 12) {
                    if (j == 2 || j == 12) {
                        rect.setFill(Color.YELLOW);
                        rect.setStroke(Color.BLACK);
                        grid.add(rect, i, j);
                        grid.add(DWS,i,j);
                    }
                }


                if (i == 11) {
                    if (j == 3 || j == 11) {
                        rect.setFill(Color.YELLOW);
                        rect.setStroke(Color.BLACK);
                        grid.add(rect, i, j);
                        grid.add(DWS,i,j);
                    }
                }

                if (i == 10) {
                    if (j == 4 || j == 10) {
                        rect.setFill(Color.YELLOW);
                        rect.setStroke(Color.BLACK);

                        grid.add(rect, i, j);
                        grid.add(DWS,i,j);
                    }

                }


                //blue:triple let score:

                if(i==1||i==13){
                    if(j==5||j==9){
                        rect.setFill(Color.BLUE);
                        rect.setStroke(Color.BLACK);
                        grid.add(rect, i, j);
                        grid.add(TLS,i,j);

                    }
                }

                if(i==5||i==9){
                    if(j==1||j==5||j==9||j==13){
                        rect.setFill(Color.BLUE);
                        rect.setStroke(Color.BLACK);
                        grid.add(rect, i, j);
                        grid.add(TLS,i,j);
                    }
                }

                // light blue:double let score:

                if(j==0||j==14){
                    if(i==3||i==11){
                        rect.setFill(Color.LIGHTBLUE);
                        rect.setStroke(Color.BLACK);
                        grid.add(rect, i, j);
                        grid.add(DLS,i,j);
                    }
                }

                if(j==2||j==12){
                    if(i==6||i==8){
                        rect.setFill(Color.LIGHTBLUE);
                        rect.setStroke(Color.BLACK);
                        grid.add(rect, i, j);
                        grid.add(DLS,i,j);
                    }
                }

                if(j==3||j==11){
                    if(i==0||i==7||i==14){
                        rect.setFill(Color.LIGHTBLUE);
                        rect.setStroke(Color.BLACK);
                        grid.add(rect, i, j);
                        grid.add(DLS,i,j);
                    }
                }

                if(j==6||j==8){
                    if(i==2||i==6||i==8||i==12){
                        rect.setFill(Color.LIGHTBLUE);
                        rect.setStroke(Color.BLACK);
                        grid.add(rect,i, j);
                        grid.add(DLS,i,j);
                    }
                }

            }
        }

        borderPane.setCenter(grid);

    }

    public void setViewModel(ViewModelGuest vm){
        this.vm = vm;

        t = new SimpleStringProperty[7];
        for(int i = 0; i < t.length; i++){
            t[i] = new SimpleStringProperty();
            t[i].bind(vm.tiles[i]);
        }

        currentTurn = new SimpleStringProperty();
        currentTurn.bind(vm.currentGuestTurn);

        createBoardGame();

        draggableTile0 = new Draggable(grid,imgTile0, t[0]);
        draggableTile0.makeDraggable();

        draggableTile1 = new Draggable(grid,imgTile1, t[1]);
        draggableTile1.makeDraggable();

        draggableTile2 = new Draggable(grid,imgTile2, t[2]);
        draggableTile2.makeDraggable();

        draggableTile3 = new Draggable(grid,imgTile3, t[3]);
        draggableTile3.makeDraggable();

        draggableTile4 = new Draggable(grid,imgTile4, t[4]);
        draggableTile4.makeDraggable();

        draggableTile5 = new Draggable(grid,imgTile5, t[5]);
        draggableTile5.makeDraggable();

        draggableTile6 = new Draggable(grid,imgTile6, t[6]);
        draggableTile6.makeDraggable();

        hbox = (HBox) borderPane.getBottom();

        guest = new Guest(this);
        guest.connectToHost();
        guest.sendWordToHost(myUserName); //telling the host my username
    }


    public void generate(){

        if(draggableTile0.getInGrid() || draggableTile1.getInGrid() || draggableTile2.getInGrid() || draggableTile3.getInGrid()
                || draggableTile4.getInGrid() || draggableTile5.getInGrid() || draggableTile6.getInGrid()){
            return;
        }

        vm.generateRandomTiles();
        Image image0 = new Image(getClass().getResourceAsStream("images/"+t[0].get()+"_tile.jpg"));
        Image image1 = new Image(getClass().getResourceAsStream("images/"+t[1].get()+"_tile.jpg"));
        Image image2 = new Image(getClass().getResourceAsStream("images/"+t[2].get()+"_tile.jpg"));
        Image image3 = new Image(getClass().getResourceAsStream("images/"+t[3].get()+"_tile.jpg"));
        Image image4 = new Image(getClass().getResourceAsStream("images/"+t[4].get()+"_tile.jpg"));
        Image image5 = new Image(getClass().getResourceAsStream("images/"+t[5].get()+"_tile.jpg"));
        Image image6 = new Image(getClass().getResourceAsStream("images/"+t[6].get()+"_tile.jpg"));

        imgTile0.setImage(image0);
        imgTile1.setImage(image1);
        imgTile2.setImage(image2);
        imgTile3.setImage(image3);
        imgTile4.setImage(image4);
        imgTile5.setImage(image5);
        imgTile6.setImage(image6);

    }


    public void initialize() {

    }

    public void undoPlacement() {
        if(!tilesPlaced.isEmpty()){
            Draggable currentDraggable = tilesPlaced.pop();
            currentDraggable.undoPlacement();
            hbox.getChildren().add(currentDraggable.getImageView());
        }
    }

    public boolean checkHorizontalLinkedTiles(){
        boolean foundLinkedTile = false;
        BoardTile firstTile = Draggable.boardTiles.get(0);
        if(firstTile.getCol() != 14){
            for(int nextCol = firstTile.getCol()+1; nextCol < 14; nextCol++){
                if(BoardTile.contains(Draggable.boardTiles, firstTile.getRow(), nextCol)){
                    continue;
                }
                else if(BoardTile.contains(totalTilesInBoard, firstTile.getRow(), nextCol)){
                    foundLinkedTile = true;
                    BoardTile t = BoardTile.getTile(firstTile.getRow(), nextCol, totalTilesInBoard);
                    Draggable.boardTiles.add(t);
                    // currentlyAdded.add(t);
                }
                else{
                    break;
                }

            }
        }


        if(firstTile.getCol() != 0){
            for(int nextCol = firstTile.getCol()-1; nextCol != 0; nextCol--){
                if(BoardTile.contains(Draggable.boardTiles, firstTile.getRow(), nextCol)){
                    continue;
                }
                else if(BoardTile.contains(totalTilesInBoard, firstTile.getRow(), nextCol)){
                    foundLinkedTile = true;
                    BoardTile t = BoardTile.getTile(firstTile.getRow(), nextCol, totalTilesInBoard);
                    Draggable.boardTiles.add(t);
                    // currentlyAdded.add(t);
                }
                else{
                    break;
                }
            }
        }

        return foundLinkedTile;
    }

    public boolean checkVerticalLinkedTiles(){
        boolean foundLinkedTile = false;
        BoardTile firstTile = Draggable.boardTiles.get(0);
        if(firstTile.getRow() != 14){
            for(int nextRow = firstTile.getRow()+1; nextRow < 14; nextRow++){
                if(BoardTile.contains(Draggable.boardTiles, nextRow, firstTile.getCol())){
                    continue;
                }
                else if(BoardTile.contains(totalTilesInBoard, nextRow, firstTile.getCol())){
                    foundLinkedTile = true;
                    BoardTile t = BoardTile.getTile(nextRow, firstTile.getCol(), totalTilesInBoard);
                    Draggable.boardTiles.add(t);
                    //currentlyAdded.add(t);

                }
                else{
                    break;
                }

            }
        }


        if(firstTile.getRow() != 0){
            for(int nextRow = firstTile.getRow()-1; nextRow != 0 ; nextRow--){
                if(BoardTile.contains(Draggable.boardTiles, nextRow, firstTile.getCol())){
                    continue;
                }
                else if(BoardTile.contains(totalTilesInBoard, nextRow, firstTile.getCol())){
                    BoardTile t = BoardTile.getTile(nextRow, firstTile.getCol(), totalTilesInBoard);
                    Draggable.boardTiles.add(t);
                    //currentlyAdded.add(t);
                }
                else{
                    break;
                }

            }
        }

        return foundLinkedTile;
    }

    public void submitWord(){

        if(Draggable.boardTiles.size() == 0){
            System.out.println("No tiles been entered");
            return;
        }
        String w = new String("");


        boolean vertical = checkVertical();
        boolean horizontal = false;

        if (!vertical)
            horizontal = checkHorizontal();


        if(!vertical && !horizontal){
            System.out.println("not vertical & not horizontal");

            while(!tilesPlaced.isEmpty()) {
                Draggable d = tilesPlaced.pop();
                d.undoPlacement();
                hbox.getChildren().add(d.getImageView());
            }
            return;
        }


        for(int i = 0; i < Draggable.boardTiles.size(); i++){
            w+=Draggable.boardTiles.get(i).getLetter(); //building the word
            StringProperty s = new SimpleStringProperty();
            s.setValue(String.valueOf(w.charAt(i)));
            if(!BoardTile.contains(totalTilesInBoard, Draggable.boardTiles.get(i).getRow(), Draggable.boardTiles.get(i).getCol())){
                BoardTile t = new BoardTile(s,Draggable.boardTiles.get(i).getRow(), Draggable.boardTiles.get(i).getCol());
                totalTilesInBoard.add(t);
                currentlyAdded.add(t);
            }
        }


        if(w.length() <= 1){
            System.out.println("please enter word");
            return;
        }

        System.out.println(w);

        int row = Draggable.boardTiles.get(0).getRow();
        int col = Draggable.boardTiles.get(0).getCol();
        String isVertical = vertical ? "true":"false";
        query = myUserName+","+ w +","+row+","+col+","+isVertical;
        System.out.println(query);
        guest.sendWordToHost(query);
    }



    public void addUserName(String uname){

        //display all the players
        int n = uname.split(",").length;
        for(int i = 0; i < n; i++){
            String user = uname.split(",")[i];
            if(!guestsInTheGame.contains(user)){
                guestsInTheGame.add(user);
                playersInfo.add(new PlayerInfo(user, 0));
                borderPane.setLeft(vboxTexts);

                Text text = new Text(user+":0");
                text.setFont(Font.font("Ariel", 18));
                vboxTexts.getChildren().add(text);
            }
        }

        vboxTexts.getChildren().removeIf(node->node instanceof Label);
        Label label = new Label();
        label.setText("turn:"+guestsInTheGame.get(0));
        if(guestsInTheGame.get(0).equals(myUserName)){
            isMyTurn = true;
            hbox.setVisible(true);
        } else {
            hbox.setVisible(false);
        }

        vboxTexts.getChildren().add(label);

    }

    public void display(){
        for(PlayerInfo pi: playersInfo){
            Text text = new Text(pi.userName+":"+pi.score);
            text.setFont(Font.font("Ariel", 18));
            vboxTexts.getChildren().add(text);
        }
    }

    public void updateScore(String uname, int score){
        for(PlayerInfo pi: playersInfo){
            if(pi.userName.equals(uname))
                pi.setScore(pi.getScore() + score);
        }
        vboxTexts.getChildren().removeIf(node->node instanceof Text);
        display();


    }

    public AtomicBoolean showChallengeOptionWindow() {
        AtomicBoolean choice = new AtomicBoolean(false);
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Challenge option");
        dialog.getDialogPane().setContentText("The word is not legal on the dictionary\n Do you want to challenge?");
        dialog.getDialogPane().setStyle("-fx-font-size: 18px;");

        dialog.getDialogPane().setPrefWidth(400);
        dialog.getDialogPane().setPrefHeight(150);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.YES, ButtonType.NO);

        dialog.showAndWait().ifPresent(result -> {
            if (result == ButtonType.YES)
                choice.set(true);
        });

        return choice;
    }



    public void handleAnswer(String answer){
        String ans = answer.split(",")[0].split(":")[1];

        if(answer.split(",")[0].split(":")[0].equals("Dictionary") && ans.equals("false") && isMyTurn && requestedChallenge == false){
            if(showChallengeOptionWindow().get()){
                //Guest requesting a challenge
                requestedChallenge = true;
                guest.challengeWord(query);
                return;
            }
        }

        System.out.println(answer);

        if(isMyTurn){
            handleCurrentGuest(answer);
            requestedChallenge = false;
        }


        if(!isMyTurn && ans.equals("true")){
            placeWordForOthers(answer);
            String turnStatus = answer.split(",")[7]+","+answer.split(",")[8];
            checkNextTurn(turnStatus);
        }

    }

    public void placeWordForOthers(String answer){
        int score = Integer.parseInt(answer.split(",")[1]);
        String uname = answer.split(",")[2];
        String word = answer.split(",")[3];
        int row = Integer.parseInt(answer.split(",")[4]);
        int col = Integer.parseInt(answer.split(",")[5]);
        String vertical = answer.split(",")[6];

        int r = row;
        int co = col;

        for(int i = 0; i<word.length(); i++){
            StringProperty s = new SimpleStringProperty();
            s.setValue(String.valueOf(word.charAt(i)));
            if(vertical.equals("true")){
                totalTilesInBoard.add(new BoardTile(s, r++, co));
            }
            else{
                totalTilesInBoard.add(new BoardTile(s, r, co++));
            }
        }

        for(Character c: word.toCharArray()){
            ImageView imageView = new ImageView();
            imageView.setImage(new Image(getClass().getResourceAsStream("images/"+c+"_tile.jpg")));
            imageView.setFitHeight(35);
            imageView.setFitWidth(35);
            if(vertical.equals("true"))
                grid.add(imageView, col, row++);
            else
                grid.add(imageView, col++, row);
        }
        updateScore(uname, score);
    }

    public void handleCurrentGuest(String answer){
        String ans = answer.split(",")[0].split(":")[1];
        int score = Integer.parseInt(answer.split(",")[1]);
        String uname = answer.split(",")[2];

        //Guest who entered the tiles
        int i = Draggable.boardTiles.size()-1;
        while(!tilesPlaced.isEmpty()){
            Draggable d = tilesPlaced.pop();
            String letter = d.getLetter().get();
            int colStr = d.col;
            int rowStr = d.row;
            i--;
            d.undoPlacement();
            hbox.getChildren().add(d.getImageView());

            if(ans.equals("true")){
                ImageView imageView = new ImageView();
                imageView.setImage(new Image(getClass().getResourceAsStream("images/"+letter+"_tile.jpg")));
                imageView.setFitHeight(35);
                imageView.setFitWidth(35);
                d.getGrid().add(imageView, colStr, rowStr);
            }
        }

        if(ans.equals("false"))
            totalTilesInBoard.removeAll(currentlyAdded);
        else{
            updateScore(uname, score);
            String turnStatus = answer.split(",")[7]+","+answer.split(",")[8];
            checkNextTurn(turnStatus);
        }

        if(!Draggable.boardTiles.isEmpty())
            Draggable.boardTiles.clear();
        currentlyAdded.clear();

    }

    public boolean checkHorizontal(){
        checkHorizontalLinkedTiles();
        if(Draggable.boardTiles.size() == 1)
            return false;
        Draggable.boardTiles.sort(Comparator.comparingInt(BoardTile::getCol));
        BoardTile[] bt = Draggable.boardTiles.toArray(new BoardTile[0]);
        for(int i = 0; i < bt.length-1; i++){
            if(bt[i].getRow() != bt[i+1].getRow() || bt[i].getCol()+1 != bt[i+1].getCol()){
                Draggable.boardTiles.removeAll(currentlyAdded);
                currentlyAdded.clear();
                return false;
            }
        }
        return true;
    }

    public boolean checkVertical(){
        checkVerticalLinkedTiles();
        if(Draggable.boardTiles.size() == 1)
            return false;
        Draggable.boardTiles.sort(Comparator.comparingInt(BoardTile::getRow));
        BoardTile[] bt = Draggable.boardTiles.toArray(new BoardTile[0]);
        for(int i = 0; i < bt.length-1; i++){
            if(bt[i].getCol() != bt[i+1].getCol() || bt[i].getRow()+1 != bt[i+1].getRow()){
                Draggable.boardTiles.removeAll(currentlyAdded);
                currentlyAdded.clear();
                return false;
            }
        }
        return true;
    }

    public void checkNextTurn(String turnStatus){
        String lastTurn = turnStatus.split(",")[0].split(":")[1];
        String nextTurn = turnStatus.split(",")[1].split(":")[1];
        if(lastTurn.equals(myUserName)){
            isMyTurn = false;
            hbox.setVisible(false);
        }
        else if(nextTurn.equals(myUserName)){
            isMyTurn = true;
            hbox.setVisible(true);
        }
        vboxTexts.getChildren().removeIf(node->node instanceof Label);
        Label label = new Label();
        label.setText("turn:"+nextTurn);
        vboxTexts.getChildren().add(label);
    }


    public void skipTurn() {
        if(isMyTurn){
            try{
                vm.out.write("nextTurn");
                vm.out.newLine();
                vm.out.flush();
            }catch (IOException e){e.printStackTrace();}
        }

        isMyTurn = false;
        vm.nextTurn();
        vboxTexts.getChildren().removeIf(node->node instanceof Label);
        Label label = new Label();
        label.setText("turn:"+currentTurn.get());
        if(currentTurn.get().equals(myUserName)){
            isMyTurn = true;
            hbox.setVisible(true);
        }
        else {
            hbox.setVisible(false);
        }
        vboxTexts.getChildren().add(label);

    }


}
