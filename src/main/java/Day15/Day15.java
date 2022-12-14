package Day15;

import java.util.ArrayList;
import java.util.HashMap;

import static Helpers.Util.readFile;
import static java.lang.System.out;
public class Day15 {

    public static void main(String[] args){
        var data = readFile("Data/Day15.txt");
        var numbers = new ArrayList<Integer>();
        for (var entry: data.get(0).split(",")){
            numbers.add(Integer.parseInt(entry));
        }

        var hist = new HistoryManager();
        for (var i = 0; i < numbers.size(); i++){
            hist.put(numbers.get(i), i+1);
        }

        var last = numbers.get(numbers.size() - 1);

        for (var turn = numbers.size()+1; turn <= 2020; turn++){
            var dif = hist.difference(last);
            if (dif != -1){
               last = dif;
               hist.put(dif, turn);
            }
            else {
                last = 0;
                hist.put(0, turn);
            }
        }
        out.println("Task 01: " + last);

        for (var turn = 2020+1; turn <= 30000000; turn++){
            var dif = hist.difference(last);
            if (dif != -1){
                last = dif;
                hist.put(dif, turn);
            }
            else {
                last = 0;
                hist.put(0, turn);
            }
        }
        out.println("Task 02: " + last);
    }

}


class History {
    Integer first;
    Integer second;

    public History(Integer first){
        this.first = first;
        this.second = -1;
    }
}

class HistoryManager{

    HashMap<Integer, History> history;

    public HistoryManager(){
        history = new HashMap<>();
    }

    public void put(Integer num, Integer turn){
        if (history.containsKey(num)){
            var cur = history.get(num);
            if (cur.second != -1) {
                cur.first = cur.second;
            }
            cur.second = turn;
        }
        else history.put(num, new History(turn));
    }

    public Integer difference(Integer num){
        if (history.containsKey(num)){
            if (history.get(num).second != -1) return history.get(num).second - history.get(num).first;
        }
        return -1;
    }

}