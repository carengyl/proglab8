package keyboard;

import main.MoveDirection;

public interface KeyboardHandleModule {
    void update();

    boolean wasEscPressed();

    MoveDirection getShiftDirection();

    boolean wasRotateRequested();

    boolean wasBoostRequested();
}
