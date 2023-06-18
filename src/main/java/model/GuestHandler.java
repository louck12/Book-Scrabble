package model;

import model.board.Board;
import model.board.Tile;
import model.board.Word;

import java.io.*;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

public  class GuestHandler implements ClientHandler{
    PrintWriter outToServer, outToGuest;
    Scanner inFromServer, inFromGuest;

    Socket guestSocket;

    Host host;


    public GuestHandler(Scanner inFromServer, PrintWriter outToServer, Socket guestSocket, Host host){
        this.inFromServer = inFromServer;
        this.outToServer = outToServer;
        this.guestSocket = guestSocket;
        this.host = host;
    }


    @Override
    public void handleClient(InputStream inFromClient, OutputStream outToClient) {
        this.outToGuest = new PrintWriter(outToClient);
        this.inFromGuest = new Scanner(inFromClient);
        int score = 0;
        //waiting for guest request

        while (guestSocket.isConnected()) {
            String msgFromGuest;
            try{
                msgFromGuest = inFromGuest.nextLine();
            } catch(NoSuchElementException e){
                break;
            }

            String data = msgFromGuest.split(",")[1];
            String wordFromGuest;
            boolean guestChallenged = false;
            if(data.contains("challenge")){
                wordFromGuest = data.split(":")[1];
                guestChallenged = true;
            }
            else{
                wordFromGuest = data;
            }
            int row = Integer.parseInt(msgFromGuest.split(",")[2]);
            int col = Integer.parseInt(msgFromGuest.split(",")[3]);
            String vertical = msgFromGuest.split(",")[4];
            boolean isVertical;
            isVertical = vertical.equals("true");


            //check if the word is boardLegal
            Board board = Board.getBoard();
            Tile[] tiles = new Tile[wordFromGuest.length()];


            for (int j = 0; j < wordFromGuest.length(); j++) {
                if(wordFromGuest.charAt(j) != '_')
                    tiles[j] = Tile.Bag.getBag().getTile(wordFromGuest.charAt(j));
            }
            Word word = new Word(tiles, row, col, isVertical);
            String ansToGuest = "";

            if (board.boardLegal(word)) {
                //Ask server if the word is dictionary legal

                if(!guestChallenged){
                    //query
                    score = board.tryPlaceWord(word, inFromServer, outToServer, false);
                    if(score == 0){
                        ansToGuest = "Dictionary:false";
                    }
                    else{
                        ansToGuest = "Dictionary:true";
                        board.print();
                    }
                   
                }

                else{
                    //challenge
                    outToServer.write("C,mobydick.txt,pg10.txt,shakespeare.txt," + wordFromGuest + "\n");
                    outToServer.flush();
                    if (inFromServer.nextLine().equals("true")){
                        score = board.tryPlaceWord(word, inFromServer, outToServer, true);
                        ansToGuest = "Dictionary:true";
                        //ansToGuest = "The word " +wordFromGuest+ " successfully challenged";
                    }
                    else{
                        //ansToGuest = "The word " +wordFromGuest+ " unsuccessfully challenged";
                        ansToGuest = "Dictionary:false";
                    }
                }
            }
            else{
                ansToGuest = "Board:false";
                //ansToGuest = "The word: " +wordFromGuest + " is not legal on the board.";
            }

            String msg = ansToGuest+","+score+","+msgFromGuest;

            if(ansToGuest.contains("false")){
                for(Tile t:tiles){
                    if(t != null)
                        Tile.Bag.getBag().put(t);
                }
            } else {
                String lastTurn = host.turnManager.getTurns().peek();
                String nextTurn = host.turnManager.nextTurn();
                msg = msg +",lastTurn:"+lastTurn+ ",nextTurn:"+nextTurn;
            }

            for(GuestHandler gh: Host.guestHandlers){
                gh.outToGuest.println(msg);
                gh.outToGuest.flush();
            }

        }

    }


    @Override
    public void close() {
        inFromGuest.close();
        outToGuest.close();
    }

}
