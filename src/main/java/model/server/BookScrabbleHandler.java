package model.server;


import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class BookScrabbleHandler implements ClientHandler {
    Scanner scan;
    PrintWriter out;
    DictionaryManager dm;

    public boolean stop = false;
    @Override
    public void handleClient(InputStream inFromclient, OutputStream outToClient) {
        scan = new Scanner(inFromclient);
        out = new PrintWriter(outToClient);
        dm = DictionaryManager.get();
        boolean ans = false;

        while(!stop){
            String line;
            try{
                line = scan.nextLine(); //Waiting for input from the host
            } catch(NoSuchElementException e){close();break;}

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

            out.flush();
        }


    }

    @Override
    public void close() {
        try{
            out.close();
            scan.close();
            stop = true;
        }catch (Exception e){e.printStackTrace();}
    }
}
