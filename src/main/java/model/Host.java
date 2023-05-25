package model;

import model.board.Board;
import model.board.Tile;
import model.board.Word;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Host {

    private static final int MAX_GUESTS = 3;
    public String ip;
    public int port;

    BufferedWriter outToServer;
    BufferedReader inFromServer;

    Board board;

    public Host(int port){
        this.ip = "localhost";
        this.port = port;

        //Connect to the main server
        try{
            Socket socket = new Socket("localhost", 1234);
            inFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            outToServer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e){e.printStackTrace();}
    }

    public void hostMode(){
        //Opening a local server socket that allows guest players connect to it
        try{
            ServerSocket serverSocket = new ServerSocket(port); //ip --> localhost | 127.0.0.1
            System.out.println("Host Server Is Running...");
            for(int i = 0; i < MAX_GUESTS; i++){ //Handling ONLY 3 guests
                Socket guestSocket = serverSocket.accept(); //guest connected to the host
                PrintWriter outToGuest = new PrintWriter(guestSocket.getOutputStream());
                Scanner inFromGuest = new Scanner(guestSocket.getInputStream());


                System.out.println("A Guest Just Connected");
                String wordFromGuest = inFromGuest.nextLine(); //waiting for guest request

                //check if the word is boardLegal
                Board board = Board.getBoard();
                Tile[] tiles = new Tile[wordFromGuest.length()];
                for(int j = 0; j < wordFromGuest.length(); j++){
                    tiles[j] = Tile.Bag.getBag().getTile(wordFromGuest.charAt(j));
                }
                Word word = new Word(tiles, 5, 7, true);
                String ansToGuest;
                if(board.boardLegal(word)){
                    //Ask server if the word is dictionary legal
                    outToServer.write("Q,mobydick.txt,"+wordFromGuest);
                    outToServer.newLine();
                    outToServer.flush();

                    if(inFromServer.readLine().equals("true")){
                        //the word is legal dictionary wise
                        ansToGuest = wordFromGuest +" is legal on the board and on dictionary";
                    }

                    else{
                        ansToGuest = wordFromGuest+ " is not legal dictionary wise";
                    }
                }

                else{
                    ansToGuest = wordFromGuest+ " is not legal on the board";
                }

                outToGuest.println(ansToGuest); //Returning the response to the guest
                outToGuest.flush();

            }

            //Prevent more guests from connecting to the host
            while(!serverSocket.isClosed()){
                Socket blockedGuestSocket = serverSocket.accept();
                System.out.println("Blocked additional guest from connecting");
                blockedGuestSocket.close();
            }
        } catch (IOException e){e.printStackTrace();}
    }

    public static void main(String[] args) {
        Host host = new Host(8000);
        host.hostMode();
    }

}
