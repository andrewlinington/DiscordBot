package Commands.SH.utils;

import net.dv8tion.jda.api.entities.TextChannel;

public interface Conditions {
    boolean occurs(TextChannel channel);
}
