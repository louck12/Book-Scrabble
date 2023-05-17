package model.server;


import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Scanner;

public class BookScrabbleHandler implements ClientHandler {
    Scanner scan;
    PrintWriter out;
    DictionaryManager dm;
    @Override
    public void handleClient(InputStream inFromclient, OutputStream outToClient) {
        scan = new Scanner(inFromclient);
        out = new PrintWriter(outToClient);
        DictionaryManager dm = DictionaryManager.get();
        boolean ans = false;

        String line = scan.nextLine();
        String[] splited = line.split(",");
        if(splited[0].equals("Q")){
            ans = dm.query(Arrays.copyOfRange(splited, 1, splited.length));
        }

        else if(splited[0].equals("C")){
            ans = dm.challenge(Arrays.copyOfRange(splited, 1, splited.length));
        }

        if(ans)
            out.println("true");
        else
            out.println("false");
    }

    @Override
    public void close() {
        try{
            out.close();
            scan.close();
        }catch (Exception e){e.printStackTrace();}
    }
}
