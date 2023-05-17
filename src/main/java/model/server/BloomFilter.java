package model.server;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
public class BloomFilter {
	
    int bitSize;
    ArrayList<String> hashFunctions = new ArrayList<String>();

    BitSet bs;

    public BloomFilter(int bitSize, String...algs){
        this.bitSize = bitSize;
        hashFunctions.addAll(Arrays.asList(algs));
        this.bs = new BitSet(bitSize);
    }

    public void add(String word) {

        try{
            for(String hashFunc: hashFunctions){
                MessageDigest md = MessageDigest.getInstance(hashFunc);
                byte[] bts = md.digest(word.getBytes());
                BigInteger bigInt = new BigInteger(bts);
                int pos = Math.abs(bigInt.intValue());
                bs.set(pos % bitSize);
            }
        }

        catch (Exception e){e.printStackTrace();}
    }

    public boolean contains(String word){
        try{
            for(String hashFunc: hashFunctions){
                MessageDigest md = MessageDigest.getInstance(hashFunc);
                byte[] bts = md.digest(word.getBytes());
                BigInteger bigInt = new BigInteger(bts);
                int pos = Math.abs(bigInt.intValue());
                if(!bs.get(pos % bitSize)){
                    return false;
                }
            }
        }

        catch (Exception e){e.printStackTrace();}

        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(bs.length());
        for(int i = 0; i < bs.length(); i++){
            if(bs.get(i))
                sb.append("1");
            else
                sb.append("0");
        }

        return sb.toString();
    }
}
