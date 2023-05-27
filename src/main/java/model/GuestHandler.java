package model;

import model.board.Board;
import model.board.Tile;
import model.board.Word;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public  class GuestHandler implements ClientHandler{
    PrintWriter outToServer, outToGuest;
    Scanner inFromServer, inFromGuest;

    Socket guestSocket;


    public GuestHandler(Scanner inFromServer, PrintWriter outToServer, Socket guestSocket){
        this.inFromServer = inFromServer;
        this.outToServer = outToServer;
        this.guestSocket = guestSocket;
    }


    @Override
    public void handleClient(InputStream inFromClient, OutputStream outToClient) {
        this.outToGuest = new PrintWriter(outToClient);
        this.inFromGuest = new Scanner(inFromClient);
        //waiting for guest request

        while (guestSocket.isConnected()) {
            if(!inFromGuest.hasNext())
                continue;
            String msgFromGuest = inFromGuest.nextLine(); //Moby,7,7,true
            String data = msgFromGuest.split(",")[0]; //Moby
            String wordFromGuest;
            boolean guestChallenged = false;
            if(data.contains("challenge")){
                wordFromGuest = data.split(":")[1];
                guestChallenged = true;
            }
            else{
                wordFromGuest = data;
            }
            int row = Integer.parseInt(msgFromGuest.split(",")[1]);
            int col = Integer.parseInt(msgFromGuest.split(",")[2]);
            String vertical = msgFromGuest.split(",")[3];
            boolean isVertical;
            isVertical = vertical.equals("true");


            //check if the word is boardLegal
            Board board = Board.getBoard();
            Tile[] tiles = new Tile[wordFromGuest.length()];
            for (int j = 0; j < wordFromGuest.length(); j++) {
                tiles[j] = Tile.Bag.getBag().getTile(wordFromGuest.charAt(j));
            }
            Word word = new Word(tiles, row, col, isVertical);
            String ansToGuest;

            if (board.boardLegal(word)) {
                //Ask server if the word is dictionary legal

                if(!guestChallenged){
                    //query
                    outToServer.write("Q,mobydick.txt,pg10.txt,shakespeare.txt," + wordFromGuest + "\n");
                    outToServer.flush();
                    if (inFromServer.nextLine().equals("true")){
                        ansToGuest = "The word " +wordFromGuest+" Dictionary Query: true";
                        board.tryPlaceWord(word);
                        System.out.println("The word " +wordFromGuest +" placed on the board");
                    }

                    else{
                        ansToGuest = "The word " +wordFromGuest+" Dictionary Query: false";
                    }

                }

                else{
                    //challenge
                    outToServer.write("Q,mobydick.txt,pg10.txt,shakespeare.txt," + wordFromGuest + "\n");
                    outToServer.flush();
                    if (inFromServer.nextLine().equals("true"))
                        ansToGuest = "The word " +wordFromGuest+ " Dictionary Challenge: true";
                    else
                        ansToGuest = "The word " +wordFromGuest+ " Dictionary Challenge: false";
                }
            }
            else
                ansToGuest = "The word: " +wordFromGuest + " is not legal on the board";

            String msg = ansToGuest;
            outToGuest.println(msg);
            outToGuest.flush();
        }
    }



    @Override
    public void close() {
        inFromGuest.close();
        outToGuest.close();
    }

}
