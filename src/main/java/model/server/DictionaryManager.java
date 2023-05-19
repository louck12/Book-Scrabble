package model.server;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/*
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
*/


public class DictionaryManager
{

    HashMap<String, Dictionary> m=new HashMap();

    public static DictionaryManager get()
    {
        DictionaryManager m=new DictionaryManager();
        return m;
    }

    public boolean query(String...args)
    {
        int flag=0;
        for(int i=0;i<args.length-1;i++)
        {
            String s=args[args.length-1];
            if(!m.containsKey(args[i]))
            {
                Dictionary d=new Dictionary(args);
                m.put(args[i],d);
            }

            File f=new File(args[i]);
            Scanner sc= null;
            try {
                sc = new Scanner(f);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            String st;
            while(sc.hasNext())
            {
                st=sc.next();
                if(st.compareTo(s)==0)
                {
                    flag=1;
                }
            }

        }
        if(flag==0)
        {
            return false;
        }
        return true;
    }

    public boolean challenge(String...args)
    {
        int flag=0;
        for(int i=0;i<args.length-1;i++)
        {
            String s=args[args.length-1];
            if(!m.containsKey(args[i]))
            {
                Dictionary d=new Dictionary(args);
                m.put(args[i],d);
            }

            File f=new File(args[i]);
            Scanner sc= null;
            try {
                sc = new Scanner(f);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            String st;
            while(sc.hasNext())
            {
                st=sc.next();
                if(st.compareTo(s)==0)
                {
                    flag=1;
                }
            }

        }
        if(flag==0)
        {
            return false;
        }
        return true;
    }

    public int getSize()
    {
        return m.size();
    }


}