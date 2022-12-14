package Day14;

import java.util.ArrayList;
import java.util.HashMap;

import static Helpers.Util.readFile;
import static java.lang.System.out;

public class Day14 {

    public static void main(String[] args) {

        var data = readFile("Data/Day14.txt");
        var mask = "";
        var memory = new HashMap<Long, Long>();
        var memoryV2 = new HashMap<Long, Long>();

        for (var line : data) {
            if (line.contains("mask")) {
                mask = line.split(" = ")[1];
            } else {
                var pieces = line.split(" = ");
                var value = Long.parseLong(pieces[1]);
                var key = Long.parseLong(pieces[0].substring(pieces[0].indexOf('[') + 1, pieces[0].indexOf(']')));
                var temp = 0L;
                for (var i = 0; i < 36; i++) {
                    if (mask.charAt(i) == '1') {
                        temp = temp | (1L << (35 - i));
                    }
                    else if (mask.charAt(i) == 'X') temp = temp | (value & (1L << (35 - i)));
                }
                memory.put(key, temp);

                var addrList = new ArrayList<Long>();
                addrList.add(0L);
                for (var i = 0; i < 36; i++){
                    var next = new ArrayList<Long>();
                    for (var entry : addrList){
                        if (mask.charAt(i) == '0') next.add(entry | (key & (1L << (35 - i))));
                        else if (mask.charAt(i) == '1') next.add(entry | (1L << (35 - i)));
                        else{
                            next.add(entry | (1L << (35 - i)));
                            next.add(entry);
                        }
                    }
                    addrList = next;
                }

                for (var addr : addrList){
                    memoryV2.put(addr, value);
                }

            }
        }

        var sum = 0L;
        for(var entry : memory.entrySet()){
            sum += entry.getValue();
        }
        out.println("Task 01: " + sum);

        var sumV2 = 0L;
        for(var entry : memoryV2.entrySet()){
            sumV2 += entry.getValue();
        }
        out.println("Task 02: " + sumV2);
    }

}