package viewModel;

import model.Model;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ViewModelHost {

    public static ArrayList<ViewModelHostHandler> viewModelHostHandlers = new ArrayList<>();


    public ViewModelHost(Model m){

        new Thread(()->{
            try{
                ServerSocket viewModelServer = new ServerSocket(8090);
                for(int i = 0 ; i < 3; i++){
                    Socket socket = viewModelServer.accept();
                    ViewModelHostHandler vmhh = new ViewModelHostHandler(m, socket);
                    viewModelHostHandlers.add(vmhh);

                    new Thread(()->{
                        try {
                            vmhh.handle(socket.getInputStream(), socket.getOutputStream());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }).start();
                }

            }catch (IOException e){e.printStackTrace();}
        }).start();

    }
}
