package model;

import java.io.*;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Guest {

    String guestName;

    Socket socket;

    Scanner inFromHost;
    PrintWriter outToHost;

    int numOfWords = 0;

    public Guest(String guestName){
        this.guestName = guestName;

        //Guest Mode
        try{
            this.socket = new Socket("127.0.0.1", 8000); //we're assuming that the guest collected the ip and the port of the host
            this.inFromHost = new Scanner(socket.getInputStream());
            this.outToHost = new PrintWriter(socket.getOutputStream());


    /*        outToHost.println("Dick" + "," +guestName);
            outToHost.flush();
            numOfWords++;
*/
            /*outToHost.println("quit,"+guestName);
            outToHost.flush();*/



        }catch (IOException e){e.printStackTrace();} catch (NoSuchElementException e) {
            System.out.println("Cannot connect");
        }

    }

    public void sendWordToHost(String word){
        outToHost.println(word + "," +guestName);
        outToHost.flush();
        numOfWords++;
    }

    public void challengeWord(String word){
        outToHost.println("challenge:"+word+","+guestName);
        outToHost.flush();
        numOfWords++;
    }



    public void listenForMessages(){
        for(int i = 0; i < numOfWords; i++){
            System.out.println(inFromHost.nextLine());
        }
    }




    public void closeEverything() throws IOException {
        try{
            socket.close();
            inFromHost.close();
            outToHost.close();
        }catch (IOException e){e.printStackTrace();}
    }


    public static void main(String[] args) {
        Guest g1= new Guest("Avi");
        //Guest g2= new Guest("Orel");

        g1.sendWordToHost("Moby,5,7,true");
        g1.challengeWord("OREL,5,7,true");
        g1.listenForMessages();

        try{
            g1.socket.close();
        }catch (IOException e){e.printStackTrace();}


        //new Guest("Jump", "Eitan");

        //new Guest("OREL", "Bar").listenForMessages();
        //Guest g3 = new Guest("DICK", "Rona");

    }
}

