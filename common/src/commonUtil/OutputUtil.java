package commonUtil;

public class OutputUtil {

    public static void printErrorMessage(Object message) {
        System.out.println("\u001B[31m" + message + "\u001B[0m");
    }

    public static void printSuccessfulMessage(Object message) {
        System.out.println("\u001B[32m" + message + "\u001B[0m");
    }

    public static void printSuccessfulMessageOneStrip(Object message) {
        System.out.print("\u001B[32m" + message + "\u001B[0m");
    }
}
