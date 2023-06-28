package main;

import graphics.MyReadableColor;

import java.util.Random;

import static main.Constants.*;

public class GameField {

    private final MyReadableColor[][] theField;
    private final int[] countFilledCellsInLine;

    private Figure figure;

    public GameField() {
        spawnNewFigure();

        theField = new MyReadableColor[COUNT_CELLS_X][COUNT_CELLS_Y + OFFSET_TOP];

        countFilledCellsInLine = new int[COUNT_CELLS_Y + OFFSET_TOP];

        Random random = new Random();

        for (int y = 0; y < BLOCKS_INITIAL_LEVEL; y++) {
            int missingBlocksCount = MISSING_BLOCKS_IN_INITIAL_LINE_MIN +
                    random.nextInt(MISSING_BLOCKS_IN_INITIAL_LINE_MAX - MISSING_BLOCKS_IN_INITIAL_LINE_MIN);

            int[] missingBlocksXCoordinates = new int[missingBlocksCount];
            missingBlocksXCoordinates[0] = random.nextInt(COUNT_CELLS_X - (missingBlocksCount - 1));
            for (int i = 1; i < missingBlocksCount; i++) {
                int prevCoordinates = missingBlocksXCoordinates[i-1];
                int offset = random.nextInt(COUNT_CELLS_X - (prevCoordinates - 1));

                missingBlocksXCoordinates[i] = prevCoordinates + offset;
            }

            int q = 0;
            for (int x = 0; x < COUNT_CELLS_X; x++) {
                if ( (q < missingBlocksCount) && (missingBlocksXCoordinates[q] == x)) {
                    theField[x][y] = EMPTINESS_COLOR;
                    q++;
                } else {
                    theField[x][y] = MyReadableColor.getRandomColor();
                    countFilledCellsInLine[y]++;
                }
            }
        }

        for (int y = BLOCKS_INITIAL_LEVEL; y < COUNT_CELLS_Y + OFFSET_TOP; y++) {
            for (int x = 0; x < COUNT_CELLS_X; x++) {
                theField[x][y] = EMPTINESS_COLOR;
            }
        }
    }

    private void spawnNewFigure() {
        int randomX = new Random().nextInt(COUNT_CELLS_X - MAX_FIGURE_WIDTH);

        this.figure = new Figure(new Coordinates(randomX, COUNT_CELLS_Y + OFFSET_TOP - 1));
    }

    public boolean isEmpty(int x, int y) {
        return (theField[x][y].equals(EMPTINESS_COLOR));
    }

    public MyReadableColor getColor(int x, int y) {
        return theField[x][y];
    }

    public Figure getFigure() {
        return figure;
    }

    public void tryMoveFigure(MoveDirection direction) {
        Coordinates[] movedCoordinates = figure.getMovedCoordinates(direction);

        if (checkMorphAllowance(movedCoordinates)) {
            figure.move(direction);
        }
    }

    public void tryRotateFigure() {
        Coordinates[] rotatedCoordinates = figure.getRotatedCoordinates();

        if (checkMorphAllowance(rotatedCoordinates)) {
            figure.rotate();
        }
    }

    private boolean checkMorphAllowance(Coordinates[] morphedCoordinates) {
        boolean allowed = true;

        for (Coordinates coordinates: morphedCoordinates) {
            if ((coordinates.y < 0 || coordinates.y >= COUNT_CELLS_Y + OFFSET_TOP)
                    || (coordinates.x < 0 || coordinates.x >= COUNT_CELLS_X)
                    || ! isEmpty(coordinates.x, coordinates.y)) {
                allowed = false;
            }
        }

        return allowed;
    }

    public void letFallDown() {
        Coordinates[] fallenCoordinates = figure.getFallenCoordinates();

        if (checkMorphAllowance(fallenCoordinates)) {
            figure.fall();
        } else {
            Coordinates[] figureCoordinates = figure.getCoordinates();

            boolean haveToShiftLinesDown = false;

            for (Coordinates coordinates: figureCoordinates) {
                theField[coordinates.x][coordinates.y] = figure.getColor();

                countFilledCellsInLine[coordinates.y]++;

                haveToShiftLinesDown = tryDestroyLine(coordinates.y) || haveToShiftLinesDown;
            }

            if (haveToShiftLinesDown) shiftLinesDown();

            spawnNewFigure();
        }
    }

    private void shiftLinesDown() {
        int fallTo = -1;

        for (int y = 0; y < COUNT_CELLS_Y; y++) {
            if (fallTo == -1) { //Если пустот ещё не обнаружено
                if (countFilledCellsInLine[y] == 0) fallTo = y; //...пытаемся обнаружить (._.)
            } else { //А если обнаружено
                if(countFilledCellsInLine[y] != 0) { // И текущую линию есть смысл сдвигать...

                    /* Сдвигаем... */
                    for(int x = 0; x < COUNT_CELLS_X; x++){
                        theField[x][fallTo] = theField[x][y];
                        theField[x][y] = EMPTINESS_COLOR;
                    }

                    /* Не забываем обновить мета-информацию*/
                    countFilledCellsInLine[fallTo] = countFilledCellsInLine[y];
                    countFilledCellsInLine[y] = 0;

                    /*
                     * В любом случае линия сверху от предыдущей пустоты пустая.
                     * Если раньше она не была пустой, то сейчас мы её сместили вниз.
                     * Если раньше она была пустой, то и сейчас пустая -- мы её ещё не заполняли.
                     */
                    fallTo++;
                }
            }
        }
    }

    private boolean tryDestroyLine(int y) {
        if (countFilledCellsInLine[y] < COUNT_CELLS_X) {
            return false;
        }

        for (int x = 0; x < COUNT_CELLS_X; x++) {
            theField[x][y] = EMPTINESS_COLOR;
        }

        /* Не забываем обновить мета-информацию! */
        countFilledCellsInLine[y] = 0;

        return true;
    }

    public boolean isOverfilled() {
        boolean ret = false;

        for(int i = 0; i < OFFSET_TOP; i++){
            if (countFilledCellsInLine[COUNT_CELLS_Y + i] != 0) {
                ret = true;
                break;
            }
        }

        return ret;
    }
}
