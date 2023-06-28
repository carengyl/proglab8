package client.util;

import common.commands.CommandArgument;

public record CommandToSend(String commandName, CommandArgument commandArgument) {
}
