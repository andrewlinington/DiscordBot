package Commands;

import Commands.SH.SecretHitlerJoin;

import java.util.ArrayList;

public final class CommandList {

    private static ArrayList<Command> commands;

    static {
        commands = new ArrayList<>();
        addAllCommands();
    }

    private static void addAllCommands() {
        commands.add(new Help("!help"));
        commands.add(new SecretHitlerJoin("!SH Join"));
    }

    public static ArrayList<Command> getCommands() {
        return commands;
    }
}
