package battleship;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class BattleField {

    private char[][] battleField;
    private char[][] shadyBattlefield;

    private ArrayList<Ship> ships = new ArrayList<>();
    public ArrayList<Ship> getShips() {
        return ships;
    }
    
    public char[][] getBattleField() {
        return battleField;
    }
    public char[][] getShadyBattlefield() {
        return shadyBattlefield;
    }

    public int shotStatus(String shotCoordinates) {
        int row = shotCoordinates.charAt(0) - 64;
        int column = Integer.parseInt(shotCoordinates.substring(1));
        if (battleField[row][column] == '~' || battleField[row][column] == 'M') {
            battleField[row][column] = 'M';
            shadyBattlefield[row][column] = 'M';
            return 1; // miss
        }
        if (battleField[row][column] == 'X') {
            return 2; // repeated hit of a ship that was previously hit
        }
        if (battleField[row][column] == 'O') { // check if ship is sunk
            battleField[row][column] = 'X';
            shadyBattlefield[row][column] = 'X';
            for (Ship ship : ships) {
                int[][] currentShipArray = ship.getCells();
                for (int i = 0; i < ship.getLength(); i++) {
                    int[] currentArray = currentShipArray[i];
                    if (currentArray[0] == row && currentArray[1] == column) {
                        ship.shotsTakenIncrement();
                        if (ship.getShotsTaken() == ship.getLength()) {
                            if (isAllShipsSunk()) {
                                return 5; // all ships destroyed
                            }
                            return 3; // ship is sunk
                        } else {
                            return 4; // hit a ship
                        }
                    }
                }
            }
            battleField[row][column] = 'X';
            shadyBattlefield[row][column] = 'X';
        }
        return 0; // something went wrong
    }

    private boolean isAllShipsSunk() {
        for (Ship ship : ships) {
            if (ship.getShotsTaken() < ship.getLength()) {
                return false;
            }
        }
        return true;
    }

    public BattleField() {
        this.battleField = new char[12][12];
        for (int i = 1; i < 12; i++) {
            char[] currentCharArr = new char[12];
            Arrays.fill(currentCharArr, '~');
            battleField[i] = currentCharArr;
        }
        this.shadyBattlefield = new char[12][12];
        for (int i = 1; i < 12; i++) {
            char[] currentCharArr = new char[12];
            Arrays.fill(currentCharArr, '~');
            shadyBattlefield[i] = currentCharArr;
        }
    } // created an empty battleField and an empty shadyBattleField filled with '~'

    public void setNewShip (int index, String[] coordinates) {
        Ship ship = makeNewShip(index);
        int firstRow = coordinates[0].charAt(0) - 64;
        int firstColumn = Integer.parseInt(coordinates[0].substring(1));
        int secondRow = coordinates[1].charAt(0) - 64;
        int secondColumn = Integer.parseInt(coordinates[1].substring(1));
        int[][] shipCellsArray = new int[ship.getLength()][];
        int arraySlot = 0;
        if (firstRow == secondRow) {
            for (int i = Math.min(firstColumn, secondColumn); i < Math.max(firstColumn, secondColumn) + 1; i++) {
                battleField[firstRow][i] = 'O';
                int[] current = new int[2];
                current[0] = firstRow;
                current[1] = i;
                shipCellsArray[arraySlot] = current;
                arraySlot++;
            }
            ship.setCells(shipCellsArray);
        } else {
            for (int i = Math.min(firstRow, secondRow); i < Math.max(firstRow, secondRow) + 1; i++) {
                battleField[i][firstColumn] = 'O';
                int[] current = new int[2];
                current[0] = i;
                current[1] = firstColumn;
                shipCellsArray[arraySlot] = current;
                arraySlot++;
            }
            ship.setCells(shipCellsArray);
        }
        ships.add(ship);
    }

    public boolean checkShipCoordinates(String coordinatesOfShip) {
        String[] coordinates = coordinatesOfShip.split("\\s+");
        int firstRow = coordinates[0].charAt(0) - 64;
        int firstColumn = Integer.parseInt(coordinates[0].substring(1));
        int secondRow = coordinates[1].charAt(0) - 64;
        int secondColumn = Integer.parseInt(coordinates[1].substring(1));

        if (firstRow == secondRow) {
            for (int i = Math.min(firstColumn, secondColumn); i < Math.max(firstColumn, secondColumn) + 1; i++) {
                if (battleField[firstRow][i] == 'O' || battleField[firstRow][i - 1] == 'O' || battleField[firstRow - 1][i - 1] == 'O' ||
                        battleField[firstRow - 1][i] == 'O' || battleField[firstRow - 1][i + 1] == 'O' || battleField[firstRow][i + 1] == 'O' ||
                        battleField[firstRow + 1][i + 1] == 'O' || battleField[firstRow + 1][i] == 'O' || battleField[firstRow + 1][i - 1] == 'O') {
                    return false;
                }
            }
        } else {
            for (int i = Math.min(firstRow, secondRow); i < Math.max(firstRow, secondRow) + 1; i++) {
                if (battleField[i][firstColumn] == 'O' || battleField[i][firstColumn - 1] == 'O' || battleField[i - 1][firstColumn - 1] == 'O' ||
                        battleField[i - 1][firstColumn] == 'O' || battleField[i - 1 ][firstColumn + 1] == 'O' || battleField[i][firstColumn + 1] == 'O' ||
                        battleField[i + 1][firstColumn + 1] == 'O' || battleField[i + 1][firstColumn] == 'O' || battleField[i + 1][firstColumn - 1] == 'O') {
                    return false;
                }
            }
        }
        return true;
    }

    private Ship makeNewShip(int index) {
        switch (index) {
            case 1: return new AircraftCarrier();
            case 2: return new Battleship();
            case 3: return new Submarine();
            case 4: return new Cruiser();
            case 5: return new Destroyer();
        }
        return null;
    }

}
