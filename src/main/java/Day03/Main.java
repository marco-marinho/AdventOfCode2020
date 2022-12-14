package Day03;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static Helpers.Util.readFile;
import static java.lang.System.out;

public class Main {

    private record Slope(int x, int y){}

    public static void main(String[] args) {

        var tress = readFile("Data/Day03.txt");
        var slope = new Slope(1, 3);
        out.println("Task 01: " + CountTrees(slope, tress));

        var slopes = new Slope[]{new Slope(1, 1), new Slope(1, 3), new Slope(1, 5), new Slope(1, 7), new Slope(2,1)};
        var res = 1;
        for (var tempSlope: slopes){
            res *= CountTrees(tempSlope, tress);
        }
        out.println("Task 02: " + res);

    }

    public static int CountTrees(Slope slope, @NotNull ArrayList<String> trees){
        var limY = trees.get(0).length();
        var row = 0;
        var col = 0;
        var count = 0;
        while(row < trees.size()){
            var element = trees.get(row).charAt(col);
            if (element == '#') count++;
            row += slope.x;
            col += slope.y;
            col %= limY;
        }
        return count;
    }
}
