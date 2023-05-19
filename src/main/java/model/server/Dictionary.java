package model.server;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/*public class Dictionary {

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

}*/

public class Dictionary {
    CacheManager cm1;
    CacheManager cm2;
    BloomFilter bf;
    IOSearcher io;
    ArrayList<String> filenames = new ArrayList<>();

    CacheReplacementPolicy c;

    public Dictionary(String... filenames) {

        String s;
        LRU lr = new LRU();
        LFU lf = new LFU();
        bf = new BloomFilter(256, "SHA1", "MD5");
        this.cm1 = new CacheManager(400, lr);
        this.cm2 = new CacheManager(100, lf);
        this.io = new IOSearcher();
        for (int i = 0; i < filenames.length; i++) {
            this.filenames.add(filenames[i].toString());
        }

        for (int j = 0; j < filenames.length; j++) {
            try {
                File f = new File(filenames[j]);
                String[] words = null;
                FileReader fr = new FileReader(f);
                BufferedReader br = new BufferedReader(fr);
                String st;
                while ((st = br.readLine()) != null) {
                    words = st.split(" ");
                    for (String word : words) {
                        bf.add(word);
                    }
                }
            } catch (Exception e) {

            }

        }

    }

    public boolean query(String s) {
        boolean b1, b2, b3;

        b1 = cm1.query(s);
        b2 = cm2.query(s);
        b3 = bf.contains(s);

        if (b1) {
            return true;
        } else {


            if (b2) {
                return false;
            } else {

            }
            if (b3) {
                return true;
            } else {
                return false;
            }
        }
    }

    public boolean challenge(String s) {
        for (int i = 0; i < filenames.size(); i++) {
            try {
                boolean b = io.search(s, filenames.get(i));

                if (b) {
                    return true;
                } else {
                    return false;
                }
            } catch (Exception e) {

            }
        }
        return false;
    }
}

