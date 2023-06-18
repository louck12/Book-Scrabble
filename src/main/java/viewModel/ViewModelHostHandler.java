package viewModel;

import model.Model;

import java.io.*;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;


public class ViewModelHostHandler implements Observer {

    Model m;

    BufferedReader in;

    BufferedWriter out;

    Socket socket;

    public ViewModelHostHandler(Model m, Socket socket){
        this.m = m;
        this.socket = socket;
        m.addObserver(this);
    }

    public void handle(InputStream in, OutputStream out){
        this.in = new BufferedReader(new InputStreamReader(in));
        this.out = new BufferedWriter(new OutputStreamWriter(out));

        while(true){
            try{
                String msg = this.in.readLine();

                if(msg.equals("generate")){
                    m.randomTiles();
                } else if(msg.equals("nextTurn")){
                    m.nextTurn();
                }
            } catch (IOException e){e.printStackTrace();}
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        String ans = "";
        if(o == m){
            if(m.getTilesFlag()){
                for(int i = 0; i < m.getRandomTiles().length ;i++){
                    if(m.getRandomTiles()[i] != null){
                        ans = ans + m.getRandomTiles()[i].letter+",";
                    }
                }try {
                    out.write(ans);
                    out.newLine();
                    out.flush();
                    System.out.println("socket closed?: "+socket.isClosed());
                }catch (IOException e){e.printStackTrace();}

            } else if(m.getTurnsFlag()){
                try{
                    for(ViewModelHostHandler vm: ViewModelHost.viewModelHostHandlers){
                        vm.out.write(m.getCurrentGuestTurn());
                        vm.out.newLine();
                        vm.out.flush();
                    }
                }catch (IOException e){e.printStackTrace();}

            }

        }
    }
}
