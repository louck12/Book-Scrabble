//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package model;

import viewModel.ViewModel;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.locks.ReentrantLock;

public class Host {
    private static final int MAX_GUESTS = 3;
    public String ip = "10.160.2.54";
    public int port;
    PrintWriter outToServer;
    Scanner inFromServer;
    Socket socket;

    TurnManager turnManager;

    public static ArrayList<GuestHandler> guestHandlers = new ArrayList();

    String usernames = "[usernames]:";


    public Host(int port) {
        this.port = port;
    }

    public void connectToMainServer() {
        try {
            this.socket = new Socket("localhost", 1234);
            this.inFromServer = new Scanner(this.socket.getInputStream());
            this.outToServer = new PrintWriter(this.socket.getOutputStream());
        } catch (IOException var2) {
            var2.printStackTrace();
        }

    }

    public void hostMode() {
        try {
            ServerSocket serverSocket = new ServerSocket(this.port);
            System.out.println("Host Server Is Running...");
            turnManager = new TurnManager(guestHandlers);

            for(int i = 0; i < MAX_GUESTS; ++i) {
                Socket guestSocket = serverSocket.accept();
                System.out.println("A Guest Just Connected");
                GuestHandler ch = new GuestHandler(this.inFromServer, this.outToServer, guestSocket, this);
                guestHandlers.add(ch);

                Scanner s = new Scanner(guestSocket.getInputStream());
                String username = s.nextLine();

                turnManager.addGuestToQueue(username);
                usernames+=username;
                usernames+=",";

                System.out.println(usernames);
                for(GuestHandler gh: guestHandlers){
                   PrintWriter w =  new PrintWriter(gh.guestSocket.getOutputStream());
                   w.println(usernames);
                   w.flush();
                }

                new Thread(() -> {
                        try {
                            ch.handleClient(guestSocket.getInputStream(), guestSocket.getOutputStream());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }).start();
            }

            while(!serverSocket.isClosed()) {
                Socket blockedGuestSocket = serverSocket.accept();
                System.out.println("Blocked additional guest from connecting");
                blockedGuestSocket.close();
            }
        } catch (IOException e) {
            System.out.println("Something went wrong with the server socket");
        }

    }


    public void closeEverything() {
        try {
            if (this.socket != null) {
                this.socket.close();
            }

            if (this.inFromServer != null) {
                this.inFromServer.close();
            }

            if (this.outToServer != null) {
                this.outToServer.close();
            }


            System.out.println("the host disconnected");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {

    }

}
