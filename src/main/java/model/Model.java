package model;

import java.io.*;
import java.net.Socket;
import java.util.Observable;

public class Model extends Observable {

    //TODO
    //Add FACADE
    //Implement test layer for each class in the model
    //Write GANTT chart in readMe.md

    public static void communicationWithGameServer(){
        try{
            Socket socket = new Socket("localhost", 1234);
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            bw.write("Hello Server!");
            bw.flush();
        } catch (IOException e){e.printStackTrace();}
    }

    public static void main(String[] args) {
        communicationWithGameServer();
    }

}
