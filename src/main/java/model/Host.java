package model;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Host {

    private static final int MAX_GUESTS = 3;
    public String ip;
    public int port;

    public Host(int port){
        this.ip = "localhost";
        this.port = port;
    }

    public void hostMode(){
        //Opening a local server socket that allows guest players connect to it
        try{
            ServerSocket serverSocket = new ServerSocket(port); //ip --> localhost | 127.0.0.1
            System.out.println("Host Server Is Running...");
            for(int i = 0; i < MAX_GUESTS; i++){ //Handling ONLY 3 guests
                Socket guestSocket = serverSocket.accept(); //guest connected to the host
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(guestSocket.getOutputStream()));
                //Functionality to handle the guest...
                //....
                System.out.println("A Guest Just Connected");
                bw.write("Hello From The Host");
                bw.newLine();
                bw.flush();
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
