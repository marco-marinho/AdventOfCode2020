package Day02;

import java.util.stream.IntStream;

import static Helpers.Util.readFile;
import static java.lang.System.out;
public class Main {
    public static void main(String[] args) {
        var data = readFile("Data/Day02.txt");
        var valid = 0;
        var valid2 = 0;

        for (var line: data){
            var pieces = line.split(": ");
            var rules = pieces[0].split(" ");
            var target = rules[1].charAt(0);
            var lims = rules[0].split("-");
            var min = Integer.parseInt(lims[0]);
            var max = Integer.parseInt(lims[1]);
            var count = pieces[1].chars().filter(c -> c==target).count();
            var count2 = IntStream
                    .range(0, pieces[1].length())
                    .filter(i -> i == min - 1 || i == max - 1)
                    .mapToObj(i -> pieces[1].charAt(i))
                    .filter(c -> c==target)
                    .count();
            if (min <= count && count <= max){
                valid++;
            }
            if (count2 == 1){
                valid2++;
            }
        }

        out.println("Task 01: " + valid);
        out.println("Task 02: " + valid2);

    }
}