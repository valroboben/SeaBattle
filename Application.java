package battleship;

import java.util.Scanner;

public class Application {

    private final Scanner scanner;
    final String matcherOfShipCoordinates = "[A-J][0-9]0?(\\s*)?[A-J][0-9]0?";
    final String matcherOfShotCoordinates = "[A-J][0-9]0?";

    public Application() {
        this.scanner = new Scanner(System.in);
    }

    private boolean isGameOver = false;


    public void run() {
        BattleField battleField1 = new BattleField();
        BattleField battleField2 = new BattleField();
        gameInitialization(battleField1, battleField2);
        while (!isGameOver) {
            reportShadyBattleField(battleField2);
            System.out.println("---------------------");
            reportBattleField(battleField1);
            System.out.println("Player 1, it's your turn:");
            makeShot(battleField2);
            if (isGameOver) {
                break;
            }

            reportShadyBattleField(battleField1);
            System.out.println("---------------------");
            reportBattleField(battleField2);
            System.out.println("Player 2, it's your turn:");
            makeShot(battleField1);
        }

    } // end of RUN

    private void gameInitialization(BattleField battleField1, BattleField battleField2) {

        System.out.println("Player 1, place your ships on the game field\n");
        reportBattleField(battleField1);
        setShips(battleField1);
        System.out.println("Press Enter and pass the move to another player");
        scanner.nextLine();

        System.out.println("Player 2, place your ships to the game field");
        reportBattleField(battleField2);
        setShips(battleField2);
        System.out.println("Press Enter and pass the move to another player");
        scanner.nextLine();
    }

    private void makeShot(BattleField battleField) {
        while (true) {
            String shotCoordinate = scanner.nextLine();
            if (!shotCoordinate.matches(matcherOfShotCoordinates)) {
                System.out.println("Error! You entered the wrong coordinates! Try again:");
                continue;
            }
            int shotStatus = battleField.shotStatus(shotCoordinate);
            reportShotStatus(shotStatus);
            if (shotStatus == 5) {
                isGameOver = true;
            } else {
                System.out.println("Press Enter and pass the move to another player");
                scanner.nextLine();
            }
            break;
        }
    }

    private void reportShotStatus(int shotStatus) {
        switch (shotStatus) {
            case 0:
                System.out.println("EPIC FAIL!");
                break;
            case 1:
                System.out.println("You missed. Try again:\n");
                break;
            case 2:
            case 4:
                System.out.println("You hit a ship! Try again:\n");
                break;
            case 3:
                System.out.println("You sank a ship! Specify a new target:\n");
                break;
            case 5:
                System.out.println("You sank the last ship. You won. Congratulations!\n");
                break;
        }
    }

    private void setShips(BattleField battleField) {
        int shipToSet = 1;
        while (shipToSet < 6) {
            System.out.printf("Enter the coordinates of the %s (%d cells):\n",
                    newShips.findNameByIndex(shipToSet), newShips.findLengthByIndex(shipToSet));
            while (true) {
                String coordinatesOfShip = scanner.nextLine();
                if (!coordinatesOfShip.matches(matcherOfShipCoordinates)) {
                    System.out.println("Error! You entered the wrong coordinates! Try again:");
                    continue;
                }
                String[] coordinates = coordinatesOfShip.split("\\s+");
                if ((coordinates[0].charAt(0) - 16 != coordinates[1].charAt(0) - 16) &&
                        Integer.parseInt(coordinates[0].substring(1)) != Integer.parseInt(coordinates[1].substring(1))) {
                    System.out.println("Error! Wrong ship location! Try again:");
                    continue;
                }
                if (checkShipLength(coordinates, shipToSet) != newShips.findLengthByIndex(shipToSet)) {
                    System.out.println("Error! Wrong length of the " + newShips.findNameByIndex(shipToSet) + "! Try again:");
                    continue;
                }
                if (!battleField.checkShipCoordinates(coordinatesOfShip)) {
                    System.out.println("Error! You placed it too close to another one. Try again:");
                    continue;
                }
                battleField.setNewShip(shipToSet, coordinates);
                break;
            }
            reportBattleField(battleField);
            shipToSet++;
        }

    }

    private int checkShipLength(String[] coordinates, int shipToSet) {
        if (coordinates[0].charAt(0) == coordinates[1].charAt(0)) {
            return Math.abs(Integer.parseInt(coordinates[0].substring(1)) - Integer.parseInt(coordinates[1].substring(1))) + 1;
        } else {
            return Math.abs(coordinates[0].charAt(0) - coordinates[1].charAt(0)) + 1;
        }
    }

    private enum newShips {
        AIRCRAFT_CARRIER("Aircraft Carrier", 1, 5),
        BATTLESHIP("Battleship", 2, 4),
        SUBMARINE("Submarine", 3, 3),
        CRUISER("Cruiser", 4, 3),
        DESTROYER("Destroyer", 5, 2);
        final String name;
        final int identifyNumber;
        final int length;

        newShips(String name, int identifyNumber, int length) {
            this.name = name;
            this.identifyNumber = identifyNumber;
            this.length = length;
        }

        public String getName() {
            return name;
        }

        public int getIdentifyNumber() {
            return identifyNumber;
        }

        public int getLength() {
            return length;
        }

        public static newShips findEnumByIndex(int index) {
            for (newShips value : values()) {
                if (value.identifyNumber == index) {
                    return value;
                }
            }
            return null;
        }

        public static int findLengthByIndex(int index) {
            for (newShips value : values()) {
                if (value.identifyNumber == index) {
                    return value.getLength();
                }
            }
            return 0;
        }

        public static String findNameByIndex(int index) {
            for (newShips value : values()) {
                if (value.identifyNumber == index) {
                    return value.getName();
                }
            }
            return "";
        }
    }

    private void reportShadyBattleField(BattleField battleField) { // print battlefield with completed moves and "for of war"
        char[][] currentField = battleField.getShadyBattlefield();
        char lineName = 'A';
        System.out.println("  1 2 3 4 5 6 7 8 9 10");
        for (int i = 1; i < 11; i++) {
            System.out.print(lineName + " ");
            lineName++;
            for (int j = 1; j < 11; j++) {
                System.out.print(currentField[i][j]);
                if (j < 10) {
                    System.out.print(" ");
                }
            }
            System.out.println();
        }
    }

    private void reportBattleField(BattleField battleField) { // print battlefield without "fog og war"
        char[][] currentField = battleField.getBattleField();
        char lineName = 'A';
        System.out.println("  1 2 3 4 5 6 7 8 9 10");
        for (int i = 1; i < 11; i++) {
            System.out.print(lineName + " ");
            lineName++;
            for (int j = 1; j < 11; j++) {
                System.out.print(currentField[i][j]);
                if (j < 10) {
                    System.out.print(" ");
                }
            }
            System.out.println();
        }
        System.out.println();
    }

}
