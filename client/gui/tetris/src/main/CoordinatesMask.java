package main;

/**
 * Each mask is a template that returns 4 coordinates of real blocks of figure via imaginary coordinates and rotation
 * That means mask generates geometrical shape of figure
 */
public enum CoordinatesMask {
    // Block form [][][][]
    I_FORM(
            new GenerationDelegate() {
                @Override
                public Coordinates[] generateFigure(Coordinates initialCoordinates, RotationMode rotation) {
                    Coordinates[] ret = new Coordinates[4];

                    switch (rotation) {
                        case NORMAL, INVERT -> {
                            ret[0] = initialCoordinates;
                            ret[1] = new Coordinates(initialCoordinates.x, initialCoordinates.y - 1);
                            ret[2] = new Coordinates(initialCoordinates.x, initialCoordinates.y - 2);
                            ret[3] = new Coordinates(initialCoordinates.x, initialCoordinates.y - 3);
                        }
                        case FLIP_CCW, FLIP_CW -> {
                            ret[0] = initialCoordinates;
                            ret[1] = new Coordinates(initialCoordinates.x + 1, initialCoordinates.y);
                            ret[2] = new Coordinates(initialCoordinates.x + 2, initialCoordinates.y);
                            ret[3] = new Coordinates(initialCoordinates.x + 3, initialCoordinates.y);
                        }
                        default -> ErrorCatcher.wrongParameter("rotation", this.getClass().getName());
                    }

                    return ret;
                }
            }
    ),

