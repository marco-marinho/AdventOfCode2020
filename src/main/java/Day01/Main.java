package Day01;

import static Helpers.Util.readFileLongs;
import static java.lang.System.out;

public class Main {
    public static void main(String[] args) {
        var data = readFileLongs("Data/Day01.txt");
        for( var first = 0; first < data.size()-1; first++){
            for (var second = first + 1; second < data.size(); second++){
                if (data.get(first) + data.get(second) != 2020L) continue;
                out.println("Task 01: " + data.get(first)*data.get(second));
            }
        }
        for( var first = 0; first < data.size()-2; first++) {
            for (var second = first + 1; second < data.size() - 1; second++) {
                for (var third = second + 1; third < data.size(); third++) {
                    if (data.get(first) + data.get(second) + data.get(third)!= 2020L) continue;
                    out.println("Task 02: " + data.get(first) * data.get(second) * data.get(third));
                }
            }
        }
    }
}