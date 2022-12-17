package Day24;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import static Helpers.Util.readFile;
import static java.lang.System.out;

public class Day24 {

    public static void main(String[] args) {
        var data = readFile("Data/Day24.txt");
        var commands = new ArrayList<ArrayList<Direction>>();
        for (var line : data) {
            var temp = new ArrayList<Direction>();
            var idx = 0;
            while (idx < line.length()) {
                if (idx + 2 <= line.length()) {
                    var twoChar = line.substring(idx, idx + 2);
                    var size = temp.size();
                    if (twoChar.equals("se")) temp.add(Direction.SOUTH_EAST);
                    if (twoChar.equals("ne")) temp.add(Direction.NORTH_EAST);
                    if (twoChar.equals("sw")) temp.add(Direction.SOUTH_WEST);
                    if (twoChar.equals("nw")) temp.add(Direction.NORTH_WEST);
                    if (temp.size() > size) {
                        idx += 2;
                        continue;
                    }
                }
                var oneChar = line.substring(idx, idx + 1);
                if (oneChar.equals("w")) {
                    temp.add(Direction.WEST);
                    idx++;
                }
                else if (oneChar.equals("e")) {
                    temp.add(Direction.EAST);
                    idx++;
                }
                else throw new IllegalStateException("Failed to parse commands");
            }
            commands.add(temp);
        }

        var flipped = new HashMap<HexCoord, Integer>();

        for (var command : commands){
            var current = new HexCoord(0, 0,0);
            for (var direction: command){
                current = current.walk(direction);
            }
            flipped.put(current, flipped.getOrDefault(current, 0) + 1);
        }

        var blackTiles = new HashSet<HexCoord>();
        for (var entry : flipped.entrySet()){
            if (entry.getValue() % 2 == 0) continue;
            blackTiles.add(entry.getKey());
        }

        out.println("Task 01: " + blackTiles.size());

        for (var i = 0; i < 100; i++){
            updateTiles(blackTiles);
        }

        out.println("Task 02: " + blackTiles.size());

    }

    static void updateTiles(@NotNull HashSet<HexCoord> blackTiles){

        var whiteTiles = new HashMap<HexCoord, Integer>();
        var toRemove = new HashSet<HexCoord>();
        for (var tile : blackTiles){
            var adjBlk = 0;
            for (var neighbour : tile.getNeighbours()){
                if (blackTiles.contains(neighbour)) adjBlk++;
                else {
                    whiteTiles.put(neighbour, whiteTiles.getOrDefault(neighbour, 0) + 1);
                }
            }
            if (adjBlk == 0 || adjBlk > 2) toRemove.add(tile);
        }

        blackTiles.removeAll(toRemove);

        for (var entry : whiteTiles.entrySet()){
            if (entry.getValue() == 2) blackTiles.add(entry.getKey());
        }

    }

}


record HexCoord(Integer q, Integer r, Integer s) {

    public HexCoord walk(@NotNull Direction direction) {
        return switch (direction) {
            case NORTH_WEST -> new HexCoord(q, r - 1, s + 1);
            case NORTH_EAST -> new HexCoord(q + 1, r - 1, s);
            case WEST -> new HexCoord(q - 1, r, s + 1);
            case EAST -> new HexCoord(q + 1, r, s - 1);
            case SOUTH_WEST -> new HexCoord(q - 1, r + 1, s);
            case SOUTH_EAST -> new HexCoord(q, r + 1, s - 1);
        };
    }

    public @NotNull ArrayList<HexCoord> getNeighbours(){
        var output = new ArrayList<HexCoord>();
        for (var direction : Direction.values()){
            output.add(walk(direction));
        }
        return output;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        HexCoord hexCoord = (HexCoord) o;

        return new EqualsBuilder().append(q, hexCoord.q).append(r, hexCoord.r).append(s, hexCoord.s).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(q).append(r).append(s).toHashCode();
    }
}

enum Direction {
    NORTH_WEST,
    NORTH_EAST,
    WEST,
    EAST,
    SOUTH_WEST,
    SOUTH_EAST,
}