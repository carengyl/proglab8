package entities;

/**
 * Data structure for Car brand.
 *
 * @author carengyl
 */
public enum CarBrand {
    COOL_BRAND(true),
    NOT_COOL_BRAND(false);
    /**
     * Cool field
     */
    private final boolean cool;

    /**
     * Constructs new instance from given Cool argument
     *
     * @param cool boolean Cool field
     */
    CarBrand(boolean cool) {
        this.cool = cool;
    }

    /**
     * @return Cool
     */
    public boolean isCool() {
        return cool;
    }

    /**
     * Builds string enumeration of Car brands
     *
     * @return string enumeration of all Car brands
     */
    public static String show() {
        StringBuilder sb = new StringBuilder();
        for (CarBrand carBrand: CarBrand.values()) {
            sb.append(carBrand.ordinal()+1).append(". ").append(carBrand).append('\n');
        }
        sb.delete(sb.length()-1,sb.length());
        return sb.toString();
    }
}
