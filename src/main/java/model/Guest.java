package model;

import java.io.*;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Guest {

    String word;
    String guestName;

    Socket socket;

    Scanner inFromHost;
    PrintWriter outToHost;

    int numOfWords = 0;

    public Guest(String word, String guestName){
        this.word = word;
        this.guestName = guestName;

        //Guest Mode
        try{
            this.socket = new Socket("127.0.0.1", 8000); //we're assuming that the guest collected the ip and the port of the host
            this.inFromHost = new Scanner(socket.getInputStream());
            this.outToHost = new PrintWriter(socket.getOutputStream());

            outToHost.println(word + "," +guestName);
            outToHost.flush();
            numOfWords++;

            outToHost.println("Dick" + "," +guestName);
            outToHost.flush();
            numOfWords++;

            outToHost.println("quit,"+guestName);
            outToHost.flush();

        }catch (IOException e){e.printStackTrace();} catch (NoSuchElementException e) {
            System.out.println("Cannot connect");
        }

    }

    public void listenForMessages(){
                for(int i = 0; i < numOfWords; i++){
                    try{
                        System.out.println(inFromHost.nextLine());
                    } catch(NoSuchElementException e){
                        System.out.println("Not found element to read");
                    }
                }

    }



    public static void main(String[] args) {
        new Guest("Moby", "Avi").listenForMessages();
        new Guest("Jump", "Eitan").listenForMessages();

        //new Guest("OREL", "Bar").listenForMessages();
        //Guest g3 = new Guest("DICK", "Rona");

    }
}

