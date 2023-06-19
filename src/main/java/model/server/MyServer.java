
package model.server;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyServer {
    private int port;
    private ClientHandler ch;
    private ExecutorService executor;

    private volatile boolean stop;

    public MyServer(int port, ClientHandler ch) {
        this.port = port;
        this.ch = ch;
        this.executor = Executors.newFixedThreadPool(3);
    }

    public void runServer() {
        try {
            ServerSocket server = new ServerSocket(port);
            server.setSoTimeout(1000);
            while (!stop) {
                try {
                    Socket aClient = server.accept();
                    System.out.println("A new host has connected"); //host connected
                    Runnable worker = new WorkerRunnable(aClient, ch);
                    executor.execute(worker);
                } catch (SocketTimeoutException e) {

                }
            }
            server.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void start() {
        stop = false;
        new Thread(() -> runServer()).start();
    }

    public void close() {
        stop = true;
        executor.shutdown();
    }
}

class WorkerRunnable implements Runnable {
    private final Socket socket;
    private final ClientHandler ch;

    public WorkerRunnable(Socket socket, ClientHandler ch) {
        this.socket = socket;
        this.ch = ch;
    }

    @Override
    public void run() {

        try {
            ch.handleClient(socket.getInputStream(), socket.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ch.close();
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
