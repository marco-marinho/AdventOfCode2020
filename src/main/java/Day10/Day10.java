package Day10;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import static Helpers.Util.readFileLongs;
import static java.lang.System.out;

public class Day10 {

    public static void main(String[] args) {

        var data = readFileLongs("Data/Day10.txt");
        data.add(0, 0L);
        data.sort(Comparator.comparing(Long::longValue));
        data.add(data.get(data.size() - 1) + 3);
        var difs = new HashMap<Long, Integer>();
        for (var i = 0; i < data.size() - 1; i++) {
            var dif = data.get(i + 1) - data.get(i);
            var cur = difs.getOrDefault(dif, 0);
            difs.put(dif, cur + 1);
        }
        var res = difs.getOrDefault(1L, 0) * difs.getOrDefault(3L, 0);
        out.println("Task 01: " + res);
        var res2 = dynamicProg(data);
        out.println("Task 02: " + res2);
    }

    public static Long dynamicProg(ArrayList<Long> data) {
        var reversed = new ArrayList<>(data);
        Collections.reverse(reversed);
        var sums = new ArrayList<Long>();
        sums.add(1L);
        for (var i = 1; i < reversed.size(); i++) {
            var current = reversed.get(i);
            var sum = 0L;
            for (var j = i - 1; j >= 0; j--) {
                var other = reversed.get(j);
                if (other <= current + 3) sum += sums.get(j);
                else break;
            }
            sums.add(sum);
        }
        return sums.get(sums.size() - 1);
    }

}