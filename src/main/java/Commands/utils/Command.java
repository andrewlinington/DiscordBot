package Commands.utils;

import lombok.Getter;
import lombok.Setter;
import main.utils.SHGame;
import main.utils.ServerGame;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Locale;

@Getter
@Setter
public abstract class Command {

    private final String key;
    private final String description;
    private SHGame game;

    /**
     * Generates the Command for a specific Key
     * @param key the string which is to be matched to run the command
     */
    public Command(String key, String desc) {
        this.key = key.toLowerCase(Locale.ROOT);
        this.description = desc;
    }

    /**
     * Confirms the keyMatches the given String
     * @param string the string from the user
     * @return if the string is equivalent
     */
    public boolean keyMatches(String string) {
        return string.equals(getKey());
    }

    /**
     * Manages message received event for a given command and greaabs the current guild for the user
     * @param event the event that started the command
     */
    public void start (MessageReceivedEvent event) {
       game =  (event.isFromType(ChannelType.PRIVATE)) ? privateCheck(event) : ServerGame.getGames().get(event.getGuild().getIdLong()) ;
    }

    /**
     * checks if the event from a private channel was made by an active player and returns nothing otherwise
     * @param event the private channel the message was sent in
     * @return
     */
    private SHGame privateCheck (MessageReceivedEvent event) {
        Guild g = ServerGame.findUserServer(event.getAuthor());
        return (g != null) ? ServerGame.getGames().get(g.getIdLong()): null;
    }

}
