package model.server;


import java.util.HashSet;

public class CacheManager {

    HashSet<String> words = new HashSet<String>();
    int size;
    CacheReplacementPolicy crp;

    public CacheManager(int size, CacheReplacementPolicy crp){
        this.size = size;
        this.crp = crp;
    }

    public boolean query(String word){
        return words.contains(word);
    }

    public void add(String word){
        crp.add(word);
        words.add(word);

        if(words.size() > size){
            String nameToRemove = crp.remove();
            words.remove(nameToRemove);
        }
    }

}
