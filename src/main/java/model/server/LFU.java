package model.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class LFU implements CacheReplacementPolicy {
    ArrayList<String> q = new ArrayList<String>();
    HashMap<String, Integer> usedWordsMapCounter = new HashMap();

    public void add(String name){
        q.add(name);
        if(usedWordsMapCounter.containsKey(name)){
            usedWordsMapCounter.put(name, usedWordsMapCounter.get(name) + 1);
            return;
        }
        usedWordsMapCounter.put(name, 1);
    }

    public String remove(){

        String required = null;
        //MINIMUM VALUE FROM VALUES IN MAP
        int min = Collections.min(usedWordsMapCounter.values());

        ArrayList<String> equalFrequency = new ArrayList<String>();
        usedWordsMapCounter.forEach((key, value) -> {
            if(value == min){
                equalFrequency.add(key);
            }
        });

        for(int i = 0; i < q.size(); i++){
            if(equalFrequency.contains(q.get(i))){
                required = new String(q.get(i));
                q.remove(i);
                usedWordsMapCounter.put(q.get(i), usedWordsMapCounter.get(q.get(i))-1);
                if(usedWordsMapCounter.get(q.get(i)) == 0){
                    usedWordsMapCounter.remove(q.get(i));
                }
                break;
            }
        }
        return required;
    }

}

