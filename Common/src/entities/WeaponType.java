package entities;

/**
 * Data structure for Weapon type
 */
public enum WeaponType {
    AXE,
    PISTOL,
    SHOTGUN,
    RIFLE,
    MACHINE_GUN;

    /**
     * Builds string enumeration of Weapon type
     *
     * @return string enumeration of Weapon type
     */
    public static String show() {
        StringBuilder sb = new StringBuilder();
        for (WeaponType weaponType: WeaponType.values()) {
            sb.append(weaponType.ordinal() + 1).append(". ").append(weaponType).append('\n');
        }
        sb.delete(sb.length()-1,sb.length());
        return sb.toString();
    }
}
