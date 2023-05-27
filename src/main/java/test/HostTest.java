package test;

import model.Guest;
import model.Host;
import model.server.BookScrabbleHandler;
import model.server.MyServer;



public class HostTest {

    public static void main(String[] args) {

        MyServer server=new MyServer(1234, new BookScrabbleHandler());
        System.out.println("Server is running...");
        server.start();

        Host hostServer = new Host(8000); //8000 is the port connection to the guests

        hostServer.connectToMainServer(); //connection to the main server

        new Thread(() -> {
            hostServer.hostMode(); //open local server socket
        }).start();

        Guest g1 = new Guest();
        Guest g2 = new Guest();
        Guest g3 = new Guest();
        Guest g4 = new Guest();
        g1.closeEverything();g2.closeEverything();g3.closeEverything();


    }
}
