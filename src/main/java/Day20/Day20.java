package Day20;

import java.util.*;

import Helpers.Matrix;
import Helpers.Point;
import com.google.common.base.Joiner;
import org.jetbrains.annotations.NotNull;

import static Helpers.Util.readFile;
import static java.lang.System.out;

public class Day20 {

    public static void main(String[] args) {

        var data = readFile("Data/Day20.txt");
        var tiles = new HashMap<Integer, Matrix<Character>>();
        var number = 0;
        var curMatrix = new Matrix<>(1, 1, 'c');
        var row = 0;

        for (var line : data) {
            if (line.contains("Tile")) {
                number = Integer.parseInt(line.replace("Tile ", "").replace(":", ""));
                curMatrix = new Matrix<>(10, 10, ' ');
                row = 0;
                continue;
            }
            if (line.length() == 0) {
                tiles.put(number, curMatrix);
                continue;
            }
            for (var i = 0; i < line.length(); i++) {
                curMatrix.set(row, i, line.charAt(i));
            }
            row++;
        }
        tiles.put(number, curMatrix);

        var borderMap = genBorders(tiles);
        var connections = getConnections(borderMap);

        var prod = 1L;
        var borders = connections.entrySet().stream().filter(c -> c.getValue().size() == 2).toList();
        for (var border : borders) {
            prod *= border.getKey();
        }
        out.println(prod);

        var jigSaw = getJigSaw(tiles, borderMap, connections);

        var lengths = (int) Math.round(Math.sqrt(tiles.size()));
        var assembled = new Matrix<>(lengths * 8, lengths * 8, ' ');
        for (var piece : jigSaw.entrySet()){
            var location = piece.getKey();
            var tile = tiles.get(piece.getValue());
            for (var i = 1; i < 9; i++){
                for (var j = 1; j < 9; j++) {
                    assembled.set(location.x() * 8 + i - 1, location.y() * 8 + j - 1, tile.get(i, j));
                }
            }
        }

        out.println(assembled);

    }

    static @NotNull HashMap<Point, Integer> getJigSaw(@NotNull HashMap<Integer, Matrix<Character>> tiles,
                                                      HashMap<Integer, ArrayList<HashSet<String>>> borderMap,
                                                      @NotNull HashMap<Integer, ArrayList<Integer>> connections){
        var jigSaw = new HashMap<Point, Integer>();
        var lengths = (int) Math.round(Math.sqrt(tiles.size()));
        var borders = connections.entrySet().stream().filter(c -> c.getValue().size() == 2).toList();

        var refCorner = borders.iterator().next();
        jigSaw.put(new Point(0 ,0), refCorner.getKey());
        var cornerMat = tiles.get(refCorner.getKey());
        var cornerConnect = connections.get(refCorner.getKey());

        var currentTile = alignCorner(cornerMat, cornerConnect, borderMap);
        tiles.put(refCorner.getKey(), currentTile);
        var currentId = refCorner.getKey();

        var y = 0;
        while(y < lengths) {
            if (y!=0) {
                var id = jigSaw.get(new Point(0, y - 1));
                currentTile = tiles.get(id);
                var next = findConnection(id, tiles, borderMap, connections, Connection.RIGH_COLUMN);
                var aligned = alignBorder(tiles.get(next), getSlice(currentTile, Connection.RIGH_COLUMN), Connection.LEFT_COLUMN);
                tiles.put(next, aligned);
                currentTile = aligned;
                currentId = next;
                jigSaw.put(new Point(0, y), next);
            }
            var x = 1;
            while (x < lengths) {
                var next = findConnection(currentId, tiles, borderMap, connections, Connection.BOTTOM_ROW);
                var aligned = alignBorder(tiles.get(next), getSlice(currentTile, Connection.BOTTOM_ROW), Connection.TOP_ROW);
                tiles.put(next, aligned);
                currentTile = aligned;
                jigSaw.put(new Point(x, y), next);
                currentId = next;
                x++;
            }
            y++;
        }
        return jigSaw;
    }

    static String getSlice(Matrix<Character> tile, @NotNull Connection border){
        return switch (border) {
            case TOP_ROW -> tile.topRow();
            case BOTTOM_ROW -> tile.bottomRow();
            case LEFT_COLUMN -> tile.leftCol();
            case RIGH_COLUMN -> tile.rightCol();
        };
    }

