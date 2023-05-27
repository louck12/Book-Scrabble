//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import model.Facade;
import model.Guest;
import model.GuestFacade;
import model.Host;
import model.server.BookScrabbleHandler;
import model.server.MyServer;

public class GuestTest {
    public GuestTest() {
    }

    public static void main(String[] args) throws InterruptedException, IOException {
        MyServer server = new MyServer(1234, new BookScrabbleHandler());
        System.out.println("Main Server is running...");
        server.start();
        Host h = new Host(8000);
        h.connectToMainServer();
        Thread hostThread = new Thread(() -> {
            h.hostMode();
        });
        hostThread.start();
        Guest g1 = new Guest();
        Facade fg = new GuestFacade(g1);
        fg.connectToServer();
        if (h.check == 1) {
            System.out.println("The guest connected to the Host successfully");
        } else {
            System.out.println("The guest cant connected to the Host");
        }

        ArrayList<String> al = new ArrayList();
        String stn1 = "Moby,7,7,true";
        String stn2 = "Up,7,6,true";
        String stn3 = "BLAB,10,20,true";
        String stn4 = "Run,7,7,false";
        al.add(stn1);
        al.add(stn2);
        al.add(stn3);
        al.add(stn4);
        fg.queryAWord(stn1);
        fg.queryAWord(stn2);
        fg.queryAWord(stn3);
        fg.queryAWord(stn4);
        fg.listenForMessages();
        ArrayList<String> l = g1.l;
        String st1 = null;
        String st2 = null;
        String st3 = null;
        g1.numOfWords = 0;
        int j = 0;

        for(Iterator var16 = l.iterator(); var16.hasNext(); ++j) {
            String s = (String)var16.next();
            st1 = s.split(" ")[2];
            st2 = s.split(" ")[4];
            st3 = s.split(" ")[5];
            if (st2.compareTo("Query:") == 0 && st3.compareTo("false") == 0) {
                System.out.println("The word: " + st1 + " is false on query- We ask for challenge!!");
                fg.challengeAWord((String)al.get(j));
            }
        }

        fg.listenForMessages();
        fg.closeEverything();
    }
}
