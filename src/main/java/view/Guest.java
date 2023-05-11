package view;

import java.io.*;
import java.net.Socket;

public class Guest {
    public static void main(String[] args) {

        //Guest Mode
        try{
            Socket socket = new Socket("localhost", 8000);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println(reader.readLine());
        }catch (IOException e){e.printStackTrace();}
    }
}

