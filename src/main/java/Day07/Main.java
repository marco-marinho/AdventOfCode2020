package Day07;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Objects;
import java.util.regex.Pattern;

import static Helpers.Util.readFile;
import static java.lang.System.out;

public class Main {

    public static void main(String[] args) {
        var containsRegEx = Pattern.compile("(\\d+?.+?) bags*");
        var nameRegEx = Pattern.compile("^(.+?) bags*");

        var data = readFile("Data/Day07.txt");
        var bags = new HashMap<String, Bag>();

        for (var line : data) {
            var nameMatcher = nameRegEx.matcher(line);
            var hasName = nameMatcher.find();
            if (!hasName) throw new IllegalStateException("No name found");
            var name = nameMatcher.group(1);
            var contents = new HashMap<String, Integer>();
            var curBag = new Bag(name, contents);
            var contentsMatcher = containsRegEx.matcher(line);
            while (contentsMatcher.find()) {
                var match = contentsMatcher.group(1);
                var delimiter = match.indexOf(" ");
                var number = Integer.parseInt(match.substring(0, delimiter));
                var content = match.substring(delimiter).trim();
                contents.put(content, number);
            }
            bags.put(name, curBag);
        }

        var res = bags.entrySet().stream().filter(c -> canCarryGold(c.getValue(), bags)).count();
        var res2 = countContents(bags.get("shiny gold"), bags) - 1;

        out.println("Task 01: " + res);
        out.println("Task 02: " + res2);

    }

    static long countContents(@NotNull Bag bag, HashMap<String, Bag> bags) {
       var sum = 1;
       for (var entry : bag.contents().entrySet()){
           sum += entry.getValue() * countContents(bags.get(entry.getKey()), bags);
       }
       return sum;
    }

    static boolean canCarryGold(@NotNull Bag bag, HashMap<String, Bag> bags) {
        var contents = bag.contents();
        for (var entry : contents.keySet()) {
            if (Objects.equals(entry, "shiny gold")) return true;
            if (canCarryGold(bags.get(entry), bags)) return true;
        }
        return false;
    }
}


record Bag(String name, HashMap<String, Integer> contents) {
}
