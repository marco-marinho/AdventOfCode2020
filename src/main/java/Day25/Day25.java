package Day25;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import static Helpers.Util.readFile;
import static java.lang.System.out;

public class Day25 {

    public static void main(String[] args) {

        var data = readFile("Data/Day25.txt");
        var targets = new ArrayList<Long>();
        for (var line : data) {
            targets.add(Long.parseLong(line));
        }

        var loops = new HashMap<Long, Long>();

        var found = 0;
        var res = 1L;
        var nLoops = 1L;
        while (found < 2) {
            res = calculateKey(res, 7L);
            if (Objects.equals(res, targets.get(0))) {
                loops.put(targets.get(0), nLoops);
                found++;
            }
            if (Objects.equals(res, targets.get(1))) {
                loops.put(targets.get(1), nLoops);
                found++;
            }
            nLoops++;
        }
        var it = loops.entrySet().iterator();
        var key = it.next().getKey();
        var loopNum = it.next().getValue();

        var temp = 1L;
        for (var i = 0; i < loopNum; i++){
            temp = calculateKey(temp, key);
        }
        out.println("Task 01: " + temp);

    }

    @Contract(pure = true)
    public static @NotNull Long calculateKey(Long current, Long subjectNum) {
        current = current * subjectNum;
        current = current % 20201227L;
        return current;
    }

}
