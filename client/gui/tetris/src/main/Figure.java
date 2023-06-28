package main;

import graphics.ReadableColor;

public class Figure {
    private final Coordinates metaPointCoords;
    private RotationMode currentRotation;
    private final FigureForm form;

    public Figure(Coordinates metaPointCoords, RotationMode rotation, FigureForm form) {
        this.metaPointCoords = metaPointCoords;
        this.currentRotation = rotation;
        this.form = form;
    }

    public Coordinates[] getCoordinates() {
        return form.getMask().generateFigure(metaPointCoords, currentRotation);
    }

    public Coordinates[] getRotatedCoordinates() {
        return form.getMask().generateFigure(metaPointCoords, RotationMode.getNextRotationFrom(currentRotation));
    }

    public void rotate() {
        this.currentRotation = RotationMode.getNextRotationFrom(currentRotation);
    }

    public Coordinates[] getMovedCoordinates(MoveDirection direction) {
        Coordinates newFirstCell = null;

        switch (direction) {
            case LEFT -> newFirstCell = new Coordinates(metaPointCoords.x - 1, metaPointCoords.y);
            case RIGHT -> newFirstCell = new Coordinates(metaPointCoords.x + 1, metaPointCoords.y);
            default -> ErrorCatcher.wrongParameter("direction (for getMovedCoords)", "Figure");
        }

        return form.getMask().generateFigure(newFirstCell, currentRotation);
    }

    public void move(MoveDirection direction) {
        switch (direction) {
            case LEFT -> metaPointCoords.x--;
            case RIGHT -> metaPointCoords.x++;
            default -> ErrorCatcher.wrongParameter("direction (for move)", "Figure");
        }
    }

    public Coordinates[] getFallenCoordinates() {
        Coordinates newFirstCell = new Coordinates(metaPointCoords.x, metaPointCoords.y - 1);

        return form.getMask().generateFigure(newFirstCell, currentRotation);
    }

    public void fall() {
        metaPointCoords.y--;
    }

    public ReadableColor getColor() {
        return form.getColor();
    }
}
