package Commands;

import Commands.SH.SecretHitlerJoin;
import Commands.SH.SecretHitlerLeave;
import Commands.SH.SecretHitlerLobby;

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
    }

    public static ArrayList<Command> getCommands() {
        return commands;
    }
}