    J_FORM(
            new GenerationDelegate() {
                @Override
                public Coordinates[] generateFigure(Coordinates initialCoordinates, RotationMode rotation) {
                    Coordinates[] ret = new Coordinates[4];

                    switch (rotation) {
                        case NORMAL -> {
                            ret[0] = new Coordinates(initialCoordinates.x + 1, initialCoordinates.y);
                            ret[1] = new Coordinates(initialCoordinates.x + 1, initialCoordinates.y - 1);
                            ret[2] = new Coordinates(initialCoordinates.x + 1, initialCoordinates.y - 2);
                            ret[3] = new Coordinates(initialCoordinates.x, initialCoordinates.y - 2);
                        }
                        case INVERT -> {
                            ret[0] = new Coordinates(initialCoordinates.x + 1, initialCoordinates.y);
                            ret[1] = initialCoordinates;
                            ret[2] = new Coordinates(initialCoordinates.x, initialCoordinates.y - 1);
                            ret[3] = new Coordinates(initialCoordinates.x, initialCoordinates.y - 2);
                        }
                        case FLIP_CCW -> {
                            ret[0] = initialCoordinates;
                            ret[1] = new Coordinates(initialCoordinates.x + 1, initialCoordinates.y);
                            ret[2] = new Coordinates(initialCoordinates.x + 2, initialCoordinates.y);
                            ret[3] = new Coordinates(initialCoordinates.x + 2, initialCoordinates.y - 1);
                        }
                        case FLIP_CW -> {
                            ret[0] = initialCoordinates;
                            ret[1] = new Coordinates(initialCoordinates.x, initialCoordinates.y - 1);
                            ret[2] = new Coordinates(initialCoordinates.x + 1, initialCoordinates.y - 1);
                            ret[3] = new Coordinates(initialCoordinates.x + 2, initialCoordinates.y - 1);
                        }
                        default -> ErrorCatcher.wrongParameter("rotation", this.getClass().getName());
                    }

                    return ret;
                }
            }
    ),
    /* Block form     []
     *            [][][]
     */
    L_FORM(
            (initialCoordinates, rotation) -> {
                Coordinates[] ret = new Coordinates[4];

                switch (rotation) {
                    case NORMAL -> {
                        ret[0] = initialCoordinates;
                        ret[1] = new Coordinates(initialCoordinates.x, initialCoordinates.y - 1);
                        ret[2] = new Coordinates(initialCoordinates.x, initialCoordinates.y - 2);
                        ret[3] = new Coordinates(initialCoordinates.x + 1, initialCoordinates.y - 2);
                    }
                    case INVERT -> {
                        ret[0] = initialCoordinates;
                        ret[1] = new Coordinates(initialCoordinates.x + 1, initialCoordinates.y);
                        ret[2] = new Coordinates(initialCoordinates.x + 1, initialCoordinates.y - 1);
                        ret[3] = new Coordinates(initialCoordinates.x + 1, initialCoordinates.y - 2);
                    }
                    case FLIP_CCW -> {
                        ret[0] = new Coordinates(initialCoordinates.x, initialCoordinates.y - 1);
                        ret[1] = new Coordinates(initialCoordinates.x + 1, initialCoordinates.y - 1);
                        ret[2] = new Coordinates(initialCoordinates.x + 2, initialCoordinates.y - 1);
                        ret[3] = new Coordinates(initialCoordinates.x + 2, initialCoordinates.y);
                    }
                    case FLIP_CW -> {
                        ret[0] = new Coordinates(initialCoordinates.x, initialCoordinates.y - 1);
                        ret[1] = initialCoordinates;
                        ret[2] = new Coordinates(initialCoordinates.x + 1, initialCoordinates.y);
                        ret[3] = new Coordinates(initialCoordinates.x + 2, initialCoordinates.y);
                    }
                    default -> ErrorCatcher.wrongParameter("rotation", "CoordinatesMask");
                }

                return ret;
            }
    ),
    /* Block form [][]
     *            [][]
     */
    O_FORM(
            (initialCoordinates, rotation) -> {
                Coordinates[] ret = new Coordinates[4];

                ret[0] = initialCoordinates;
                ret[1] = new Coordinates(initialCoordinates.x, initialCoordinates.y - 1);
                ret[2] = new Coordinates(initialCoordinates.x + 1, initialCoordinates.y - 1);
                ret[3] = new Coordinates(initialCoordinates.x + 1, initialCoordinates.y);

                return ret;
            }
    ),
    /* Block form   [][]
     *            [][]
     */
    S_FORM(
            (initialCoordinates, rotation) -> {
                Coordinates[] ret = new Coordinates[4];

                switch (rotation) {
                    case NORMAL, INVERT -> {
                        ret[0] = new Coordinates(initialCoordinates.x, initialCoordinates.y - 1);
                        ret[1] = new Coordinates(initialCoordinates.x + 1, initialCoordinates.y - 1);
                        ret[2] = new Coordinates(initialCoordinates.x + 1, initialCoordinates.y);
                        ret[3] = new Coordinates(initialCoordinates.x + 2, initialCoordinates.y);
                    }
                    case FLIP_CCW, FLIP_CW -> {
                        ret[0] = initialCoordinates;
                        ret[1] = new Coordinates(initialCoordinates.x, initialCoordinates.y - 1);
                        ret[2] = new Coordinates(initialCoordinates.x + 1, initialCoordinates.y - 1);
                        ret[3] = new Coordinates(initialCoordinates.x + 1, initialCoordinates.y - 2);
                    }
                    default -> ErrorCatcher.wrongParameter("rotation", "CoordinatesMask");
                }

                return ret;
            }
    ),
    /* Block form [][]
     *              [][]
     */
    Z_FORM(
            (initialCoordinates, rotation) -> {
                Coordinates[] ret = new Coordinates[4];

                switch (rotation) {
                    case NORMAL, INVERT -> {
                        ret[0] = initialCoordinates;
                        ret[1] = new Coordinates(initialCoordinates.x + 1, initialCoordinates.y);
                        ret[2] = new Coordinates(initialCoordinates.x + 1, initialCoordinates.y - 1);
                        ret[3] = new Coordinates(initialCoordinates.x + 2, initialCoordinates.y - 1);
                    }
                    case FLIP_CCW, FLIP_CW -> {
                        ret[0] = new Coordinates(initialCoordinates.x + 1, initialCoordinates.y);
                        ret[1] = new Coordinates(initialCoordinates.x + 1, initialCoordinates.y - 1);
                        ret[2] = new Coordinates(initialCoordinates.x, initialCoordinates.y - 1);
                        ret[3] = new Coordinates(initialCoordinates.x, initialCoordinates.y - 2);
                    }
                    default -> ErrorCatcher.wrongParameter("rotation", "CoordinatesMask");
                }

                return ret;
            }
    ),
    /* Block form [][][]
     *              []
     */
    T_FORM(
            (initialCoordinates, rotation) -> {
                Coordinates[] ret = new Coordinates[4];

                switch (rotation) {
                    case NORMAL -> {
                        ret[0] = initialCoordinates;
                        ret[1] = new Coordinates(initialCoordinates.x + 1, initialCoordinates.y);
                        ret[2] = new Coordinates(initialCoordinates.x + 1, initialCoordinates.y - 1);
                        ret[3] = new Coordinates(initialCoordinates.x + 2, initialCoordinates.y);
                    }
                    case INVERT -> {
                        ret[0] = new Coordinates(initialCoordinates.x, initialCoordinates.y - 1);
                        ret[1] = new Coordinates(initialCoordinates.x + 1, initialCoordinates.y - 1);
                        ret[2] = new Coordinates(initialCoordinates.x + 1, initialCoordinates.y);
                        ret[3] = new Coordinates(initialCoordinates.x + 2, initialCoordinates.y - 1);
                    }
                    case FLIP_CCW -> {
                        ret[0] = initialCoordinates;
                        ret[1] = new Coordinates(initialCoordinates.x, initialCoordinates.y - 1);
                        ret[2] = new Coordinates(initialCoordinates.x + 1, initialCoordinates.y - 1);
                        ret[3] = new Coordinates(initialCoordinates.x, initialCoordinates.y - 2);
                    }
                    case FLIP_CW -> {
                        ret[0] = new Coordinates(initialCoordinates.x + 1, initialCoordinates.y);
                        ret[1] = new Coordinates(initialCoordinates.x + 1, initialCoordinates.y - 1);
                        ret[2] = new Coordinates(initialCoordinates.x, initialCoordinates.y - 1);
                        ret[3] = new Coordinates(initialCoordinates.x + 1, initialCoordinates.y - 2);
                    }
                    default -> ErrorCatcher.wrongParameter("rotation", "CoordinatesMask");
                }

                return ret;
            }
    );



    private interface GenerationDelegate {
        Coordinates[] generateFigure(Coordinates coordinates, RotationMode rotation);
    }
    private final GenerationDelegate forms;

    CoordinatesMask(GenerationDelegate forms) {
        this.forms = forms;
    }


    public Coordinates[] generateFigure(Coordinates coordinates, RotationMode rotation) {
        return this.forms.generateFigure(coordinates, rotation);
    }
}
