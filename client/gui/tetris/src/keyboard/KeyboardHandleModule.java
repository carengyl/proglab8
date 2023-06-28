package keyboard;

import main.MoveDirection;

public interface KeyboardHandleModule {
    void update();

    boolean wasEscPressed();

    MoveDirection getMoveDirection();

    boolean wasRotateRequested();

    boolean wasBoostRequested();
}
