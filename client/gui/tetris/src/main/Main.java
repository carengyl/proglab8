package main;

import graphics.GraphicsModule;
import keyboard.KeyboardHandleModule;

public class Main {
    private static boolean endOfGame;
    private static MoveDirection moveDirection;
    private static GraphicsModule graphicsModule;
    private static KeyboardHandleModule keyboardHandleModule;
    private static final GameField gameField;
    private static boolean isRotateRequested;
    private static boolean isBoostRequested;
    private static int loopNumber;

    static {
        loopNumber = 0;
        endOfGame = false;
        moveDirection = MoveDirection.WAITING;
        isRotateRequested = false;
        isBoostRequested = false;


        gameField = new GameField();
    }


    public static void main(String[] args) {
        while (!endOfGame) {
            input();
            applyLogic();

            graphicsModule.draw();
            graphicsModule.sync(Constants.FPS);
        }

        graphicsModule.destroy();
    }

    private static void input() {
        keyboardHandleModule.update();
        moveDirection = keyboardHandleModule.getMoveDirection();
        isBoostRequested = keyboardHandleModule.wasBoostRequested();
        isRotateRequested = keyboardHandleModule.wasRotateRequested();
        endOfGame = endOfGame || keyboardHandleModule.wasEscPressed() || graphicsModule.isCloseRequested();
    }

    public static void applyLogic() {
        if (moveDirection != MoveDirection.WAITING) {
            gameField.tryMoveFigure(moveDirection);
            moveDirection = MoveDirection.WAITING;
        }

        if (isRotateRequested) {
            gameField.tryRotateFigure();
            isRotateRequested = false;
        }

        if ( (loopNumber% (Constants.FRAMES_PER_MOVE / (isBoostRequested ? Constants.BOOST_MULTIPLIER: 1)) ) == 0) {
            gameField.letFallDown();
        }

        loopNumber = (loopNumber+1)%(Constants.FRAMES_PER_MOVE);
        endOfGame = endOfGame || gameField.isOverfilled();
    }
}