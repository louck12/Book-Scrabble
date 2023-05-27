package Test;

import model.Guest;
import model.Host;
import model.server.BookScrabbleHandler;
import model.server.MyServer;

import java.io.IOException;
import java.util.ArrayList;


public class guestTest
{
    public static void main(String[] args) throws InterruptedException, IOException {

            MyServer server = new MyServer(1234, new BookScrabbleHandler());
            server.start();

        Host h = new Host(8000);
        Thread hostThread = new Thread(() -> {
            h.hostMode();
        });
        hostThread.start();

        Guest g1 = new Guest();
        Thread t=new Thread(()-> {
        });
        t.start();

        //לבדוק אם אפשר להכניס כמה מילים במקביל בלוח, ושינוי הספירה של המילים ב- guest
        if(h.check==1)
        {
            System.out.println("The guest connected to the Host successfully");
        }
        else
        {
            System.out.println("The guest cant connected to the Host");
        }

        ArrayList<String> al=new ArrayList<>();
        String stn1="Moby,7,7,true";
        String stn2="up,7,6,true";
        String stn3="dfdd,2,3,false";
        String stn4="ghgjg,7,7,false";
        al.add(stn1);
        al.add(stn2);
        al.add(stn3);
        al.add(stn4);

        g1.sendWordToHost(stn1);//Word legal and board legal


        g1.sendWordToHost(stn2);//place on board is not legal


        g1.sendWordToHost(stn3);//Word is not legal


        g1.sendWordToHost(stn4);//Word is legal challenge is false

        g1.listenForMessages();

        ArrayList<String> l=g1.l;
        String st1=null;
        String st2=null;
        String st3=null;

        g1.numOfWords=0;
        int j=0;
        for(String s:l) {
            st1 = s.split(" ")[2]; //word
            st2 = s.split(" ")[4]; //query
            st3 = s.split(" ")[5]; //ans query

            if (st2.compareTo("Query:") == 0) {
                if (st3.compareTo("false") == 0) {
                    System.out.println("The word: " + st1 + " is false on query- We ask for challenge!!");
                    g1.challengeWord(al.get(j));
                }
            }
            j++;
        }

        g1.listenForMessages();
g1.closeEverything();

        t.interrupt();
BookScrabbleHandler.b=false;
//h.close();
//BookScrabbleHandler.b=false;

        //  g.listenForMessages();

      //  String s=g.word;
      //System.out.println("The Word is:"+s);

    }






}
