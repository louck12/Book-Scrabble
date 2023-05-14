package model;

import java.io.*;
import java.net.Socket;

public class Guest {

    public static void main(String[] args) {

        //Guest Mode
        try{
            Socket socket = new Socket("127.0.0.1", 8000); //we're assuming that the guest collected the ip and the port of the host
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            if(reader.readLine() == null)
                System.out.println("Can not connect to host");
            else
                System.out.println(reader.readLine());
        }catch (IOException e){e.printStackTrace();}
    }
}

