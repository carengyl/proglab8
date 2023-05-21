package clientUtil;

import commands.CommandArgument;

public record CommandToSend(String commandName, CommandArgument commandArgument) {
}
