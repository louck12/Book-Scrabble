package model.server;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class DictionaryManager {
    public static DictionaryManager dm = null;

    HashMap<String, Dictionary> map = new HashMap<String, Dictionary>();

    public boolean challenge(String...files){
        List<Boolean> checkExistence = new ArrayList<>();
        String word = files[files.length-1];
        String[] f = Arrays.copyOfRange(files, 0, files.length-1);

        for(int i = 0; i < files.length-1; i++){

            if(!map.containsKey(files[i])){
                Dictionary newD = new Dictionary(f);
                checkExistence.add(newD.challenge(word));
                map.put(files[i], newD);
            }
            else{
                checkExistence.add(map.get(files[i]).challenge(word));
            }
        }

        for(Boolean b: checkExistence){
            if(b) return true;
        }
        return false;
    }


    public boolean query(String...files){
        List<Boolean> checkExistence = new ArrayList<>();
        String word = files[files.length-1];
        String[] f = Arrays.copyOfRange(files, 0, files.length-1);

        for(int i = 0; i < files.length-1; i++){
            if(!map.containsKey(files[i])){
                Dictionary newD = new Dictionary(f);
                checkExistence.add(newD.query(word));
                map.put(files[i], newD);
            }
            else{
                checkExistence.add(map.get(files[i]).query(word));
            }
        }

        for(Boolean b: checkExistence){
            if(b) return true;
        }
        return false;
    }

    public int getSize(){
        return map.size();
    }

    public static DictionaryManager get(){
        if(dm == null){
            dm = new DictionaryManager();
        }

        return dm;
    }
}
