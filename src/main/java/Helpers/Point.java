package Helpers;

import org.jetbrains.annotations.NotNull;

public record Point(int x, int y) {
    @NotNull
    public Point add(int x, int y){
        return new Point(this.x + x, this.y + y);
    }

    public @NotNull Integer manhattan(){
        return Math.abs(x) + Math.abs(y);
    }

}
