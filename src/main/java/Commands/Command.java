package Commands;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;


/**
 * the command for dealing with a message received event
 */
public abstract class Command {

    private final String key;
    private final String desc;

    /**
     * Generates the Command for a specific Key
     * @param key the string which is to be matched to run the command
     */
    public Command(String key, String desc) {
        this.key = key;
        this.desc = desc;
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
     * Gets the Key
     * @return the String Key
     */
    public String getKey () {
        return key;
    }

    /**
     * get the description for the help command
     * @return the description of the command
     */
    public String getDescription () {
        return desc;
    }


    /**
     * Manages message received event for a given command
     * @param event the event that started the command
     */
    public void start (MessageReceivedEvent event) {}

}
