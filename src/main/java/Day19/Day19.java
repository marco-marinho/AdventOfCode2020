package Day19;

import org.jetbrains.annotations.NotNull;

import java.util.*;

import static Helpers.Util.readFile;
import static java.lang.System.out;
public class Day19 {

    public static void main(String[] args){

        var data = readFile("Data/Day19.txt");
        var it = data.iterator();
        var rules = new HashMap<Integer, Rule>();
        while (it.hasNext()){
            var line = it.next();
            if (line.length() == 0) break;
            var parts = line.split(": ");
            var name = Integer.parseInt(parts[0]);
            if (parts[1].contains("\"")){
                rules.put(name, new Rule(parts[1].charAt(1), new ArrayList<>()));
            }
            else {
                var elements = parts[1].split("\\|");
                var references = new ArrayList<List<Integer>>();
                for (var element : elements){
                    references.add(Arrays.stream(element.trim().split(" ")).mapToInt(Integer::parseInt).boxed().toList());
                }
                rules.put(name, new Rule( null, references));
            }
        }

        var entries = new ArrayList<String>();
        while (it.hasNext()){
            entries.add(it.next());
        }

        var acceptable = ResolveRule(rules.get(0), rules);

        var sum = 0;
        for (var entry : entries){
            if (!acceptable.contains(entry)) continue;
            sum++;
        }

        out.println("Task 01: " + sum);

        var block42 = ResolveRule(rules.get(42), rules);
        var len42 = block42.iterator().next().length();
        var block31 = ResolveRule(rules.get(31), rules);
        var len31 = block31.iterator().next().length();
        var sum2 = 0;

        for (var entry : entries){
            if (entry.length() % len42 != 0 && (entry.length() % len42) % len31 != 0) continue;
            var idx = 0;
            var matched = true;
            var num42 = 0;
            var num31 = 0;
            while (idx < entry.length()){
                var cur = entry.substring(idx, idx + len42);
                idx += len42;
                if (!block42.contains(cur)){
                    idx -= len42;
                    break;
                }
                num42++;
            }
            while (idx < entry.length()){
                var cur = entry.substring(idx, idx + len31);
                idx += len31;
                if (!block31.contains(cur)){
                    matched = false;
                    break;
                }
                num31++;
            }
            if (matched && num42 > num31 && num31 > 0) {
                sum2++;
            }
        }

        out.println("Task 02: " + sum2);

    }

    static @NotNull HashSet<String> ResolveRule(@NotNull Rule input, HashMap<Integer, Rule> rules){
        if (input.acceptable() != null) {
            var output = new HashSet<String>();
            output.add(input.acceptable().toString());
            return output;
        }
        var output = new HashSet<String>();
        for (var reference : input.references()){
            var stems = new HashSet<String>();
            stems.add("");
            for (var element : reference){
                var temp = new HashSet<String>();
                var possibilities = ResolveRule(rules.get(element), rules);
                for (var stem : stems){
                    for (var possibility : possibilities){
                        temp.add(stem + possibility);
                    }
                }
                stems = temp;
            }
            output.addAll(stems);
        }
        return output;
    }

}

record Rule(Character acceptable, ArrayList<List<Integer>> references){}