package model;

import java.util.*;

public class TurnManager {

    Queue<String> turns;
    ArrayList<GuestHandler> guestHandlers = new ArrayList<>();

    public TurnManager(ArrayList<GuestHandler> guestHandlers){
        turns = new LinkedList<>();
        this.guestHandlers = guestHandlers;
    }
    

    public void addGuestToQueue(String uname){
        turns.add(uname);
    }


    public String nextTurn(){
        if(!turns.isEmpty()){
            turns.add(turns.poll());
            return turns.peek();
        }
        return "";
    }

    public Queue<String> getTurns(){return turns;}


}
