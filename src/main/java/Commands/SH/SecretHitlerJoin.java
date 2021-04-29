package Commands.SH;

import Commands.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;

public class SecretHitlerJoin extends Command {
    public SecretHitlerJoin(String s) {
        super(s);
    }

    @Override
    public void start(MessageReceivedEvent event) {
        EmbedBuilder eb = new EmbedBuilder();
        String name = event.getAuthor().getName();
        PlayerList.addPlayer(new Player(name));
        eb.setTitle("Secret Hitler Lobby 1/10");
        eb.setColor(Color.RED);

    }


}