    static Integer findConnection(Integer refId, @NotNull HashMap<Integer, Matrix<Character>> tiles,
                                  HashMap<Integer, ArrayList<HashSet<String>>> borderMap,
                                  @NotNull HashMap<Integer, ArrayList<Integer>> connections,
                                  Connection connection) {

        var reference = getSlice(tiles.get(refId), connection);

        var possibilities = connections.get(refId);

        for (var possibility : possibilities){
            var borders = borderMap.get(possibility);
            for (var border : borders){
               if (border.contains(reference)) return possibility;
            }
        }

        return -1;
    }

    static Matrix<Character> alignBorder(Matrix<Character> tile, String reference, Connection connection){
        var state = 0;
        while (true) {
            if (state == 1) {
                tile = tile.transpose();
            }
            if (state == 2) {
                tile = tile.flip();
                state = 0;
            }
            var toMatch = getSlice(tile, connection);
            if (reference.equals(toMatch)) return tile;
            state++;
        }
    }

    static Matrix<Character> alignCorner(Matrix<Character> cornerMat, @NotNull ArrayList<Integer> connections1, HashMap<Integer, ArrayList<HashSet<String>>> borderMap) {
        var state = 0;
        while (true) {
            if (state == 1) {
                cornerMat = cornerMat.transpose();
            }
            if (state == 2) {
                cornerMat = cornerMat.flip();
                state = 0;
            }
            var leftCol = Joiner.on("").join(cornerMat.getCol(cornerMat.nCols - 1));
            var bottomRow = Joiner.on("").join(cornerMat.getRow(cornerMat.nRows - 1));
            var found = 0;
            for (var conn : connections1) {
                for (var entry : borderMap.get(conn)) {
                    if (entry.contains(leftCol) || entry.contains(bottomRow)) {
                        found++;
                    }
                }
            }
            if (found == 2) break;
            state++;
        }
        return cornerMat;
    }

    static @NotNull HashMap<Integer, ArrayList<HashSet<String>>> genBorders(@NotNull HashMap<Integer, Matrix<Character>> tiles) {

        var borderMap = new HashMap<Integer, ArrayList<HashSet<String>>>();

        for (var entry : tiles.entrySet()) {
            var curSet = new HashSet<String>();
            var curMat = entry.getValue();
            var borderSets = new ArrayList<HashSet<String>>();
            var topRow = curMat.getRow(0);
            curSet.add(Joiner.on("").join(topRow));
            Collections.reverse(topRow);
            curSet.add(Joiner.on("").join(topRow));
            borderSets.add(curSet);
            curSet = new HashSet<>();
            var bottomRow = curMat.getRow(curMat.nRows - 1);
            curSet.add(Joiner.on("").join(bottomRow));
            Collections.reverse(bottomRow);
            curSet.add(Joiner.on("").join(bottomRow));
            borderSets.add(curSet);
            curSet = new HashSet<>();
            var rightCol = curMat.getCol(0);
            curSet.add(Joiner.on("").join(rightCol));
            Collections.reverse(rightCol);
            curSet.add(Joiner.on("").join(rightCol));
            var leftCol = curMat.getCol(curMat.nCols - 1);
            borderSets.add(curSet);
            curSet = new HashSet<>();
            curSet.add(Joiner.on("").join(leftCol));
            Collections.reverse(leftCol);
            curSet.add(Joiner.on("").join(leftCol));
            borderSets.add(curSet);
            borderMap.put(entry.getKey(), borderSets);
        }
        return borderMap;
    }

    static @NotNull HashMap<Integer, ArrayList<Integer>> getConnections(@NotNull HashMap<Integer, ArrayList<HashSet<String>>> borderMap) {
        var connections = new HashMap<Integer, ArrayList<Integer>>();

        for (var entry : borderMap.entrySet()) {
            var curConnections = new ArrayList<Integer>();
            borderLoop:
            for (var border : entry.getValue()) {
                for (var variation : border) {
                    for (var other : borderMap.entrySet()) {
                        if (Objects.equals(other.getKey(), entry.getKey())) continue;
                        for (var otherVariation : other.getValue()) {
                            if (otherVariation.contains(variation)) {
                                curConnections.add(other.getKey());
                                continue borderLoop;
                            }
                        }
                    }
                }
            }
            connections.put(entry.getKey(), curConnections);
        }
        return connections;
    }

}

enum Connection {
    LEFT_COLUMN,
    RIGH_COLUMN,
    TOP_ROW,
    BOTTOM_ROW,
}