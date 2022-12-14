package Helpers;

import com.google.common.base.Joiner;

import java.util.ArrayList;

public class Matrix<T> {

    ArrayList<ArrayList<T>> data;
    public int nRows;
    public int nCols;

    private T element;

    public Matrix(int nRows, int nCols, T element) {
        this.nRows = nRows;
        this.nCols = nCols;
        this.element = element;
        data = new ArrayList<>();
        for (var i = 0; i < nRows; i++) {
            var cur = new ArrayList<T>();
            for (var j = 0; j < nCols; j++) {
                cur.add(element);
            }
            data.add(cur);
        }
    }

    public Matrix<T> transpose() {
        var output = new Matrix<>(nCols, nRows, element);
        for (var i = 0; i < nRows; i++) {
            for (var j = 0; j < nCols; j++) {
                output.set(j, i, this.get(i, j));
            }
        }
        return output;
    }

    public Matrix<T> flip() {
        var output = new Matrix<>(nRows, nCols, element);
        for (var i = 0; i < nRows; i++) {
            for (var j = 0; j <= nCols / 2; j++) {
                output.set(i, j, this.get(i, nCols - j - 1));
                output.set(i, nCols - j - 1, this.get(i, j));
            }
        }
        return output;
    }

    public int countNeighboursFull(int row, int col, T reference) {
        var count = 0;
        for (var i = -1; i <= 1; i++) {
            for (var j = -1; j <= 1; j++) {
                if (!checkValidIdxs(row + i, col + j) || (i == 0 && j == 0)) continue;
                if (data.get(row + i).get(col + j) == reference) count++;
            }
        }
        return count;
    }

    public ArrayList<T> getRow(int row) {
        var output = new ArrayList<T>();
        for (var i = 0; i < nCols; i++) {
            output.add(get(row, i));
        }
        return output;
    }

    public ArrayList<T> getCol(int col) {
        var output = new ArrayList<T>();
        for (var i = 0; i < nRows; i++) {
            output.add(get(i, col));
        }
        return output;
    }

    public String rightCol() {
        return Joiner.on("").join(getCol(nCols - 1));
    }

    public String leftCol() {
        return Joiner.on("").join(getCol(0));
    }

    public String topRow() {
        return Joiner.on("").join(getRow(0));
    }

    public String bottomRow() {
        return Joiner.on("").join(getRow(nRows - 1));
    }

    public int countElement(T element) {
        var count = 0;
        for (var row : data) {
            for (var el : row) {
                if (el == element) count++;
            }
        }
        return count;
    }

    public boolean checkValidIdxs(int row, int col) {
        return !(row < 0 || col < 0 || row >= nRows || col >= nCols);
    }

    public void set(int row, int col, T value) {
        if (!checkValidIdxs(row, col)) return;
        data.get(row).set(col, value);
    }

    public T get(int row, int col) {
        if (!checkValidIdxs(row, col)) return null;
        return data.get(row).get(col);
    }

    public String toString() {
        var sb = new StringBuilder();
        sb.append("\n");
        for (var row : data) {
            for (var item : row) {
                sb.append(item.toString());
            }
            sb.append("\n");
        }
        return sb.toString();
    }

}
