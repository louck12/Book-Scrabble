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

        while (true) {
            String msgFromGuest = inFromGuest.nextLine();
            String wordFromGuest = msgFromGuest.split(",")[0];
            System.out.println(wordFromGuest);
            this.guestName = msgFromGuest.split(",")[1];
            System.out.println(wordFromGuest);

            if (wordFromGuest.equals("quit")) {
                try {
                    System.out.println("here on quit");
                    Host.guestHandlers.remove(this);
                    this.guestSocket.close();
                    break;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            //check if the word is boardLegal
            Board board = Board.getBoard();
            Tile[] tiles = new Tile[wordFromGuest.length()];
            for (int j = 0; j < wordFromGuest.length(); j++) {
                tiles[j] = Tile.Bag.getBag().getTile(wordFromGuest.charAt(j));
            }
            Word word = new Word(tiles, 5, 7, true);
            String ansToGuest;
            if (board.boardLegal(word)) {
                //Ask server if the word is dictionary legal
                outToServer.write("Q,mobydick.txt," + wordFromGuest + "\n");
                outToServer.flush();

                if (inFromServer.nextLine().equals("true"))
                    //the word is legal dictionary wise
                    ansToGuest = wordFromGuest + " is legal on the board and on dictionary";

                else
                    ansToGuest = wordFromGuest + " is not legal dictionary wise";

            } else
                ansToGuest = wordFromGuest + " is not legal on the board";


            System.out.println(ansToGuest);

            sendAnswerToAllGuests(ansToGuest);
        }
    }

        //out.println(ansToGuest); //Returning the response to the guest
        //out.flush();


    private void sendAnswerToAllGuests(String ansToGuest) {
        for(GuestHandler gh: Host.guestHandlers){
            String name = gh.guestName;
            gh.outToGuest.println("To: " + name + ", " +ansToGuest);
            gh.outToGuest.flush();
        }
    }

    @Override
    public void close() {
        inFromGuest.close();
        outToGuest.close();
    }

}