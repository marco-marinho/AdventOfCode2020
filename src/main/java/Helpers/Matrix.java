package Helpers;

import java.util.ArrayList;

public class Matrix<T> {

    ArrayList<ArrayList<T>> data;
    public int nRows;
    public int nCols;

    public Matrix(int nRows, int nCols, T element) {
        this.nRows = nRows;
        this.nCols = nCols;
        data = new ArrayList<>();
        for (var i = 0; i < nRows; i++) {
            var cur = new ArrayList<T>();
            for (var j = 0; j < nCols; j++) {
                cur.add(element);
            }
            data.add(cur);
        }
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

    public int countElement(T element){
        var count = 0;
        for (var row: data){
            for (var el : row){
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
        for (var row : data) {
            for (var item : row) {
                sb.append(item.toString());
            }
            sb.append("\n");
        }
        return sb.toString();
    }

}
