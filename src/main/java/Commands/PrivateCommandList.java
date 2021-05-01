package Commands;

import Commands.SH.Commands.LegislativeVote;

import java.util.ArrayList;

public class PrivateCommandList {

    private static ArrayList<Command> commands;

    static {
        commands = new ArrayList<>();
        addAllCommands();
    }

    private static void addAllCommands() {
        commands.add(new PrivateHelp("!help"));
        commands.add(new LegislativeVote("!vote"));

    }

    public static ArrayList<Command> getCommands() {
        return commands;
    }

}
