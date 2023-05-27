package model;

public interface Facade {

    public void connectToServer();
    public void queryAWord(String word);
    public void challengeAWord(String word);

    public void listenForMessages();

    public void closeEverything();

}
