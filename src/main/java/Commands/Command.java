package Commands;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public abstract class Command {

    private String key;


    public Command(String key) {
        this.key = key;
    }


    public boolean keyMatches(String string) {
        return string.startsWith(getKey());
    }

    private String getKey () {
        return key;
    }

    public void start (MessageReceivedEvent event) {

    }

}
