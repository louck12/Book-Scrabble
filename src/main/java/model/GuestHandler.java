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

    String guestName;

    volatile boolean stop = false;

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
            String msgFromGuest = inFromGuest.nextLine(); //challenge:Moby,4,5,true
            String data = msgFromGuest.split(",")[0]; //challenge:Moby
            String wordFromGuest;
            boolean guestChallenged = false;
            if(data.contains("challenge")){
                wordFromGuest = data.split(":")[1];
                guestChallenged = true;
            }
            else{
                wordFromGuest = msgFromGuest.split(",")[0];
            }
            int row = Integer.parseInt(msgFromGuest.split(",")[1]);
            int col = Integer.parseInt(msgFromGuest.split(",")[2]);
            String vertical = msgFromGuest.split(",")[3];
            boolean isVertical;
            isVertical = vertical.equals("true");
            this.guestName = msgFromGuest.split(",")[4];


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
                    outToServer.write("Q,mobydick.txt," + wordFromGuest + "\n");
                    outToServer.flush();
                    if (inFromServer.nextLine().equals("true"))
                        ansToGuest = wordFromGuest + " is legal on the board and on dictionary";
                    else
                        ansToGuest = wordFromGuest + " is not legal dictionary wise";
                }

                else{
                    //challenge
                    outToServer.write("C,mobydick.txt," + wordFromGuest + "\n");
                    outToServer.flush();
                    if (inFromServer.nextLine().equals("true"))
                        ansToGuest = "challenged successfully: " +wordFromGuest + " is legal on the dictionary";
                    else
                        ansToGuest = "Challenged unsuccessfully: "+wordFromGuest + " is not legal dictionary wise";
                }
            }
            else
                ansToGuest = wordFromGuest + " is not legal on the board";

            //sendAnswerToAllGuests(ansToGuest);
            String msg = "To: " + guestName + ", " +ansToGuest;
            outToGuest.println(msg);
            outToGuest.flush();
        }
    }


    private void sendAnswerToAllGuests(String ansToGuest) {
        for(GuestHandler gh: Host.guestHandlers){
            String msg = "To: " + gh.guestName + ", " +ansToGuest;
            System.out.println(msg);
            gh.outToGuest.println(msg);
            gh.outToGuest.flush();
        }
    }

    @Override
    public void close() {
        inFromGuest.close();
        outToGuest.close();
    }

}