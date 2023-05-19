package model;

import model.server.ClientHandler;
import model.server.MyServer;

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
        /*MyServer serverHost = new MyServer(port, new GuestHandler());
        System.out.println("Host local server is running...");
        serverHost.start();*/
        try{
            ServerSocket serverSocket = new ServerSocket(port); //ip --> localhost | 127.0.0.1
            System.out.println("Host Server Is Running...");
            for(int i = 0; i < MAX_GUESTS; i++){ //Handling ONLY 3 guests
                Socket guestSocket = serverSocket.accept(); //guest connected to the host
                //BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(guestSocket.getOutputStream()));
                PrintWriter outToGuest = new PrintWriter(guestSocket.getOutputStream());
                Scanner inFromGuest = new Scanner(guestSocket.getInputStream());


                System.out.println("A Guest Just Connected");
                String guestRequest = inFromGuest.nextLine(); //waiting for guest request
                System.out.println(guestRequest);
                if(guestRequest.equals("Q"))
                    outToServer.write("Q,mobydick.txt,BOOK");

                else if(guestRequest.equals("C"))
                    outToServer.write("C,mobydick.txt,KJCBREJKVRVJ");

                outToServer.newLine();
                outToServer.flush();
                outToGuest.write(inFromServer.readLine()); //Returning the response to the guest

                //out.write("Hello From The Host");
                //out.flush();
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
