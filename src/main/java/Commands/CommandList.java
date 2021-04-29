package Commands;

import Commands.SH.*;

import java.util.ArrayList;

public final class CommandList {

    private static ArrayList<Command> commands;

    static {
        commands = new ArrayList<>();
        addAllCommands();
    }

    private static void addAllCommands() {
        commands.add(new Help("!help"));
        commands.add(new SecretHitlerJoin("!join"));
        commands.add(new SecretHitlerLeave("!leave"));
        commands.add(new SecretHitlerLobby("!show"));
        commands.add(new SecretHitlerLobby("!lobby"));
        commands.add(new SecretHitlerStart("!start"));
        commands.add(new SecretHitlerElection("!elect"));

    }

    public static ArrayList<Command> getCommands() {
        return commands;
    }
}
