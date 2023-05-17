package model.server;

import java.util.LinkedList;

public class LRU implements CacheReplacementPolicy{

    LinkedList<String> ls = new LinkedList<String>();
    public void add(String word){
        ls.add(word);
    }

    public String remove(){
        int count = 1;
        String first = new String(ls.getFirst());
        String last = new String(ls.getLast());
        if(!first.equals(last)){
            ls.remove(0);
            return first;
        }

        else{
            while(last.equals(ls.get(count))){
                count++;
            }

            //when all list values are the same
            if(count >= ls.size())
                return null;

            String cur = new String(ls.get(count));
            ls.remove(count);
            return cur;

        }
    }
}
