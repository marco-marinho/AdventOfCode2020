package Day06;

import java.util.ArrayList;
import java.util.HashSet;

import static Helpers.Util.readFile;
import static java.lang.System.out;

public class Main {
    public static void main(String[] args) {
        var data = readFile("Data/Day06.txt");

        var buffer = new StringBuilder();
        var answers = new ArrayList<String>();
        for (var line : data) {
            if (line.length() > 0) buffer.append(line);
            else {
                answers.add(buffer.toString().trim().replace(" ", ""));
                buffer.setLength(0);
            }
        }
        answers.add(buffer.toString());
        var sum = 0;
        for (var ans : answers) {
            var set = new HashSet<Character>();
            for (var entry : ans.toCharArray()) {
                set.add(entry);
            }
            sum += set.size();
        }
        out.println("Task 01: " + sum);

        var groups = new ArrayList<ArrayList<HashSet<Character>>>();
        var curGroup = new ArrayList<HashSet<Character>>();
        for (var line : data) {
            if (line.length() == 0) {
                groups.add(curGroup);
                curGroup = new ArrayList<>();
                continue;
            }
            var set = new HashSet<Character>();
            for (var entry : line.toCharArray()) {
                set.add(entry);
            }
            curGroup.add(set);
        }
        groups.add(curGroup);

        sum = 0;

        for (var group : groups) {
            var base = group.get(0);
            for (var other = 1; other < group.size(); other++) {
                base.retainAll(group.get(other));
            }
            sum += base.size();
        }

        out.println("Task 02: " + sum);

    }
}