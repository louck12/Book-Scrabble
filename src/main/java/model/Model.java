package model;

import model.board.Tile;
import viewModel.ViewModel;

import java.util.Observable;

public class Model extends Observable {

    Tile[] tiles;

    String currentGuestTurn;
    private boolean tilesFlag;

    private boolean turnFlag;


    Host host;

    public Model(Host host){
        tiles = new Tile[7];
        currentGuestTurn = new String();
        tilesFlag = false;
        turnFlag = false;
        this.host = host;

        host.connectToMainServer();
        new Thread(()-> {
            host.hostMode();
        }).start();
    }

    public void randomTiles(){
        turnFlag = false;
        tilesFlag = true;
        Tile.Bag bag = Tile.Bag.getBag();
        for(int i = 0; i < tiles.length; i++){
            tiles[i] = bag.getRand();
        }
        setChanged();
        notifyObservers();
    }

    public void nextTurn(){
        tilesFlag = false;
        turnFlag = true;
        currentGuestTurn = host.turnManager.nextTurn();
        setChanged();
        notifyObservers();
    }


    public Tile[] getRandomTiles(){
        return tiles;
    }

    public String getCurrentGuestTurn(){return currentGuestTurn;}

    public boolean getTilesFlag(){return tilesFlag;}
    public boolean getTurnsFlag(){return turnFlag;}



}
