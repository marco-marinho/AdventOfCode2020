package Day17;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import static Helpers.Util.readFile;
import static java.lang.System.out;

public class Day17 {

    public static void main(String[] args) {
        var data = readFile("Data/Day17.txt");
        var active = new HashSet<IPoint>();
        var active4D = new HashSet<IPoint>();
        for (var x = 0; x < data.size(); x++) {
            for (var y = 0; y < data.get(x).length(); y++) {
                if (data.get(x).charAt(y) == '#') {
                    active.add(new Point3D((long) x, (long) y, 0L));
                    active4D.add(new Point4D((long) x, (long) y, 0L, 0L));
                }
            }
        }

        for (var i = 0; i < 6; i++) {
            active = runCycle(active);
        }
        for (var i = 0; i < 6; i++) {
            active4D = runCycle(active4D);
        }
        out.println("Task 01: " + active.size());
        out.println("Task 02: " + active4D.size());
    }

    static @NotNull HashSet<IPoint> runCycle(@NotNull HashSet<IPoint> active) {
        var nextActive = new HashSet<IPoint>();
        var innactNghCnt = new HashMap<IPoint, Integer>();

        for (var point : active) {
            var neighbours = point.getNeighbours();
            var count = 0;
            for (var other : neighbours) {
                if (active.contains(other)) count++;
                else innactNghCnt.put(other, innactNghCnt.getOrDefault(other, 0) + 1);
            }
            if (count == 2 || count == 3) nextActive.add(point);
        }
        for (var entry : innactNghCnt.entrySet()) {
            if (entry.getValue() == 3) nextActive.add(entry.getKey());
        }
        return nextActive;
    }

}

interface IPoint{
    ArrayList<IPoint> getNeighbours();
}

record Point3D(Long x, Long y, Long z) implements IPoint {

    @Override
    public @NotNull ArrayList<IPoint> getNeighbours() {
        var output = new ArrayList<IPoint>();
        for (var x = -1; x <= 1; x++) {
            for (var y = -1; y <= 1; y++) {
                for (var z = -1; z <= 1; z++) {
                    if (x == 0 && y == 0 && z == 0) continue;
                    output.add(new Point3D(this.x + x, this.y + y, this.z + z));
                }
            }
        }
        return output;
    }
}

record Point4D(Long x, Long y, Long z, Long w) implements IPoint {

    public @NotNull ArrayList<IPoint> getNeighbours() {
        var output = new ArrayList<IPoint>();
        for (var x = -1; x <= 1; x++) {
            for (var y = -1; y <= 1; y++) {
                for (var z = -1; z <= 1; z++) {
                    for (var w = -1; w <= 1; w++) {
                        if (x == 0 && y == 0 && z == 0 && w == 0) continue;
                        output.add(new Point4D(this.x + x, this.y + y, this.z + z, this.w + w));
                    }
                }
            }
        }
        return output;
    }
}