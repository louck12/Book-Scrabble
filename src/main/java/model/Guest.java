
package model;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import model.server.BookScrabbleHandler;
import model.server.MyServer;
import view.HelloApplication;
import view.WindowController;
import viewModel.ViewModel;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Guest {
    Socket socket;
    Scanner inFromHost;
    PrintWriter outToHost;
    public int numOfWords = 0;

    WindowController c;

    public ArrayList<String> l = new ArrayList();

    public Guest(WindowController c) {
        this.c = c;
    }

    public void connectToHost() {

        if(socket == null){
            try{
                Thread.sleep(3000);
            }catch (InterruptedException e){}
        }

        try {
            this.socket = new Socket("localhost", 8080);
            this.inFromHost = new Scanner(this.socket.getInputStream());
            this.outToHost = new PrintWriter(this.socket.getOutputStream());
            System.out.println("Listening to the host...");

        } catch (IOException e) {
            System.out.println("cannot connect");
        } catch (NoSuchElementException e1) {
            System.out.println("Cannot connect");
        }

        new Thread(()->{
            while(socket.isConnected()){
                String ans = inFromHost.nextLine();
               if(ans.contains("[usernames]")){
                    String unames = ans.split(":")[1];
                    Platform.runLater(()->c.addUserName(unames));
                }
                else{
                    Platform.runLater(() -> c.handleAnswer(ans));
                }
            }
        }).start();

    }

    public void sendWordToHost(String word) {
        this.outToHost.println(word);
        this.outToHost.flush();
    }

    public void challengeWord(String word) {
        this.outToHost.println("challenge:" + word);
        this.outToHost.flush();
        ++this.numOfWords;
    }

    public void listenForMessages() {
        for(int i = 0; i < this.numOfWords; ++i) {
            String ans = this.inFromHost.nextLine();
            this.l.add(ans);
            System.out.println(ans);
        }
    }



    public void closeEverything() {
        try {
            if (this.socket != null) {
                this.socket.close();
            }

            if (this.inFromHost != null) {
                this.inFromHost.close();
            }

            if (this.outToHost != null) {
                this.outToHost.close();
            }

            System.out.println("Guest just disconnected");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
