package commands;

public class CommandArgument {
    private final int numberOfArgs;
    private int currentArgNumber;
    private String arg;
    private String[] args;

    public CommandArgument(String[] args) {
        this.numberOfArgs = args.length;
        currentArgNumber = 0;
        if (numberOfArgs > 1) {
            this.args = args;
            arg = args[0];
        } else if (numberOfArgs == 1) {
            arg = args[0];
        }
    }

    public String getArg() {
        if (numberOfArgs > 1 && currentArgNumber < args.length - 1) {
            String prevArg = arg;
            arg = args[currentArgNumber++];
            return prevArg;
        }
        return arg;
    }

    public int getNumberOfArgs() {
        return numberOfArgs;
    }
}
