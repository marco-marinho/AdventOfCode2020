package Day22;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;

import static Helpers.Util.readFile;
import static java.lang.System.out;
public class Day22 {

    public static void main(String[] args){

        var data = readFile("Data/Day22.txt");
        var decks = new ArrayList<ArrayDeque<Long>>();
        var curDeck = new ArrayDeque<Long>();

        for (var line : data){
            if (line.length() == 0) continue;
            if (line.contains("Player")) {
                if (curDeck.size() > 0) decks.add(curDeck);
                curDeck = new ArrayDeque<>();
                continue;
            }
            curDeck.add(Long.parseLong(line));
        }
        decks.add(curDeck);

        while(decks.get(0).size() > 0 && decks.get(1).size() > 0) {
            var p1 = decks.get(0).removeFirst();
            var p2 = decks.get(1).removeFirst();
            var toAdd = new ArrayList<Long>();
            toAdd.add(p1);
            toAdd.add(p2);
            toAdd.sort(Collections.reverseOrder());
            if (p1 > p2) {
                decks.get(0).addAll(toAdd);
            } else {
                decks.get(1).addAll(toAdd);
            }
        }

        var winner = decks.get(0).size() > 0 ? 0 : 1;
        var winnerDeck = decks.get(winner);
        var size = winnerDeck.size();
        var sum = 0L;
        for (var i = 0; i < size; i++){
            var cur = winnerDeck.removeFirst();
            sum += cur * (size - i);
        }

        out.println(sum);

    }

}
