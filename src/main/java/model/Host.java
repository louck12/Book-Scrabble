package model;

import model.board.Board;
import model.board.Tile;
import model.board.Word;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Host {

    private static final int MAX_GUESTS = 3;
    public String ip;
    public int port;

    PrintWriter outToServer;
    Scanner inFromServer;


    public static ArrayList<GuestHandler> guestHandlers = new ArrayList<>();

    public Host(int port){
        this.ip = "localhost";
        this.port = port;

        //Connect to the main server
        try{
            Socket socket = new Socket("localhost", 1234);
            inFromServer = new Scanner(socket.getInputStream());
            outToServer = new PrintWriter(socket.getOutputStream());
        } catch (IOException e){e.printStackTrace();}
    }

    public void hostMode(){
        //Opening a local server socket that allows guest players connect to it
        try{
            ServerSocket serverSocket = new ServerSocket(port); //ip --> localhost | 127.0.0.1
            System.out.println("Host Server Is Running...");
            for(int i = 0; i < MAX_GUESTS; i++){ //Handling ONLY 3 guests
                Socket guestSocket = serverSocket.accept(); //guest connected to the host
                System.out.println("A Guest Just Connected");

                GuestHandler ch = new GuestHandler(inFromServer, outToServer, guestSocket);
                guestHandlers.add(ch);
                ch.handleClient(guestSocket.getInputStream(), guestSocket.getOutputStream());
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
