package model;

import java.io.*;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Guest {

    String word;
    String guestName;

    public Guest(String word){
        this.word = word;
        //Guest Mode
        try{
            Socket socket = new Socket("127.0.0.1", 8000); //we're assuming that the guest collected the ip and the port of the host
            Scanner inFromHost = new Scanner(socket.getInputStream());
            PrintWriter outToHost = new PrintWriter(socket.getOutputStream());

            outToHost.println(word);
            outToHost.flush();

            System.out.println("Response from host: " +inFromHost.nextLine());
        }catch (IOException e){e.printStackTrace();} catch (NoSuchElementException e) {
            System.out.println("Cannot connect");
        }

    }

    public static void main(String[] args) {
        Guest g1 = new Guest("Moby");
        Guest g2 = new Guest("OREL");
        Guest g3 = new Guest("DICK");

    }
}

