package battleship;

import java.util.HashSet;
import java.util.Set;

public abstract class Ship {

    private int length;
    private int shotsTaken = 0;
    private int[][] cells = new int[length][];

    public Ship(int length) {
        this.length = length;
    }

    public int getLength() {
        return length;
    }
    public int[][] getCells() {
        return cells;
    }
    public int getShotsTaken() {
        return shotsTaken;
    }
    public void shotsTakenIncrement() {
        shotsTaken++;
    }

    public void setCells(int[][] cells) {
        this.cells = cells;
    }

}
