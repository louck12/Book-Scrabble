package model.server;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Dictionary {

    CacheManager existWords;
    CacheManager notExistWords;

    BloomFilter bf;
    ArrayList<String> fileNames = new ArrayList<String>();

    public Dictionary(String...files){
        //Cache Manager with LRU
        existWords = new CacheManager(400, new LRU());

        //Cache Manager with LFU
        notExistWords = new CacheManager(100, new LFU());

        bf =new BloomFilter(256,"MD5","SHA1");

        fileNames.addAll(Arrays.asList(files));

        try{
            for(String file: fileNames){
                File currentFile = new File(file);
                Scanner input = new Scanner(currentFile);
                while(input.hasNext()){
                    String word = input.next();
                    bf.add(word);
                }
            }

        }

        catch (FileNotFoundException e){
            e.printStackTrace();
        }


    }

    public boolean query(String word){
        if(existWords.query(word))
            return true;

        if(notExistWords.query(word))
            return false;

        boolean ans = bf.contains(word);
        if(ans)
            existWords.add(word);
        else
            notExistWords.add(word);

        return ans;
    }

    public boolean challenge(String word){
        boolean ans = IOSearcher.search(word, fileNames.toArray(new String[0]));
        if(ans)
            existWords.add(word);
        else
            notExistWords.add(word);
        return ans;
    }

}
