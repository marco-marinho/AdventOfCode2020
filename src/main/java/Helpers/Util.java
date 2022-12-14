package Helpers;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Util{

    @NotNull
    public static ArrayList<String> readFile(String path){
        try {
            var reader = new BufferedReader(new FileReader(path));
            var line = reader.readLine();
            var output = new ArrayList<String>();
            while(line != null){
                output.add(line);
                line = reader.readLine();
            }
            return output;
        } catch (IOException e){
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
    @NotNull
    public static ArrayList<Long> readFileLongs(String path){
        try {
            var reader = new BufferedReader(new FileReader(path));
            var line = reader.readLine();
            var output = new ArrayList<Long>();
            while(line != null){
                output.add(Long.parseLong(line));
                line = reader.readLine();
            }
            return output;
        } catch (IOException e){
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

}