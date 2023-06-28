package main;

import graphics.ReadableColor;

import java.util.Random;

public enum FigureForm {
    I_FORM (CoordinatesMask.I_FORM, ReadableColor.BLUE),
    J_FORM (CoordinatesMask.J_FORM, ReadableColor.ORANGE),
    L_FORM (CoordinatesMask.L_FORM, ReadableColor.YELLOW),
    O_FORM (CoordinatesMask.O_FORM, ReadableColor.RED),
    S_FORM (CoordinatesMask.S_FORM, ReadableColor.AQUA),
    Z_FORM (CoordinatesMask.Z_FORM, ReadableColor.PURPLE),
    T_FORM (CoordinatesMask.T_FORM, ReadableColor.GREEN);

    private final CoordinatesMask mask;
    private final ReadableColor color;

    FigureForm(CoordinatesMask mask, ReadableColor color) {
        this.mask = mask;
        this.color = color;
    }

    private static final FigureForm[] formByNumber = {I_FORM, J_FORM, L_FORM, O_FORM, S_FORM, Z_FORM, T_FORM};

    public CoordinatesMask getMask() {
        return mask;
    }

    public ReadableColor getColor() {
        return color;
    }

    public static FigureForm getRandomForm() {
        int formNumber = new Random().nextInt(formByNumber.length);
        return formByNumber[formNumber];
    }
}
