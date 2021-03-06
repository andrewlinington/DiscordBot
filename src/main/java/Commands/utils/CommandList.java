package Commands.utils;

import Commands.Help;
import Commands.PrivateHelp;
import Commands.Rules;
import Commands.SH.Commands.*;

import java.util.ArrayList;

/**
 * Generates all commands as a list for the event listeners
 */
public final class CommandList {

    private static CommandList  c = null;

    private CommandList() {
        commands = new ArrayList<>();
        privateCommands = new ArrayList<>();
        addAllCommands();
        addAllPrivateCommands();
    }

    /**
     * Singleton class for CommandList
     * @return the singleton
     */
    public static CommandList getInstance() {
        if(c == null) {
            c = new CommandList();
        }
        return c;
    }

    private final ArrayList<Command> commands;

    private final ArrayList<Command> privateCommands;

    /**
     * generates a list of private commands
     */
    private void addAllPrivateCommands() {
        privateCommands.add(new PrivateHelp("!help", "Call for help" ));
        privateCommands.add(new LegislativeVote("!policy", "PresidentPick a Policy" ));
    }

    /**
     * generates all the public channel commands
     */
    private void addAllCommands() {
        commands.add(new Help("!help","Call for help"));
        commands.add(new SecretHitlerJoin("!join","Join the Lobby"));
        commands.add(new SecretHitlerLeave("!leave","Leave the Lobby"));
        commands.add(new SecretHitlerLobby("!lobby","Show the Lobby"));
        commands.add(new SecretHitlerStart("!start", "Start the Game"));
        commands.add(new SecretHitlerElection("!elect", "Elect a Chancellor"));
        commands.add(new ShootPlayer("!shoot", "Shoots a Player"));
        commands.add(new PresidentPick("!pick", "Picks the next president" ));
        commands.add(new PresidentInvestigate("!info", "Locates the players public role"));
        commands.add(new Rules("!rules", "Da Rules"));
        commands.add(new KickAll("!clearLobby", "Kick all players"));
        commands.add(new Kick("!kick", "Kicks a given player from an inactive lobby"));
        commands.add(new EndGame("!end", "Ends the active game"));
    }

    /**
     * gets all the public commands
     * @return public commands
     */
    public ArrayList<Command> getCommands() {
        return commands;
    }

    /**
     * gets all the private commands
     * @return public commands
     */
    public ArrayList<Command> getPrivateCommands() {
        return privateCommands;
    }
}
