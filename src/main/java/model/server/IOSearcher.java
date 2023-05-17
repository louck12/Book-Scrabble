package model.server;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class IOSearcher {

    public static boolean search(String word, String...fileName){

        try{

            for(String currentFile: fileName){
                Scanner txtScan = new Scanner(new File(currentFile));
                while(txtScan.hasNextLine()){
                    String str = txtScan.nextLine();
                    if(str.contains(word)){
                        return true;
                    }
                }
            }
            return false;
        }

        catch (FileNotFoundException e){
            e.printStackTrace();
        }

        return false;

    }
}
