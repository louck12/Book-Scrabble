package viewModel;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.*;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ViewModelGuest {

    private Socket socket;
    private BufferedReader in;
    public BufferedWriter out;

    public StringProperty[] tiles;

    public StringProperty currentGuestTurn;

    public ViewModelGuest(){
        try {
            this.socket = new Socket("localhost", 8090);
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {}

        tiles = new SimpleStringProperty[7];
        for(int i = 0; i < tiles.length; i++){
            tiles[i] = new SimpleStringProperty();
        }
        currentGuestTurn = new SimpleStringProperty();
    }

    public void generateRandomTiles() {

        try {
            out.write("generate");
            out.newLine();
            out.flush();

            String ans = in.readLine();
            int len = ans.split(",").length;
            for (int i = 0; i < len; i++) {
                tiles[i].set(String.valueOf(ans.split(",")[i]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

        public void nextTurn(){
        //only one user ask the ViewModelHost
        try{
            currentGuestTurn.set(in.readLine());
        }catch (IOException e){e.printStackTrace();}
    }



}
