package Day18;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Objects;

import static Helpers.Util.readFile;
import static java.lang.System.out;

public class Day18 {

    public static void main(String[] args) {
        var data = readFile("Data/Day18.txt");
        var sum = 0L;
        var sum2 = 0L;

        for (var line : data) {
            sum += Solve(line, false);
            sum2 += Solve(line, true);
        }
        out.println(sum);
        out.println(sum2);

    }

    static @NotNull Long Solve(@NotNull String input, Boolean precedence) {
        var stack = new ArrayDeque<ArrayList<String>>();
        StringBuilder buffer = new StringBuilder();
        var current = new ArrayList<String>();

        for (var entry : input.toCharArray()) {
            if (entry == '(') {
                stack.addLast(current);
                current = new ArrayList<>();
                continue;
            }
            if (entry == ')') {
                if (buffer.length() > 0) {
                    current.add(buffer.toString());
                }
                if (precedence) ResolveWithPrecedence(current);
                else Resolve(current);
                buffer.setLength(0);
                var temp = stack.removeLast();
                temp.addAll(current);
                current = temp;
            }
            if (entry != ' ' && entry != ')') {
                buffer.append(entry);
                continue;
            }
            if (buffer.length() > 0) {
                current.add(buffer.toString());
            }
            buffer.setLength(0);
        }
        if (buffer.length() > 0) {
            current.add(buffer.toString());
        }
        if (precedence) ResolveWithPrecedence(current);
        else Resolve(current);
        if (current.size() != 1) throw new IllegalStateException("Cold not solve");
        return Long.parseLong(current.get(0));
    }

    static void Resolve(@NotNull ArrayList<String> input) {
        if (input.size() < 3) return;
        while (input.size() >= 3) {
            var first = Long.parseLong(input.get(0));
            var op = input.get(1);
            var second = Long.parseLong(input.get(2));
            var res = 0L;
            if (Objects.equals(op, "*")) {
                res = first * second;
            }
            if (Objects.equals(op, "+")) {
                res = first + second;
            }
            input.remove(0);
            input.remove(0);
            input.remove(0);
            input.add(0, String.valueOf(res));
        }
    }

    static void ResolveWithPrecedence(ArrayList<String> input) {
        var ops = new String[]{"+", "*"};
        for (var opr : ops) {
            while (input.contains(opr)) {
                var idx = input.indexOf(opr);
                var first = Long.parseLong(input.get(idx - 1));
                var op = input.get(idx);
                var second = Long.parseLong(input.get(idx + 1));
                var res = 0L;
                if (Objects.equals(op, "*")) {
                    res = first * second;
                }
                if (Objects.equals(op, "+")) {
                    res = first + second;
                }
                input.remove(idx - 1);
                input.remove(idx - 1);
                input.remove(idx - 1);
                input.add(idx - 1, String.valueOf(res));
            }
        }
    }

}


