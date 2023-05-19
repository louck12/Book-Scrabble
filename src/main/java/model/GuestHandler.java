package model;

import model.server.ClientHandler;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Scanner;

public  class GuestHandler implements ClientHandler{
    PrintWriter out;
    Scanner in;

    public GuestHandler(Scanner in, PrintWriter out){
        this.in = in;
        this.out = out;
    }
    @Override
    public void handleClient(InputStream inFromclient, OutputStream outToClient) {
        out=new PrintWriter(outToClient);
        in=new Scanner(inFromclient);
        String text = in.next();
        out.println(new StringBuilder(text).reverse().toString());
        out.flush();
    }

    @Override
    public void close() {
        in.close();
        out.close();
    }

}