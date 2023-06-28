package keyboard.lwjglmodule;

import keyboard.KeyboardHandleModule;
import main.MoveDirection;
import org.lwjgl.input.Keyboard;

public class LWJGLKeyboardHandleModule implements KeyboardHandleModule {
    private boolean wasEscPressed;
    private boolean wasRotateRequested;
    private MoveDirection moveDirection;
    @Override
    public void update() {
        resetValues();

        while (Keyboard.next()) {
            if (Keyboard.getEventKeyState()) {
                switch (Keyboard.getEventKey()) {
                    case Keyboard.KEY_ESCAPE -> wasEscPressed = true;
                    case Keyboard.KEY_UP -> wasRotateRequested = true;
                    case Keyboard.KEY_LEFT -> moveDirection = MoveDirection.LEFT;
                    case Keyboard.KEY_RIGHT -> moveDirection = MoveDirection.RIGHT;
                }
            }
        }
    }

    private void resetValues() {
        wasEscPressed = false;
        wasRotateRequested = false;
        moveDirection = MoveDirection.WAITING;
    }

    @Override
    public boolean wasEscPressed() {
        return wasEscPressed;
    }

    @Override
    public MoveDirection getMoveDirection() {
        return moveDirection;
    }

    @Override
    public boolean wasRotateRequested() {
        return wasRotateRequested;
    }

    @Override
    public boolean wasBoostRequested() {
        return Keyboard.isKeyDown(Keyboard.KEY_DOWN);
    }
}
