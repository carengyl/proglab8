package main;

import graphics.MyReadableColor;

import java.util.Random;

public enum FigureForm {
    I_FORM (CoordinatesMask.I_FORM, MyReadableColor.BLUE),
    J_FORM (CoordinatesMask.J_FORM, MyReadableColor.ORANGE),
    L_FORM (CoordinatesMask.L_FORM, MyReadableColor.YELLOW),
    O_FORM (CoordinatesMask.O_FORM, MyReadableColor.RED),
    S_FORM (CoordinatesMask.S_FORM, MyReadableColor.AQUA),
    Z_FORM (CoordinatesMask.Z_FORM, MyReadableColor.PURPLE),
    T_FORM (CoordinatesMask.T_FORM, MyReadableColor.GREEN);

    private final CoordinatesMask mask;
    private final MyReadableColor color;

    FigureForm(CoordinatesMask mask, MyReadableColor color) {
        this.mask = mask;
        this.color = color;
    }

    private static final FigureForm[] formByNumber = {I_FORM, J_FORM, L_FORM, O_FORM, S_FORM, Z_FORM, T_FORM};

    public CoordinatesMask getMask() {
        return mask;
    }

    public MyReadableColor getColor() {
        return color;
    }

    public static FigureForm getRandomForm() {
        int formNumber = new Random().nextInt(formByNumber.length);
        return formByNumber[formNumber];
    }
}
