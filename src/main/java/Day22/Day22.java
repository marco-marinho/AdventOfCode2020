package Day22;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

import static Helpers.Util.readFile;
import static java.lang.System.out;
public class Day22 {

    public static void main(String[] args){

        out.println("Task 01: " + calcScore(playGame(getDecks())));
        var res = playGameRecursive(getDecks());
        var winnerDeck = res.decks().get(res.winner() - 1);
        out.println("Task 02: "  + calcScore(winnerDeck));
    }

    static @NotNull ArrayList<ArrayList<Long>> getDecks(){
        var data = readFile("Data/Day22.txt");
        var decks = new ArrayList<ArrayList<Long>>();
        var curDeck = new ArrayList<Long>();

        for (var line : data){
            if (line.length() == 0) continue;
            if (line.contains("Player")) {
                if (curDeck.size() > 0) decks.add(curDeck);
                curDeck = new ArrayList<>();
                continue;
            }
            curDeck.add(Long.parseLong(line));
        }
        decks.add(curDeck);
        return decks;
    }

    static @NotNull ArrayList<ArrayList<Long>> cloneDeck(@NotNull ArrayList<ArrayList<Long>> decks, ArrayList<Long> sizes){
        var output = new ArrayList<ArrayList<Long>>();
        for (var i = 0; i < decks.size(); i++){
            var deck = decks.get(i);
            output.add(new ArrayList<>(deck.subList(0, Math.toIntExact(sizes.get(i)))));
        }
        return output;
    }

    static ArrayList<Long> playGame(@NotNull ArrayList<ArrayList<Long>> decks){
        while(decks.get(0).size() > 0 && decks.get(1).size() > 0) {
            var p1 = decks.get(0).remove(0);
            var p2 = decks.get(1).remove(0);
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
        return decks.get(winner);
    }

    static Long calcScore(@NotNull ArrayList<Long> deck){
        var size = deck.size();
        var sum = 0L;
        for (var i = 0; i < size; i++){
            var cur = deck.remove(0);
            sum += cur * (size - i);
        }
        return sum;
    }


    @Contract("_ -> new")
    static @NotNull Result playGameRecursive(@NotNull ArrayList<ArrayList<Long>> decks) {
        var history = new HashSet<String>();
        while (decks.get(0).size() > 0 && decks.get(1).size() > 0) {
            var state = decks.toString();
            if (history.contains(state)) return new Result(1, decks);
            history.add(state);
            var p1 = decks.get(0).remove(0);
            var p2 = decks.get(1).remove(0);
            Result winner;
            if (p1 <= decks.get(0).size() && p2 <= decks.get(1).size()) {
                var lens = new ArrayList<Long>();
                lens.add(p1);
                lens.add(p2);
                winner = playGameRecursive(cloneDeck(decks, lens));
            }
            else {
                 var winnerIdx = p1 > p2 ? 1 : 2;
                 winner = new Result(winnerIdx, decks);
            }
            var toAdd = new ArrayList<Long>();
            if (winner.winner() == 1) {
                toAdd.add(p1);
                toAdd.add(p2);
                decks.get(0).addAll(toAdd);
            } else {
                toAdd.add(p2);
                toAdd.add(p1);
                decks.get(1).addAll(toAdd);
            }
        }

        var winner = decks.get(0).size() > 0 ? 1 : 2;
        return new Result(winner, decks);
    }

}

record Result(Integer winner, ArrayList<ArrayList<Long>> decks){}
