package model;

public class GuestFacade implements Facade{

    Guest guest;

    public GuestFacade(Guest guest){
        this.guest = guest;
    }

    @Override
    public void connectToServer() {
        guest.connectToHost();
    }

    @Override
    public void queryAWord(String word) {
        guest.sendWordToHost(word);
    }

    @Override
    public void challengeAWord(String word) {
        guest.challengeWord(word);
    }

    public void listenForMessages(){
        guest.listenForMessages();
    }

    public void closeEverything(){guest.closeEverything();}
}
