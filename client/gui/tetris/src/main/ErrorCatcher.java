package main;

public class ErrorCatcher {
    public static void wrongParameter(String eParameter, String eClass) {
        System.err.println("Wrong parameter "+eParameter+" occurred in class "+eClass);
        System.exit(-2);
    }

    public static void graphicsFailure(Exception e) {
        System.err.println("GraphicsModule failed.");
        e.printStackTrace();
        System.exit(-3);
    }
}
