package model;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Guest {

    public Guest(){

    }

    public static void main(String[] args) {

        //Guest Mode
        try{
            Socket socket = new Socket("127.0.0.1", 8000); //we're assuming that the guest collected the ip and the port of the host
            Scanner inFromHost = new Scanner(socket.getInputStream());
            PrintWriter outToHost = new PrintWriter(socket.getOutputStream());
            /*if(reader.readLine() == null)
                System.out.println("Can not connect to host");
            else
                System.out.println(reader.readLine());*/
            outToHost.println("Moby");
            outToHost.flush();

            System.out.println("Response from host: " +inFromHost.nextLine());
        }catch (IOException e){e.printStackTrace();}
    }


}

