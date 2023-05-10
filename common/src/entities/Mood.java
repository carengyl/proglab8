package entities;

/**
 * Data structure for Mood
 */
public enum Mood {
    LONGING,
    CALM,
    RAGE,
    FRENZY;

    /**
     * @param i given number
     * @return mood which ordinal equals i-1
     */
    public static Mood getMoodByNumber(int i) {
        for (Mood mood: Mood.values()) {
            if (mood.ordinal() == i-1) {
                return mood;
            }
        }
        return null;
    }

    /**
     * Builds string enumeration of Mood
     *
     * @return string enumeration of Moods
     */
    public static String show() {
        StringBuilder sb = new StringBuilder();
        for (Mood mood: Mood.values()) {
            sb.append(mood.ordinal()+1).append(". ").append(mood).append('\n');
        }
        sb.delete(sb.length()-1,sb.length());
        return sb.toString();
    }
}
