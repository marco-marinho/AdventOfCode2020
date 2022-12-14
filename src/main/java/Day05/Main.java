package Day05;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

import static Helpers.Util.readFile;
import static java.lang.System.out;

public class Main {
    public static void main(String[] args) {

        var data = readFile("Data/Day05.txt");
        var ids = new ArrayList<Integer>(data.size());
        for (var line : data) {
            var row = binarySearch(line.substring(0, 7), 127);
            var col = binarySearch(line.substring(7), 7);
            ids.add(row * 8 + col);
        }

        var max = Collections.max(ids, null);
        var min = Collections.min(ids, null);
        var presentSet = new HashSet<>(ids);
        out.println("Task 01: " + max);
        for (var i = min; i <= max; i++){
            if (!presentSet.contains(i)){
                out.println("Task 02: " + i);
                break;
            }
        }
    }

    public static int binarySearch(@NotNull String input, int Max) {
        var min = 0;
        var max = Max;
        for (var entry : input.chars().toArray()) {
            var half = min + ((max - min) / 2);
            if (entry == 'F' || entry == 'L') max = half;
            else min = half + 1;
        }
        return min;
    }

}
