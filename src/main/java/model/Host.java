//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package model;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Host {
    private static final int MAX_GUESTS = 3;
    public String ip = "localhost";
    public int port;
    PrintWriter outToServer;
    Scanner inFromServer;
    Socket socket;
    ServerSocket serverSocket;
    public int check = 0;
    public static ArrayList<GuestHandler> guestHandlers = new ArrayList();

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
            this.serverSocket = new ServerSocket(this.port);
            System.out.println("Host Server Is Running...");

            for(int i = 0; i < 3; ++i) {
                Socket guestSocket = this.serverSocket.accept();
                System.out.println("A Guest Just Connected");
                this.check = 1;
                (new Thread(() -> {
                    try {
                        GuestHandler ch = new GuestHandler(this.inFromServer, this.outToServer, guestSocket);
                        guestHandlers.add(ch);
                        ch.handleClient(guestSocket.getInputStream(), guestSocket.getOutputStream());
                    } catch (IOException var3) {
                        throw new RuntimeException(var3);
                    }
                })).start();
            }

            while(!this.serverSocket.isClosed()) {
                Socket blockedGuestSocket = this.serverSocket.accept();
                this.check = 0;
                System.out.println("Blocked additional guest from connecting");
                blockedGuestSocket.close();
            }
        } catch (IOException var3) {
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
        } catch (IOException var2) {
            var2.printStackTrace();
        }

    }

    public static void main(String[] args) {
        Host host = new Host(8000);
        host.hostMode();
    }
}
