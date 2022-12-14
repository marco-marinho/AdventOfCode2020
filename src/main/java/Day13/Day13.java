package Day13;

import java.util.ArrayList;
import java.util.Objects;

import static Helpers.Util.readFile;
import static java.lang.System.out;

public class Day13 {

    public static void main(String[] args) {

        var data = readFile("Data/Day13.txt");
        var start = Integer.parseInt(data.get(0));
        var idsStr = data.get(1).split(",");
        var ids = new ArrayList<Integer>();
        var differences = new ArrayList<Integer>();
        var difference = 0;
        for (var entry : idsStr) {
            if (!Objects.equals(entry, "x")) {
                ids.add(Integer.parseInt(entry));
                differences.add(difference);
            }
            difference++;
        }

        var cur = start;
        var curId = 0;
        search:
        while (true) {
            for (var element : ids) {
                if (cur % element == 0) {
                    curId = element;
                    break search;
                }
            }
            cur++;
        }
        var wait = cur - start;
        out.println("Task 01: " + (curId * wait));

        var jump = 1L;
        var estimate = 0L;
        for (var i = 0; i < ids.size(); i++) {
            while (true) {
                estimate += jump;
                if ((estimate + differences.get(i)) % ids.get(i) == 0) {
                    jump *= ids.get(i);
                    break;
                }
            }
        }
        out.println("Task 02: " + estimate);

    }

}
