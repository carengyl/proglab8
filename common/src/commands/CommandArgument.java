package commands;

public class CommandArgument {
    private boolean multipleArgs = false;
    private int currentArgNumber;
    private String arg;
    private String[] args;

    public CommandArgument(String[] args) {
        if (args.length > 1) {
            multipleArgs = true;
            this.args = args;
        }
        this.arg = args[0];
    }

    public String getArg() {
        if (multipleArgs && currentArgNumber < args.length - 1) {
            String prevArg = arg;
            arg = args[currentArgNumber++];
            return prevArg;
        }
        return arg;
    }

    public int getNumberOfArgs() {
        if (multipleArgs) {
            return args.length;
        }
        return 1;
    }
}
