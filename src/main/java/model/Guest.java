//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package model;

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
    Facade gf = new GuestFacade(this);
    public ArrayList<String> l = new ArrayList();

    public Guest() {
    }

    public void connectToHost() {
        try {
            this.socket = new Socket("127.0.0.1", 8000);
            this.inFromHost = new Scanner(this.socket.getInputStream());
            this.outToHost = new PrintWriter(this.socket.getOutputStream());
        } catch (IOException var2) {
            var2.printStackTrace();
        } catch (NoSuchElementException var3) {
            System.out.println("Cannot connect");
        }

    }

    public void sendWordToHost(String word) {
        this.outToHost.println(word);
        this.outToHost.flush();
        ++this.numOfWords;
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
        } catch (IOException var2) {
            var2.printStackTrace();
        }

    }
}
