package Day09;

import java.util.*;

import static Helpers.Util.readFileLongs;
import static java.lang.System.out;

class Day09 {

    public static void main(String[] args) {

        var data = readFileLongs("Data/Day09.txt");
        var preamble = 25;
        var checker = new RuleCheckers(25);

        var res = -1L;

        for (var i = 0; i < preamble; i++) {
            checker.update(data.get(i));
        }
        for (var i = preamble; i < data.size(); i++) {
            if (!checker.checkAndUpdate(data.get(i))) {
                res = data.get(i);
                break;
            }
        }

        out.println("Task 01: " + res);

        var res2 = new ArrayList<Long>();

        var found = false;
        for (var start = 0; start < data.size(); start++) {
            var sum = 0L;
            for (var cur = start; cur < data.size(); cur++) {
                sum += data.get(cur);
                if (sum > res) break;
                if (sum == res) {
                    res2.addAll(data.subList(start, cur + 1));
                    found = true;
                    break;
                }
            }
            if (found) break;
        }

        var t2 = res2.stream().min(Comparator.comparing(Long::longValue)).get() +
                res2.stream().max(Comparator.comparing(Long::longValue)).get();

        out.println("Task 02: " + t2);

    }

}

class RuleCheckers {

    Integer maxLen;
    LinkedList<Long> numbers;

    public RuleCheckers(Integer maxLen) {
        this.maxLen = maxLen;
        numbers = new LinkedList<>();
    }

    public void update(Long number) {
        numbers.add(number);
        while (numbers.size() > maxLen) {
            numbers.remove(0);
        }
    }

    public boolean checkAndUpdate(Long number) {
        var valid = false;
        var acceptable = new HashSet<Long>();
        for (var i = 0; i < numbers.size() - 1; i++) {
            for (var j = i + 1; j < numbers.size(); j++) {
                if (Objects.equals(numbers.get(i), numbers.get(j))) continue;
                acceptable.add(numbers.get(i) + numbers.get(j));
            }
        }
        for (var pos : acceptable) {
            if (Objects.equals(pos, number)) {
                valid = true;
                break;
            }
        }
        update(number);
        return valid;
    }

}