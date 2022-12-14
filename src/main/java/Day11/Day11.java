package Day11;

import Helpers.Matrix;
import Helpers.Point;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static Helpers.Util.readFile;
import static java.lang.System.out;

public class Day11 {

    public static void main(String[] args) {

        var matrix = getData();
        var prevState = matrix.toString();
        while(true)
        {
            matrix = update(matrix);
            if (Objects.equals(matrix.toString(), prevState)) break;
            prevState = matrix.toString();
        }
        out.println("Task 01: " + matrix.countElement('#'));

        matrix = getData();
        prevState = matrix.toString();
        while(true)
        {
            matrix = updateDiag(matrix);
            if (Objects.equals(matrix.toString(), prevState)) break;
            prevState = matrix.toString();
        }
        out.println("Task 02: " + matrix.countElement('#'));
    }

    static @NotNull Matrix<Character> getData(){
        var data = readFile("Data/Day11.txt");
        var matrix = new Matrix<>(data.size(), data.get(0).length(), ' ');
        for (var i = 0; i < data.size(); i++) {
            var row = data.get(i).toCharArray();
            for (var j = 0; j < row.length; j++) {
                matrix.set(i, j, row[j]);
            }
        }
        return matrix;
    }

    static int countDiags(int row, int col, Matrix<Character> matrix){
        var count = 0;
        var directions = new Point[]{new Point(-1, 0), new Point(1, 0), new Point(0, -1),
        new Point(0, 1), new Point(-1, -1), new Point(-1, 1), new Point(1, -1), new Point(1, 1)};
        for (var direction : directions){
            var nextR = row + direction.x();
            var nextC = col + direction.y();
            while (matrix.checkValidIdxs(nextR, nextC)){
                if (matrix.get(nextR, nextC) == 'L') break;
                if (matrix.get(nextR, nextC) == '#') {
                    count++;
                    break;
                }
                nextR += direction.x();
                nextC += direction.y();
            }
        }
        return count;
    }

    static @NotNull Matrix<Character> update(@NotNull Matrix<Character> matrix){
        var next = new Matrix<>(matrix.nRows, matrix.nCols, ' ');
        for (var i = 0; i < matrix.nRows; i++) {
            for (var j = 0; j < matrix.nCols; j++) {
                if (matrix.get(i, j) == '.') next.set(i, j, '.');
                else if (matrix.get(i, j) == '#' && matrix.countNeighboursFull(i, j, '#') >= 4) {
                    next.set(i, j, 'L');
                } else if (matrix.get(i, j) == 'L' && matrix.countNeighboursFull(i, j, '#') == 0) {
                    next.set(i, j, '#');
                } else next.set(i, j, matrix.get(i, j));
            }
        }
        return next;
    }
    static @NotNull Matrix<Character> updateDiag(@NotNull Matrix<Character> matrix){
        var next = new Matrix<>(matrix.nRows, matrix.nCols, ' ');
        for (var i = 0; i < matrix.nRows; i++) {
            for (var j = 0; j < matrix.nCols; j++) {
                if (matrix.get(i, j) == '.') next.set(i, j, '.');
                else if (matrix.get(i, j) == '#' && countDiags(i, j, matrix) >= 5) {
                    next.set(i, j, 'L');
                } else if (matrix.get(i, j) == 'L' && countDiags(i, j, matrix) == 0) {
                    next.set(i, j, '#');
                } else next.set(i, j, matrix.get(i, j));
            }
        }
        return next;
    }

}
