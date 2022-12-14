package Day16;

import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.regex.Pattern;

import static Helpers.Util.readFile;
import static java.lang.System.out;

public class Day16 {

    public static void main(String[] args) {
        Pattern range = Pattern.compile("(\\d+-\\d+)");
        var data = readFile("Data/Day16.txt");

        var dataIt = data.iterator();
        var current = dataIt.next();
        var rules = new ArrayList<Rule>();
        var ruleMap = new HashMap<String, Rule>();
        var ruleSet = new HashSet<String>();

        while (current.length() > 0) {
            var matcher = range.matcher(current);
            var limits = new ArrayList<Integer>();
            while (matcher.find()) {
                var pieces = matcher.group(1).split("-");
                limits.add(Integer.parseInt(pieces[0]));
                limits.add(Integer.parseInt(pieces[1]));
            }
            var name = current.split(":")[0];
            var rule = new Rule(name, limits.get(0), limits.get(1), limits.get(2), limits.get(3));
            rules.add(rule);
            ruleMap.put(name, rule);
            ruleSet.add(name);
            current = dataIt.next();
        }

        dataIt.next();
        current = dataIt.next();
        var yours = new ArrayList<>(Arrays.stream(current.split(",")).map(Integer::parseInt).toList());

        dataIt.next();
        dataIt.next();

        var nearby = new ArrayList<ArrayList<Integer>>();
        var valid = new ArrayList<ArrayList<Integer>>();
        while (dataIt.hasNext()) {
            current = dataIt.next();
            nearby.add(new ArrayList<>(Arrays.stream(current.split(",")).map(Integer::parseInt).toList()));
        }

        var errors = new ArrayList<Integer>();
        for (var other : nearby) {
            var problems = checkAll(other, rules);
            errors.addAll(problems);
            if (problems.size() == 0) {
                valid.add(other);
            }
        }
        valid.add(yours);
        out.println("Task 01: " + errors.stream().reduce(0, Integer::sum));

        var rulePosMap = new HashMap<Integer, HashSet<String>>();

        for (var idx = 0; idx < valid.get(0).size(); idx++) {
            rulePosMap.put(idx, new HashSet<>());
            for (var ruleName : ruleSet) {
                var found = true;
                var curRule = ruleMap.get(ruleName);
                for (var other : valid) {
                    var check = curRule.check(other.get(idx));
                    if (!check) {
                        found = false;
                        break;
                    }
                }
                if (found) {
                    rulePosMap.get(idx).add(ruleName);
                }
            }
        }

        var solutionSet = new HashMap<String, Integer>();

        while (!rulePosMap.entrySet().stream().allMatch(c -> c.getValue().size() == 0)) {
            var curName = "";
            for (var entry : rulePosMap.entrySet()) {
                if (entry.getValue().size() == 1) {
                    curName = entry.getValue().iterator().next();
                    solutionSet.put(curName, entry.getKey());
                    break;
                }
            }
            for (var entry : rulePosMap.entrySet()) {
                entry.getValue().remove(curName);
            }
        }

        var prod = 1L;

        for (var entry : solutionSet.entrySet()){
            if (entry.getKey().contains("departure")){
                prod *= yours.get(entry.getValue());
            }
        }

        out.println("Task 02: " + prod);

    }

    static @NotNull ArrayList<Integer> checkAll(@NotNull ArrayList<Integer> data, ArrayList<Rule> rules) {
        var out = new ArrayList<Integer>();
        for (var element : data) {
            var valid = false;
            for (var rule : rules) {
                if (rule.check(element)) {
                    valid = true;
                    break;
                }
            }
            if (!valid) out.add(element);
        }
        return out;
    }

}

record Rule(String name, Integer min1, Integer max1, Integer min2, Integer max2) {

    public boolean check(Integer value) {
        return (value >= min1 && value <= max1) || (value >= min2 && value <= max2);
    }

}