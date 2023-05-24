package model.server;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class MyServer {
    private int port;
    private ClientHandler ch;

    private volatile boolean stop;

    public MyServer(int port, ClientHandler ch){
        this.port = port;
        this.ch = ch;
    }

    public void runServer() {
        try{
            ServerSocket server = new ServerSocket(port);
            server.setSoTimeout(1000);
            while(!stop) {
                try {
                    Socket aClient = server.accept();
                    System.out.println("A new host has connected"); //host connected
                    ch.handleClient(aClient.getInputStream(), aClient.getOutputStream());
                    ch.close();
                    aClient.close();
                } catch (SocketTimeoutException e) {}
            }
            server.close();
        } catch (IOException e){e.printStackTrace();}
    }

    public void start() {
        stop = false;
        new Thread(()->runServer()).start();
    }

    public void close(){
        stop = true;
    }
	
}
