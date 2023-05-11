package model;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Observable;

public class Model extends Observable {

    public void socketWithGameServer(){
        try{
            Socket socket = new Socket("localhost", 1234);
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            bw.write("hello server!");
            bw.flush();
        } catch (IOException e){e.printStackTrace();}
    }

    public void hostMode(int port){
        try{
            ServerSocket serverSocket = new ServerSocket(8000);
            System.out.println("Host Server Is Running...");
            while (!serverSocket.isClosed()){
                Socket clientSocket = serverSocket.accept(); //guest connected to the host
                System.out.println("A guest just connected");
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                bw.write("hello from the host");
                bw.newLine();
                bw.flush();

            }
        } catch (IOException e){e.printStackTrace();}
    }

    public static void main(String[] args) {

    }
}
